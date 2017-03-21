package net.asovel.myebike.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import net.asovel.myebike.LoginActivity;
import net.asovel.myebike.R;
import net.asovel.myebike.backendless.common.DefaultCallback;
import net.asovel.myebike.backendless.data.Marca;
import net.asovel.myebike.backendless.data.MarcaLista;
import net.asovel.myebike.resultadosebikes.EBikeListActivity;
import net.asovel.myebike.utils.AnalyticsApplication;
import net.asovel.myebike.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FragmentListMarca extends Fragment
{
    public static final String TAG = FragmentListMarca.class.getSimpleName();

    private Tracker tracker;

    private RecyclerView recyclerView;
    private List<Marca> marcas;
    private int numTitle;
    private int numPeninsula;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_buscar_marca, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state)
    {
        super.onActivityCreated(state);

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        tracker = application.getDefaultTracker();

        recyclerView = (RecyclerView) getView().findViewById(R.id.buscar_marca_recycleView);
        marcas = new ArrayList<Marca>();
        queryMarcas();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        tracker.setScreenName(TAG);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    private void queryMarcas()
    {
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        QueryOptions queryOptions = new QueryOptions();
        queryOptions.addRelated("lista");
        dataQuery.setQueryOptions(queryOptions);

        Backendless.Data.of(MarcaLista.class).find(dataQuery, new DefaultCallback<BackendlessCollection<MarcaLista>>(getContext())
        {
            @Override
            public void handleResponse(BackendlessCollection<MarcaLista> response)
            {
                super.handleResponse(response);

                MarcaLista marcaLista = response.getCurrentPage().get(0);
                List<Marca> listaAux = marcaLista.getLista();

                Collections.sort(listaAux, new Comparator<Marca>()
                {
                    @Override
                    public int compare(final Marca object1, final Marca object2)
                    {
                        return object1.getNombre().compareToIgnoreCase(object2.getNombre());
                    }
                });


                int size = listaAux.size();
                for (int i = 0; i < size; ++i) {
                    Marca marca = listaAux.get(i);
                    if ((marca.getPeninsula() != null) && marca.getPeninsula())
                        marcas.add(marca);
                }
                numPeninsula = marcas.size();
                numTitle = marcas.size() + 1;

                for (int i = 0; i < size; ++i) {
                    Marca marca = listaAux.get(i);
                    if ((marca.getPeninsula() != null) && !marca.getPeninsula())
                        marcas.add(marca);
                }
                setUpRecyclerView();
            }
        });
    }

    private void setUpRecyclerView()
    {
        AdaptadorMarcas adaptador = new AdaptadorMarcas();
        adaptador.setListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onItemClick(view);
            }
        });
        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void onItemClick(View view)
    {
        int position = recyclerView.getChildAdapterPosition(view);

        Intent intent;
        Bundle bundle = new Bundle();

        if (position > numTitle) {

            String url = marcas.get(position - 2).getPagina_web() + Constants.UTM;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return;
        }
        String nombreMarca = marcas.get(position - 1).getNombre();

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(Constants.CATEGORY_MARCA)
                .setAction("Buscar marca")
                .setLabel(nombreMarca)
                .build());

        SharedPreferences prefs = getActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        String email = prefs.getString("email", "");

        ArrayList<String> listClauses = new ArrayList<>(1);
        listClauses.add("marca.nombre = '" + nombreMarca + "'");
        bundle.putStringArrayList(Constants.WHERECLAUSE, listClauses);
        bundle.putString(Constants.CALLER, TAG);

        if (email.equals("")) {
            intent = new Intent(getContext(), LoginActivity.class);
        } else {
            intent = new Intent(getContext(), EBikeListActivity.class);
        }

        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class AdaptadorMarcas extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener
    {
        private static final int VIEW_TYPE_ITEM = 0;
        private static final int VIEW_TYPE_TITLE = 1;

        private View.OnClickListener listener;

        public AdaptadorMarcas()
        {
        }

        public void setListener(View.OnClickListener listener)
        {
            this.listener = listener;
        }

        @Override
        public int getItemCount()
        {
            return marcas.size() + 2;
        }

        @Override
        public int getItemViewType(int position)
        {
            if (position == 0)
                return VIEW_TYPE_TITLE;

            return (position != numTitle) ? VIEW_TYPE_ITEM : VIEW_TYPE_TITLE;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType)
        {
            if (viewType == VIEW_TYPE_ITEM) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listado_marcas, viewGroup, false);
                view.setOnClickListener(this);
                return new MarcasViewHolder(view);
            }
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listado_marcas_title, viewGroup, false);
            return new MarcasTitleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
        {
            if (position != 0 && position != numTitle)

                if (position < numTitle)
                    ((MarcasViewHolder) holder).bindMarca(position - 1);

                else
                    ((MarcasViewHolder) holder).bindMarca(position - 2);

            else
                ((MarcasTitleViewHolder) holder).bindTitle(position);
        }

        @Override
        public void onClick(View v)
        {
            listener.onClick(v);
        }
    }

    public class MarcasViewHolder extends RecyclerView.ViewHolder
    {
        private TextView marcaView;
        private TextView paisView;

        public MarcasViewHolder(View itemView)
        {
            super(itemView);
            marcaView = (TextView) itemView.findViewById(R.id.list_marca);
            paisView = (TextView) itemView.findViewById(R.id.list_marca_pais);
        }

        public void bindMarca(int position)
        {
            Marca marca = marcas.get(position);
            marcaView.setText(marca.getNombre());
            paisView.setText(marca.getPais());
        }
    }

    public class MarcasTitleViewHolder extends RecyclerView.ViewHolder
    {
        private TextView titleView;

        public MarcasTitleViewHolder(View itemView)
        {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.list_marca);
        }

        public void bindTitle(int position)
        {
            if (position == 0)
                titleView.setText("Marcas distribuidas en España (" + numPeninsula + " )");
            else
                titleView.setText("Marcas sin distribución en España (" + (marcas.size() - numPeninsula) + " )");
        }
    }


}
