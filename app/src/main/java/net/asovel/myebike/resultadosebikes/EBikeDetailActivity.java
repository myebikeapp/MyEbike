package net.asovel.myebike.resultadosebikes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.asovel.myebike.R;
import net.asovel.myebike.main.MainActivity;
import net.asovel.myebike.utils.Constants;
import net.asovel.myebike.utils.ParcelableEBike;
import net.asovel.myebike.utils.ParcelableMarca;

public class EBikeDetailActivity extends AppCompatActivity
{
    private static final String TAG = EBikeDetailActivity.class.getSimpleName();

    private ParcelableEBike parcelableEBike;
    private String caller;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebike_detail);

        parcelableEBike = getIntent().getParcelableExtra(ParcelableEBike.PARCELABLEEBIKE);
        caller = getIntent().getExtras().getString(Constants.CALLER, "");
        initUI();
    }

    private void initUI()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ImageView imagen = (ImageView) findViewById(R.id.toolbar_detail_image);
        TextView marcaModelo = (TextView) findViewById(R.id.detail_marca_modelo);
        TextView precio = (TextView) findViewById(R.id.detail_precio);
        TextView uso = (TextView) findViewById(R.id.detail_uso);
        TextView autonomia = (TextView) findViewById(R.id.detail_autonomia);
        TextView peso = (TextView) findViewById(R.id.detail_peso);
        TextView suspension = (TextView) findViewById(R.id.detail_suspension);
        TextView tamanoRueda = (TextView) findViewById(R.id.detail_tamano_rueda);
        TextView ubicacionMotor = (TextView) findViewById(R.id.detail_ubicacion_motor);
        RatingBar valoracion = (RatingBar) findViewById(R.id.detail_valoracion);
        TextView descripcion = (TextView) findViewById(R.id.detail_descripcion);

        if (parcelableEBike.getFoto() != null) {
            Picasso.with(getBaseContext())
                    .load(parcelableEBike.getFoto())
                    .placeholder(R.drawable.ebike)
                    .resize(AdaptadorEbikes.BicicletasViewHolder.IMAGE_WIDTH, AdaptadorEbikes.BicicletasViewHolder.IMAGE_HEIGHT)
                    .centerCrop()
                    .into(imagen);
        } else {
            imagen.setImageResource(R.drawable.ebike);
        }

        if (parcelableEBike.getMarca() != null)
            marcaModelo.setText(parcelableEBike.getMarca().getNombre() + " " + parcelableEBike.getModelo());
        else
            marcaModelo.setText(parcelableEBike.getModelo());

        if (parcelableEBike.getPrecio_SORT2() != -1)
            precio.setText("" + parcelableEBike.getPrecio_SORT2() + " €");

        uso.setText(parcelableEBike.getUso());

        if (parcelableEBike.getAutonomia() != -1)
            autonomia.setText("" + parcelableEBike.getAutonomia() + " Km");

        if (parcelableEBike.getPeso() != -1)
            peso.setText("" + parcelableEBike.getPeso() + " Kg");

        suspension.setText(parcelableEBike.getSuspension());

        int tamano = parcelableEBike.getTamano_ruedas();
        if (tamano != -1) {

            if (tamano != 27)
                tamanoRueda.setText("" + tamano + "\"");
            else
                tamanoRueda.setText("27.5\"");
        }

        ubicacionMotor.setText(parcelableEBike.getUbicacion_motor());

        if (parcelableEBike.getValoracion_SORT1() != -1)
            valoracion.setRating(parcelableEBike.getValoracion_SORT1());

        descripcion.setText(parcelableEBike.getLink());

        FloatingActionButton buscarTiendas = (FloatingActionButton) findViewById(R.id.floating_buscar_tiendas);
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
        if (marca == null || marca.getNombre() == null)
            return;
        Intent intent = new Intent(EBikeDetailActivity.this, MapaActivity.class);
        Bundle bundle = new Bundle();
        String nombreMarca = marca.getNombre();
        bundle.putString(Constants.NOMBRE_MARCA, nombreMarca);

        if (caller.equals(MainActivity.TAG))
            bundle.putString(Constants.CALLER, caller);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!caller.equals("")) {
                    //   Intent intent = new Intent(this, MainActivity.class);
                    //   NavUtils.navigateUpTo(this, intent);
                    finish();
                } else
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