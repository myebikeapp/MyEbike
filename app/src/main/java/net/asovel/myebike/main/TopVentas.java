package net.asovel.myebike.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import net.asovel.myebike.R;
import net.asovel.myebike.backendless.common.DefaultCallback;
import net.asovel.myebike.backendless.data.EBike;
import net.asovel.myebike.resultadosebikes.AdaptadorEbikes;
import net.asovel.myebike.resultadosebikes.DividerItemDecoration;
import net.asovel.myebike.resultadosebikes.EBikeDetailActivity;
import net.asovel.myebike.utils.ParcelableEBike;

import java.util.ArrayList;
import java.util.List;

public class TopVentas extends Fragment {

    public static final String WHERECLAUSE = "WHERECLAUSE";
    public static final String SORTBY = "SORTBY";

    public static final int PAGESIZE = 7;

    private RecyclerView recyclerView;
    private AdaptadorEbikes adaptador;

    private List<EBike> eBikes = new ArrayList<>();
    private BackendlessCollection<EBike> collection;
    private boolean[] isPageLoaded = new boolean[10];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_ventas, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        recyclerView = (RecyclerView) getView().findViewById(R.id.recycleView);

        launchQuery();
    }

    private void launchQuery()
    {
        Bundle bundle = getArguments();

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

        Backendless.Persistence.of(EBike.class).find(dataQuery, new DefaultCallback<BackendlessCollection<EBike>>(getContext())
        {
            @Override
            public void handleResponse(BackendlessCollection<EBike> response)
            {
                super.handleResponse(response);

                if (response.getCurrentPage().size() == 0)
                {
                    Toast.makeText(getContext(), "Hem de posar mes bicis al Backendless", Toast.LENGTH_LONG).show();
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
        adaptador = new AdaptadorEbikes(getContext(), totalPages);
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
            public void onButtonsClick(int page)
            {
                setUpPage(page);
            }
        });
        adaptador.setEBikes(eBikes);
        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
    }

    private void onItemClick(View view)
    {
        Intent intent = new Intent(getContext(), EBikeDetailActivity.class);
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

    private void setUpPage(final int page)
    {
        final int limInf = getlimInf();

        if (isPageLoaded[page])
        {
            int numEBikes = eBikes.size();
            int aux2 = limInf + PAGESIZE;
            int limSup = (numEBikes < aux2) ? numEBikes : aux2;
            adaptador.setEBikes(eBikes.subList(limInf, limSup));
            recyclerView.scrollToPosition(0);
            return;
        }
        collection.getPage(PAGESIZE, page * PAGESIZE, new DefaultCallback<BackendlessCollection<EBike>>(getContext())
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



}
