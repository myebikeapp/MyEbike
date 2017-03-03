package net.asovel.myebike.resultadosebikes;

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
import net.asovel.myebike.main.FragmentListMarca;
import net.asovel.myebike.main.MainActivity;
import net.asovel.myebike.main.MyEBike;
import net.asovel.myebike.utils.Constants;
import net.asovel.myebike.utils.ParcelableEBike;

import java.util.ArrayList;
import java.util.List;

public class FragmentListEBike extends Fragment
{
    private static final String TAG = FragmentListEBike.class.getSimpleName();

    public static final int PAGESIZE = 7;

    private RecyclerView recyclerView;
    private AdaptadorEbikes adaptador;
    private List<EBike> eBikes = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_list_ebikes, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state)
    {
        super.onActivityCreated(state);

        recyclerView = (RecyclerView) getView().findViewById(R.id.recycleView);

        launchQuery();
    }

    private void launchQuery()
    {
        Bundle bundle = getArguments();

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();

        List<String> listClauses = bundle.getStringArrayList(Constants.WHERECLAUSE);

        if (listClauses != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < listClauses.size(); i++) {
                builder.append(" and " + listClauses.get(i));
            }

            String whereClause = builder.substring(5);
            dataQuery.setWhereClause(whereClause);
            Log.d(TAG, whereClause);
        }

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.addRelated("marca");

        queryOptions.setPageSize(100);

        List<String> sortBy = bundle.getStringArrayList(Constants.SORTBY);

        if (sortBy == null) {
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

                if (response.getCurrentPage().size() == 0) {
                    finishActivity();
                    return;
                }
                eBikes.addAll(response.getCurrentPage());
                int numEbikes = eBikes.size();
                int totalPages = getTotalPages(numEbikes);

                String caller = getArguments().getString(Constants.CALLER, "");
                if (caller.equals(FragmentListMarca.TAG) || caller.equals(MyEBike.TAG)) {
                    String label = getResources().getString(R.string.EBikeListActivity_label) + " (" + numEbikes + ")";
                    getActivity().setTitle(label);
                }

                setUpRecyclerView(totalPages);
            }
        });
    }

    private void finishActivity()
    {
        if (getArguments().getString(Constants.CALLER, "").equals(FragmentListMarca.TAG))
            Toast.makeText(getContext(), "Lo sentimos pero no tenemos modelos registrados de esta marca", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getContext(), "Lo sentimos pero no tenemos modelos registrados para esta selección", Toast.LENGTH_LONG).show();

        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                try {
                    Thread.sleep(3500);
                    getActivity().finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
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
            public void onButtonsClick()
            {
                setUpPage();
            }
        });
        int numEbikes = eBikes.size();
        int min = numEbikes < PAGESIZE ? numEbikes : PAGESIZE;
        adaptador.setEBikes(eBikes.subList(0, min));
        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void onItemClick(View view)
    {
        Intent intent = new Intent(getContext(), EBikeDetailActivity.class);
        Bundle bundle = new Bundle();

        int position = adaptador.getPage() * PAGESIZE + recyclerView.getChildAdapterPosition(view);
        ParcelableEBike parcelableEBike = ParcelableEBike.fromEBike(eBikes.get(position));
        bundle.putParcelable(ParcelableEBike.PARCELABLEEBIKE, parcelableEBike);

        String caller = getArguments().getString(Constants.CALLER, "");
        if (caller.equals(MainActivity.TAG))
            bundle.putString(Constants.CALLER, caller);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void setUpPage()
    {
        int page = adaptador.getPage();
        int limInf = page * PAGESIZE;
        int numEBikes = eBikes.size();
        int aux = limInf + PAGESIZE;
        int limSup = (numEBikes < aux) ? numEBikes : aux;
        adaptador.setEBikes(eBikes.subList(limInf, limSup));
        recyclerView.scrollToPosition(0);
    }
}
