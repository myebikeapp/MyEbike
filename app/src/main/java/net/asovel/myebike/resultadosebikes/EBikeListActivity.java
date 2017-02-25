package net.asovel.myebike.resultadosebikes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.backendless.Backendless;
import com.google.android.gms.analytics.Tracker;

import net.asovel.myebike.R;
import net.asovel.myebike.backendless.common.Defaults;
import net.asovel.myebike.utils.AnalyticsApplication;
import net.asovel.myebike.utils.Constants;

import java.util.ArrayList;

public class EBikeListActivity extends AppCompatActivity
{
    public static final String TAG = EBikeListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebike_list);

        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION);

        iniUI();
        Fragment fragment = new FragmentListEBike();

        Bundle receiver = getIntent().getExtras();
        Bundle sender = new Bundle();

        ArrayList<String> listClauses = receiver.getStringArrayList(Constants.WHERECLAUSE);
        ArrayList<String> sortBy = receiver.getStringArrayList(Constants.SORTBY);

        sender.putString(Constants.CALLER, receiver.getString(Constants.CALLER));
        sender.putStringArrayList(Constants.WHERECLAUSE, listClauses);
        sender.putStringArrayList(Constants.SORTBY, sortBy);
        fragment.setArguments(sender);

        getSupportFragmentManager().beginTransaction().replace(R.id.principal, fragment, null).commit();
    }

    private void iniUI()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ebikes);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Si la activity no tiene la propiedad singleTop hay que crear el intent con el atributo FLAG_ACTIVITY_CLEAR_TOP
                // Intent intent = NavUtils.getParentActivityIntent(this);
                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // NavUtils.navigateUpTo(this, intent);
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
