package net.asovel.myebike.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import net.asovel.myebike.LoginActivity;
import net.asovel.myebike.R;
import net.asovel.myebike.backendless.common.DefaultCallback;
import net.asovel.myebike.backendless.data.Marca;
import net.asovel.myebike.resultadosebikes.EBikeListActivity;
import net.asovel.myebike.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class BuscarMarca extends Fragment {

    public static final String TAG = BuscarMarca.class.getSimpleName();

    private RecyclerView recyclerView;
    private String[] nombresMarca;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buscar_marca, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        recyclerView = (RecyclerView) getView().findViewById(R.id.buscar_marca_recycleView);

        launchQuery();
    }

    private void launchQuery() {
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();

        QueryOptions queryOptions = new QueryOptions();

        List<String> sortBy = new ArrayList<>(1);
        sortBy.add("nombre");
        queryOptions.setSortBy(sortBy);

        queryOptions.setPageSize(100);
        dataQuery.setQueryOptions(queryOptions);

        Backendless.Persistence.of(Marca.class).find(dataQuery, new DefaultCallback<BackendlessCollection<Marca>>(getContext()) {
            @Override
            public void handleResponse(BackendlessCollection<Marca> response) {
                super.handleResponse(response);

                List<Marca> marcas = response.getCurrentPage();
                int size = marcas.size();
                nombresMarca = new String[size];

                for (int i = 0; i < size; i++)
                    nombresMarca[i] = marcas.get(i).getNombre();

                setUpRecyclerView();
            }
        });
    }

    private void setUpRecyclerView() {
        AdaptadorMarcas adaptador = new AdaptadorMarcas();
        adaptador.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(view);
            }
        });
        recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
    }

    private void onItemClick(View view) {
        SharedPreferences prefs = getActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        String email = prefs.getString("email", "");

        Intent intent;
        Bundle bundle = new Bundle();

        ArrayList<String> listClauses = new ArrayList<>(1);
        int position = recyclerView.getChildAdapterPosition(view);
        listClauses.add("marca.nombre = '" + nombresMarca[position] + "'");
        bundle.putStringArrayList(Constants.WHERECLAUSE, listClauses);

        if (email.equals("")) {
            intent = new Intent(getContext(), LoginActivity.class);
            bundle.putString(Constants.CALLER, TAG);
        } else {
            intent = new Intent(getContext(), EBikeListActivity.class);
        }

        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class AdaptadorMarcas extends RecyclerView.Adapter<MarcasViewHolder> implements View.OnClickListener {

        private View.OnClickListener listener;

        public AdaptadorMarcas() {
        }

        public void setListener(View.OnClickListener listener) {
            this.listener = listener;
        }

        @Override
        public int getItemCount() {
            return nombresMarca.length;
        }

        @Override
        public MarcasViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listado_marcas, viewGroup, false);
            view.setOnClickListener(this);
            return new MarcasViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MarcasViewHolder holder, int position) {
            holder.bindMarca(nombresMarca[position]);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v);
        }
    }

    public class MarcasViewHolder extends RecyclerView.ViewHolder {

        private TextView marcaView;

        public MarcasViewHolder(View itemView) {
            super(itemView);
            marcaView = (TextView) itemView.findViewById(R.id.list_marca);
        }

        public void bindMarca(String marca) {
            marcaView.setText(marca);
        }
    }


}
