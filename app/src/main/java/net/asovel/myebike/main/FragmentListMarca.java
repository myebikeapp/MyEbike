package net.asovel.myebike.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;

import net.asovel.myebike.LoginActivity;
import net.asovel.myebike.R;
import net.asovel.myebike.backendless.data.Marca;
import net.asovel.myebike.resultadosebikes.EBikeListActivity;
import net.asovel.myebike.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class FragmentListMarca extends Fragment
{

    public static final String TAG = FragmentListMarca.class.getSimpleName();

    private RecyclerView recyclerView;
    private int numPeninsula;
    private String[] nombresMarca;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_buscar_marca, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state)
    {
        super.onActivityCreated(state);

        recyclerView = (RecyclerView) getView().findViewById(R.id.buscar_marca_recycleView);

        QueryMarcasTask task = new QueryMarcasTask();
        task.execute();
    }

    private class QueryMarcasTask extends AsyncTask<Void, Void, Integer>
    {
        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected Integer doInBackground(Void... values)
        {
            BackendlessDataQuery dataQuery = new BackendlessDataQuery();
            QueryOptions queryOptions = new QueryOptions();

            List<String> sortBy = new ArrayList<>(1);
            sortBy.add("nombre");
            queryOptions.setSortBy(sortBy);
            queryOptions.setPageSize(100);
            dataQuery.setQueryOptions(queryOptions);

            String whereClause = "peninsula = True";
            dataQuery.setWhereClause(whereClause);

            BackendlessCollection<Marca> response1 = Backendless.Data.of(Marca.class).find(dataQuery);
            List<Marca> marcas1 = response1.getCurrentPage();

            whereClause = "peninsula = False";
            dataQuery.setWhereClause(whereClause);

            BackendlessCollection<Marca> response2 = Backendless.Data.of(Marca.class).find(dataQuery);
            List<Marca> marcas2 = response2.getCurrentPage();

            numPeninsula = marcas1.size();
            int numForeign = marcas2.size();
            nombresMarca = new String[numPeninsula + numForeign];

            for (int i = 0; i < numPeninsula; i++)
                nombresMarca[i] = marcas1.get(i).getNombre();

            for (int i = 0; i < numForeign; i++)
                nombresMarca[i + numPeninsula] = marcas2.get(i).getNombre();

            return 0;
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {
        }

        @Override
        protected void onPostExecute(Integer result)
        {
            setUpRecyclerView();
        }

        @Override
        protected void onCancelled()
        {
        }
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
        int position = recyclerView.getChildAdapterPosition(view) - 1;

        if (position > numPeninsula) {
            Toast.makeText(getContext(), "Esta marca no esta disponible en España", Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences prefs = getActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        String email = prefs.getString("email", "");

        Intent intent;
        Bundle bundle = new Bundle();

        ArrayList<String> listClauses = new ArrayList<>(1);
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
            return nombresMarca.length + 2;
        }

        @Override
        public int getItemViewType(int position)
        {
            if (position == 0)
                return VIEW_TYPE_TITLE;

            return (position != numPeninsula) ? VIEW_TYPE_ITEM : VIEW_TYPE_TITLE;
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
            if (position != 0 && position != numPeninsula)

                if (position < numPeninsula)
                    ((MarcasViewHolder) holder).bindMarca(nombresMarca[position - 1]);
                else
                    ((MarcasViewHolder) holder).bindMarca(nombresMarca[position - 2]);
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

        public MarcasViewHolder(View itemView)
        {
            super(itemView);
            marcaView = (TextView) itemView.findViewById(R.id.list_marca);
        }

        public void bindMarca(String marca)
        {
            marcaView.setText(marca);
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
                titleView.setText("Marcas que se venden actualment en España");
            else
                titleView.setText("Marcas que no se venden actualment en España");
        }
    }


}
