package net.asovel.myebike.resultadosebikes;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.squareup.picasso.Target;

import net.asovel.myebike.R;
import net.asovel.myebike.utils.Constants;
import net.asovel.myebike.utils.ParcelableEBike;
import net.asovel.myebike.utils.ParcelableMarca;

public class EBikeDetailActivity extends AppCompatActivity {

    private ParcelableEBike parcelableEBike;
    private String caller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebike_detail);

        parcelableEBike = getIntent().getParcelableExtra(ParcelableEBike.PARCELABLEEBIKE);
        caller = getIntent().getExtras().getString(Constants.CALLER, "");
        initUI();
    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ImageView imagen = (ImageView) findViewById(R.id.toolbar_detail_image);
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

        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {

                    /*int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    Matrix matrix = new Matrix();
                    matrix.postScale(0.7f, 0.9f);
                    Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);*/
                    imagen.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable drawable) {

            }

            @Override
            public void onPrepareLoad(Drawable drawable) {

            }
        };

        if (parcelableEBike.getFoto() != null) {
            Picasso.with(getBaseContext())
                    .load(parcelableEBike.getFoto())
                    .placeholder(R.drawable.ebike)
                    .resize(AdaptadorEbikes.BicicletasViewHolder.IMAGE_WIDTH, AdaptadorEbikes.BicicletasViewHolder.IMAGE_HEIGHT)
                    .centerInside()
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

        descripcion.setText(parcelableEBike.getDescripcion());

        FloatingActionButton buscarTiendas = (FloatingActionButton) findViewById(R.id.floating_buscar_tiendas);
        buscarTiendas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localizarTiendas();
            }
        });
    }

    private void localizarTiendas() {
        ParcelableMarca marca = parcelableEBike.getMarca();
        if (marca == null)
            return;
        Intent intent = new Intent(EBikeDetailActivity.this, MapsActivity.class);
        Bundle bundle = new Bundle();
        String nombreMarca = marca.getNombre();
        bundle.putString(Constants.NOMBRE_MARCA, nombreMarca);

        if (!caller.equals(""))
            bundle.putString(Constants.CALLER, caller);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}

/*
<android.support.v7.widget.CardView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentRight="true"
        android:layout_marginRight="16dp"
        android:layout_marginTop="8dp"
        card_view:cardCornerRadius="6dp"
        card_view:cardElevation="6dp">

<Button
android:id="@+id/btn_buscar_tiendas"
        android:layout_width="64dp"
        android:layout_height="64dp"
        android:layout_marginBottom="4dp"
        android:layout_marginLeft="4dp"
        android:layout_marginRight="4dp"
        android:background="@drawable/maps_icon" />

</android.support.v7.widget.CardView>*/