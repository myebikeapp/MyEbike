package net.asovel.myebike.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.asovel.myebike.R;
import net.asovel.myebike.resultadosebikes.EBikeListActivity;
import net.asovel.myebike.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class BuscarMarca extends Fragment {

    private RecyclerView recyclerView;
    private String[] marcas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buscar_marca, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        this.marcas = getResources().getStringArray(R.array.marcas);
        iniUI();
    }

    private void iniUI() {
        recyclerView = (RecyclerView) getView().findViewById(R.id.buscar_marca_recycleView);
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
        Intent intent = new Intent(getContext(), EBikeListActivity.class);
        Bundle bundle = new Bundle();
        int position = recyclerView.getChildAdapterPosition(view);
        ArrayList<String> marca = new ArrayList<>(1);
        marca.add("marca.nombre = '" + marcas[position] + "'");
        bundle.putStringArrayList(Constants.WHERECLAUSE, marca);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class AdaptadorMarcas extends RecyclerView.Adapter<MarcasViewHolder> implements View.OnClickListener{

        private View.OnClickListener listener;

        public AdaptadorMarcas() {
        }

        public void setListener(View.OnClickListener listener) {
            this.listener = listener;
        }

        @Override
        public int getItemCount() {
            return marcas.length;
        }

        @Override
        public MarcasViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listado_marcas, viewGroup, false);
            view.setOnClickListener(this);
            return new MarcasViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MarcasViewHolder holder, int position) {
            holder.bindMarca(marcas[position]);
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
