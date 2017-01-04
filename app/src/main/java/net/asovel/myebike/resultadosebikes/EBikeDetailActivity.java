package net.asovel.myebike.resultadosebikes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.asovel.myebike.R;
import net.asovel.myebike.utils.ParcelableEBike;
import net.asovel.myebike.utils.ParcelableMarca;

public class EBikeDetailActivity extends AppCompatActivity
{
    private ParcelableEBike parcelableEBike;

    private ImageView imagen;
    private TextView marca;
    private TextView modelo;
    private TextView precio;
    private RatingBar valoracion;

    private TextView uso;
    private TextView autonomia;
    private TextView peso;
    private TextView suspension;
    private TextView tamanoRueda;
    private TextView ubicacionMotor;
    private TextView descripcion;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebike_detail);

        parcelableEBike = getIntent().getParcelableExtra(ParcelableEBike.PARCELABLEEBIKE);
        initUI();
    }

    private void initUI()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imagen = (ImageView) findViewById(R.id.toolbar_detail_image);
        modelo = (TextView) findViewById(R.id.detail_modelo);
        precio = (TextView) findViewById(R.id.detail_precio);
        uso = (TextView) findViewById(R.id.detail_uso);
        autonomia = (TextView) findViewById(R.id.detail_autonomia);
        peso = (TextView) findViewById(R.id.detail_peso);
        suspension = (TextView) findViewById(R.id.detail_suspension);
        tamanoRueda = (TextView) findViewById(R.id.detail_tamano_rueda);
        ubicacionMotor = (TextView) findViewById(R.id.detail_ubicacion_motor);
        valoracion = (RatingBar) findViewById(R.id.detail_valoracion);
        descripcion = (TextView) findViewById(R.id.detail_descripcion);

        if (parcelableEBike.getFoto() != null)
        {
            Picasso.with(getBaseContext())
                    .load(parcelableEBike.getFoto())
                    .placeholder(R.drawable.ebike)
                    .resize(350, 256).centerCrop()
                    .into(imagen);
        }

        modelo.setText(parcelableEBike.getModelo());
        if (parcelableEBike.getPrecio_SORT2() != -1)
            precio.setText("" + parcelableEBike.getPrecio_SORT2() + " €");

        uso.setText(parcelableEBike.getUso());
        if (parcelableEBike.getAutonomia() != -1)
            autonomia.setText("" + parcelableEBike.getAutonomia() + " Km");

        if (parcelableEBike.getPeso() != -1)
            peso.setText("" + parcelableEBike.getPeso() + " Kg");

        suspension.setText(parcelableEBike.getSuspension());
        if (parcelableEBike.getTamano_ruedas() != -1)
            tamanoRueda.setText("" + parcelableEBike.getTamano_ruedas() + "\"");

        ubicacionMotor.setText(parcelableEBike.getUbicacion_motor());

        if (parcelableEBike.getValoracion_SORT1() != -1)
            valoracion.setRating(parcelableEBike.getValoracion_SORT1());

        descripcion.setText(parcelableEBike.getDescripcion());

        FloatingActionButton buscarTiendas = (FloatingActionButton) findViewById(R.id.btn_buscar_tiendas);
        buscarTiendas.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                localizarTiendas();
            }
        });
    }

    private void localizarTiendas()
    {
        ParcelableMarca marca = parcelableEBike.getMarca();
        if (marca == null || marca.getNombre().equals("sin datos"))
            return;
        Intent intent = new Intent(EBikeDetailActivity.this, MapsActivity.class);
        Bundle bundle = new Bundle();
        String nombreMarca = marca.getNombre();
        bundle.putString(MapsActivity.NOMBRE_MARCA, nombreMarca);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
    }
}