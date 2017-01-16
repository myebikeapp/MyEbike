package net.asovel.myebike.resultadosebikes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import net.asovel.myebike.R;
import net.asovel.myebike.backendless.common.DefaultCallback;
import net.asovel.myebike.backendless.common.Defaults;
import net.asovel.myebike.backendless.data.EBike;
import net.asovel.myebike.utils.ParcelableEBike;

import java.util.ArrayList;
import java.util.List;

public class EBikeListActivity extends AppCompatActivity
{
    public static final String WHERECLAUSE = "WHERECLAUSE";
    public static final String SORTBY = "SORTBY";

    public static final int PAGESIZE = 7;

    private RecyclerView recyclerView;
    private AdaptadorEbikes adaptador;

    private List<EBike> eBikes = new ArrayList<>();
    private BackendlessCollection<EBike> collection;
    private boolean[] isPageLoaded = new boolean[10];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebike_list);

        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION);

        iniUI();

        launchQuery();
    }

    private void iniUI()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ebikes);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
    }

    private void launchQuery()
    {
        Bundle bundle = getIntent().getExtras();

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();

        List<String> listClauses = bundle.getStringArrayList(WHERECLAUSE);

        if (listClauses != null)
        {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < listClauses.size(); i++)
            {
                builder.append(" and " + listClauses.get(i));
            }

            String whereClause = builder.substring(5);
            dataQuery.setWhereClause(whereClause);
            Log.w("hola", whereClause);
        }

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.addRelated("marca");

        queryOptions.setPageSize(PAGESIZE);

        List<String> sortBy = bundle.getStringArrayList(SORTBY);

        if (sortBy == null)
        {
            sortBy = new ArrayList<>();
            sortBy.add("patrocinado_SORT0 DESC");
            sortBy.add("valoracion_SORT1 DESC");
            sortBy.add("precio_SORT2");
        }
        queryOptions.setSortBy(sortBy);

        dataQuery.setQueryOptions(queryOptions);
        
        Backendless.Persistence.of(EBike.class).find(dataQuery, new DefaultCallback<BackendlessCollection<EBike>>(this)
        {
            @Override
            public void handleResponse(BackendlessCollection<EBike> response)
            {
                super.handleResponse(response);

                if (response.getCurrentPage().size() == 0)
                {
                    Toast.makeText(EBikeListActivity.this, "Hem de posar mes bicis al Backendless", Toast.LENGTH_LONG).show();
                    return;
                }
                collection = response;
                eBikes.addAll(collection.getCurrentPage());
                int totalPages = getTotalPages(collection.getTotalObjects());
                setUpRecyclerView(totalPages);
                isPageLoaded[0] = true;
            }
        });
    }

    private int getTotalPages(int totalObjects)
    {
        if (totalObjects % PAGESIZE == 0)
            return (totalObjects / PAGESIZE) - 1;

        return totalObjects / PAGESIZE;
    }

    private void setUpRecyclerView(int totalPages)
    {
        adaptador = new AdaptadorEbikes(this, totalPages);
        adaptador.setListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onItemClick(view);
            }
        });
        adaptador.setButtonsListener(new AdaptadorEbikes.OnAdaptadorButtonsListener()
        {
            @Override
            public void onButtonsClick()
            {
                setUpPage();
            }
        });
        adaptador.setEBikes(eBikes);
        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private void onItemClick(View view)
    {
        Intent intent = new Intent(EBikeListActivity.this, EBikeDetailActivity.class);
        Bundle bundle = new Bundle();
        int position = getlimInf() + recyclerView.getChildAdapterPosition(view);
        ParcelableEBike parcelableEBike = ParcelableEBike.fromEBike(eBikes.get(position));
        bundle.putParcelable(ParcelableEBike.PARCELABLEEBIKE, parcelableEBike);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private int getlimInf()
    {
        int limInf = 0;
        for (int i = 0; i < adaptador.getPage(); i++)
        {
            if (isPageLoaded[i])
                limInf += PAGESIZE;
        }
        return limInf;
    }

    private void setUpPage()
    {
        final int limInf = getlimInf();
        final int page = adaptador.getPage();

        if (isPageLoaded[page])
        {
            int numEBikes = eBikes.size();
            int aux2 = limInf + PAGESIZE;
            int limSup = (numEBikes < aux2) ? numEBikes : aux2;
            adaptador.setEBikes(eBikes.subList(limInf, limSup));
            recyclerView.scrollToPosition(0);
            return;
        }
        collection.getPage(PAGESIZE, page * PAGESIZE, new DefaultCallback<BackendlessCollection<EBike>>(this)
        {
            @Override
            public void handleResponse(BackendlessCollection<EBike> response)
            {
                super.handleResponse(response);

                eBikes.addAll(limInf, response.getCurrentPage());
                adaptador.setEBikes(response.getCurrentPage());
                recyclerView.scrollToPosition(0);

                isPageLoaded[page] = true;
            }
        });
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
