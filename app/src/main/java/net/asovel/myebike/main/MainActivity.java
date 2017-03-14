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
import net.asovel.myebike.resultadosebikes.FragmentListEBike;
import net.asovel.myebike.utils.Constants;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

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

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_home);
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

        MenuItem item = navigationView.getMenu().getItem(7);

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
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
                fragment = new FragmentListEBike();
                ArrayList<String> listClauses = new ArrayList<>(1);
                listClauses.add("patrocinado_SORT0 > 0");
                bundle.putStringArrayList(Constants.WHERECLAUSE, listClauses);
                bundle.putString(Constants.CALLER, TAG);
                break;
            case R.id.menu_myebike:
                fragment = new FragmentAsistente();
                fabVisibility = true;
                break;
            case R.id.menu_buscar_marca:
                fragment = new FragmentListMarca();
                break;
            case R.id.menu_buscar_tienda:
                fragment = new FragmentMapa();
                bundle.putString(Constants.CALLER, TAG);
                break;
            case R.id.menu_recomendaciones:
                fragment = new FragmentWeb();
                bundle.putString(Constants.URL, "http://sites.google.com/asovel.org/web/e-bikes/antes-de-comprar");
                break;
            case R.id.menu_noticias:
                fragment = new FragmentWeb();
                bundle.putString(Constants.URL, "http://asovel.blogspot.com.es/?m=1");
                break;
            case R.id.menu_nosotros:
                fragment = new FragmentWeb();
                bundle.putString(Constants.URL, "http://sites.google.com/asovel.org/web/asociación/sobre-nosotros");
                break;
            case R.id.menu_sesion:
                if (connected) {
                    SharedPreferences prefs = getSharedPreferences(Constants.LOGIN, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.remove(Constants.EMAIL);
                    editor.remove(Constants.USER_ID);
                    editor.commit();
                    setConnected("");
                    return true;
                }
                Intent intent = new Intent(this, LoginActivity.class);
                bundle.putString(Constants.CALLER, TAG);
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

        if (id != R.id.menu_buscar_tienda)
            setTitle(item.getTitle());

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}

