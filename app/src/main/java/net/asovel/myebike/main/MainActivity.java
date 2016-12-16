package net.asovel.myebike.main;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    //PROba




    public static final String PAGINA_WEB = "PAGINA_WEB";

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION);

        iniUI();
    }

    private void iniUI()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_inicio);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        fab = (FloatingActionButton) findViewById(R.id.btn_buscar_ebikes);

        Bundle bundle = getIntent().getExtras();
       // String email = bundle.getString(LoginActivity.EMAIL);

        View navHeader = navigationView.getHeaderView(0);
        TextView textEmail = (TextView) navHeader.findViewById(R.id.text_email);
       // textEmail.setText(email);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        boolean fabVisibility = false;

        boolean fragmentTransaction = false;
        Fragment fragment = null;

        switch (item.getItemId())
        {
            case R.id.menu_inicio:

                break;
            case R.id.menu_top_ventas:
                Intent intent = new Intent(this, EBikeListActivity.class);
                Bundle bundle = new Bundle();
                ArrayList<String> listClauses = new ArrayList<>();
                listClauses.add("valoracion_SORT1 = 5");
                bundle.putStringArrayList(EBikeListActivity.WHERECLAUSE, listClauses);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.menu_myebike:
                fragment = new MyEbike();
                fragmentTransaction = true;
                fabVisibility = true;
                break;
            case R.id.menu_nosotros:
                fragment = new WebAsovel();
                Bundle bundle1 = new Bundle();
                bundle1.putString(PAGINA_WEB, "http://www.asovel.net/?page_id=484");
                fragment.setArguments(bundle1);
                fragmentTransaction = true;
                break;
            case R.id.menu_antes:
                fragment = new WebAsovel();
                Bundle bundle2 = new Bundle();
                bundle2.putString(PAGINA_WEB, "http://www.asovel.net/?page_id=475");
                fragment.setArguments(bundle2);
                fragmentTransaction = true;
                break;
            case R.id.menu_asovel:
                fragment = new WebAsovel();
                Bundle bundle3 = new Bundle();
                bundle3.putString(PAGINA_WEB, "http://www.asovel.net/");
                fragment.setArguments(bundle3);
                fragmentTransaction = true;
                break;
        }

        int id = item.getItemId();

        if (id == R.id.menu_registro)
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        if (fabVisibility)
            fab.setVisibility(View.VISIBLE);
        else
            fab.setVisibility(View.INVISIBLE);

        if (fragmentTransaction)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.principal, fragment).commit();
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}

