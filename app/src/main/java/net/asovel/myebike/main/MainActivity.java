package net.asovel.myebike.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.backendless.Backendless;

import net.asovel.myebike.LoginActivity;
import net.asovel.myebike.R;
import net.asovel.myebike.backendless.common.Defaults;
import net.asovel.myebike.resultadosebikes.EBikeListActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String NAME = "MainActivity";
    public static final String EMAIL = "EMAIL";

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private int mSelectedId = -1;
    private boolean connected;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION);

        iniUI();
    }

    private void iniUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_inicio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        fab = (FloatingActionButton) findViewById(R.id.btn_buscar_ebikes);

        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        String email = prefs.getString("email", "");

        setConnected(email);
    }

    private void setConnected(String email) {

        MenuItem item = navigationView.getMenu().getItem(5);

        if (!email.equals("")) {
            connected = true;
            item.setTitle("Cerrar sesión");
        } else {
            connected = false;
            email = "Desconectado";
            item.setTitle("Iniciar sesión");
        }

        View navHeader = navigationView.getHeaderView(0);
        TextView textEmail = (TextView) navHeader.findViewById(R.id.text_email);
        textEmail.setText(email);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.menu_myebike_appbar:
                MenuItem item1 = navigationView.getMenu().getItem(1);
                onNavigationItemSelected(item1);
                return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (mSelectedId == id) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        Fragment fragment = null;
        Bundle bundle = new Bundle();
        boolean fabVisibility = false;

        switch (id) {
            case R.id.menu_top_ventas:
                fragment = new TopVentas();
                break;
            case R.id.menu_myebike:
                fragment = new MyEBike();
                fabVisibility = true;
                break;
            case R.id.menu_nosotros:
                fragment = new WebAsovel();
                bundle.putString(WebAsovel.PAGINA_WEB, "http://www.asovel.net/?page_id=484");
                break;
            case R.id.menu_antes:
                fragment = new WebAsovel();
                bundle.putString(WebAsovel.PAGINA_WEB, "http://www.asovel.net/?page_id=475");
                break;
            case R.id.menu_asovel:
                fragment = new WebAsovel();
                bundle.putString(WebAsovel.PAGINA_WEB, "http://www.asovel.net/");
                break;
            case R.id.menu_sesion:
                if (connected) {
                    SharedPreferences prefs = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("email", "");
                    editor.commit();
                    setConnected("");
                    return true;
                }
                Intent intent = new Intent(this, LoginActivity.class);
                bundle.putString(LoginActivity.CALLER, NAME);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
        }

        fragment.setArguments(bundle);
        mSelectedId = id;

        if (fabVisibility)
            fab.setVisibility(View.VISIBLE);
        else
            fab.setVisibility(View.INVISIBLE);

        getSupportFragmentManager().beginTransaction().replace(R.id.principal, fragment).commit();
        item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}

