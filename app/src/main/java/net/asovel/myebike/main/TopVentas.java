package net.asovel.myebike.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.backendless.BackendlessCollection;

import net.asovel.myebike.R;
import net.asovel.myebike.backendless.data.EBike;
import net.asovel.myebike.resultadosebikes.AdaptadorEbikes;
import net.asovel.myebike.resultadosebikes.EBikeDetailActivity;
import net.asovel.myebike.resultadosebikes.EBikesCallback;
import net.asovel.myebike.resultadosebikes.Query;
import net.asovel.myebike.utils.ParcelableEBike;

import java.util.ArrayList;
import java.util.List;

public class TopVentas extends Fragment implements EBikesCallback {

    public static final int PAGESIZE = 7;

    private RecyclerView recyclerView;
    private Query query;
    private List<EBike> eBikes = new ArrayList<>();
    private boolean[] isPageLoaded = new boolean[20];
    private AdaptadorEbikes adaptador;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_ventas, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        recyclerView = (RecyclerView) getView().findViewById(R.id.recycleView);

        ArrayList<String> listClauses = new ArrayList<>();
        listClauses.add("valoracion_SORT1 = 5");

        query = new Query(getContext(), this);
        query.launchQuery(null, null, 7);
    }

    @Override
    public void onQueryCallback(BackendlessCollection<EBike> collection) {

        if (!isPageLoaded[0]) {
            eBikes.addAll(collection.getCurrentPage());
            isPageLoaded[0] = true;
            setUpRecyclerView();

        } else {
            eBikes.addAll(getlimInf(), collection.getCurrentPage());
            adaptador.setEBikes(collection.getCurrentPage());
            recyclerView.scrollToPosition(0);
            isPageLoaded[adaptador.getPage()] = true;
        }
    }

    private void setUpRecyclerView() {
        int totalPages = query.getTotalPages(PAGESIZE);
        adaptador = new AdaptadorEbikes(getContext(), totalPages);
        adaptador.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(view);
            }
        });
        adaptador.setButtonsListener(new AdaptadorEbikes.OnAdaptadorButtonsListener() {
            @Override
            public void onButtonsClick() {
                setUpPage();
            }
        });
        adaptador.setEBikes(eBikes);
        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void onItemClick(View view) {
        Intent intent = new Intent(getContext(), EBikeDetailActivity.class);
        Bundle bundle = new Bundle();
        int position = getlimInf() + recyclerView.getChildAdapterPosition(view);
        ParcelableEBike parcelableEBike = ParcelableEBike.fromEBike(eBikes.get(position));
        bundle.putParcelable(ParcelableEBike.PARCELABLEEBIKE, parcelableEBike);
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }

    private void setUpPage() {
        int limInf = getlimInf();
        int page = adaptador.getPage();

        if (isPageLoaded[page]) {
            int numEBikes = eBikes.size();
            int aux2 = limInf + PAGESIZE;
            int limSup = (numEBikes < aux2) ? numEBikes : aux2;
            adaptador.setEBikes(eBikes.subList(limInf, limSup));
            recyclerView.scrollToPosition(0);

        } else {
            query.nextPage(page, PAGESIZE);
        }
    }

    private int getlimInf() {
        int limInf = 0;
        for (int i = 0; i < adaptador.getPage(); i++) {
            if (isPageLoaded[i])
                limInf += PAGESIZE;
        }
        return limInf;
    }
}
