package net.asovel.myebike.resultadosebikes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

import net.asovel.myebike.R;
import net.asovel.myebike.main.MainActivity;
import net.asovel.myebike.utils.AnalyticsApplication;
import net.asovel.myebike.utils.Constants;
import net.asovel.myebike.utils.ParcelableEBike;
import net.asovel.myebike.utils.ParcelableMarca;

import java.util.HashMap;
import java.util.Map;

public class EBikeDetailActivity extends AppCompatActivity
{
    private static final String TAG = EBikeDetailActivity.class.getSimpleName();

    private Tracker tracker;

    private ParcelableEBike parcelableEBike;
    private String caller;

    private EditText nameText;
    private TextInputLayout nameLayout;
    private EditText emailText;
    private TextInputLayout emailLayout;
    private EditText phoneText;
    private TextInputLayout phoneLayout;
    private EditText postalText;
    private TextInputLayout postalLayout;
    private EditText messageText;
    private TextInputLayout messageLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebike_detail);

        Backendless.setUrl(getString(R.string.SERVER_URL));
        Backendless.initApp(this, getString(R.string.APPLICATION_ID), getString(R.string.SECRET_KEY), getString(R.string.VERSION));

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();

        parcelableEBike = getIntent().getParcelableExtra(ParcelableEBike.PARCELABLEEBIKE);
        caller = getIntent().getExtras().getString(Constants.CALLER, "");
        initUI();
        initUserForm();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        tracker.setScreenName(TAG);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
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
        TextView temporada = (TextView) findViewById(R.id.detail_temporada);
        RatingBar valoracion = (RatingBar) findViewById(R.id.detail_valoracion);
        TextView link = (TextView) findViewById(R.id.detail_link);
        TextView descripcion = (TextView) findViewById(R.id.detail_descripcion);

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

        if (parcelableEBike.getValoracion_SORT1() != -1)
            valoracion.setRating(parcelableEBike.getValoracion_SORT1());

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

        if (parcelableEBike.getAny() != -1)
            temporada.setText("" + parcelableEBike.getAny());

        String url = parcelableEBike.getLink();
        if (url != null) {
            link.setVisibility(View.VISIBLE);
            link.setMovementMethod(LinkMovementMethod.getInstance());
            link.setText(Html.fromHtml("<a href=\"" + url + Constants.UTM + "\">" + url + "</a>"));
        }

        String descripcionText = parcelableEBike.getDescripcion();
        if (descripcionText != null) {
            descripcion.setVisibility(View.VISIBLE);
            descripcion.setText(descripcionText);
        }

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

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(Constants.CATEGORY_EBIKE)
                .setAction("Buscar tiendas")
                .build());

        Intent intent = new Intent(EBikeDetailActivity.this, MapaActivity.class);
        Bundle bundle = new Bundle();
        String nombreMarca = marca.getNombre();
        bundle.putString(Constants.NOMBRE_MARCA, nombreMarca);

        if (caller.equals(MainActivity.TAG))
            bundle.putString(Constants.CALLER, caller);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initUserForm()
    {
        nameText = (EditText) findViewById(R.id.user_name);
        nameLayout = (TextInputLayout) findViewById(R.id.tilayout_name);
        emailText = (EditText) findViewById(R.id.user_email);
        emailLayout = (TextInputLayout) findViewById(R.id.tilayout_email);
        phoneText = (EditText) findViewById(R.id.user_phone);
        phoneLayout = (TextInputLayout) findViewById(R.id.tilayout_phone);
        postalText = (EditText) findViewById(R.id.user_postal);
        postalLayout = (TextInputLayout) findViewById(R.id.tilayout_postal);
        messageText = (EditText) findViewById(R.id.user_message);
        Button contactarButton = (Button) findViewById(R.id.button_contactar);

        SharedPreferences prefs = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        String email = prefs.getString("email", "");
        emailText.setText(email);

        nameText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus) {
                    checkNombre(nameText.getText().toString());
                }
            }
        });

        emailText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus) {
                    checkEmail(emailText.getText().toString());
                }
            }
        });

        phoneText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus) {
                    checkPhone(phoneText.getText().toString());
                }
            }
        });

        contactarButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onContactarClicked();
            }
        });
    }

    private void onContactarClicked()
    {

        final String nombre = nameText.getText().toString();
        if (!checkNombre(nombre)) {
            Toast.makeText(EBikeDetailActivity.this, "Nombre incorrecto", Toast.LENGTH_LONG).show();
            return;
        }

        final String email = emailText.getText().toString();
        if (!checkEmail(email)){
            Toast.makeText(EBikeDetailActivity.this, "Email incorrecto", Toast.LENGTH_LONG).show();
            return;
        }

        final String telefono = phoneText.getText().toString();
        if (!checkPhone(telefono)){
            Toast.makeText(EBikeDetailActivity.this, "Telefono incorrecto", Toast.LENGTH_LONG).show();
            return;
        }
        final String postal = postalText.getText().toString();
        final String mensaje = messageText.getText().toString();
        final String referencia = parcelableEBike.getMarca().getNombre() + " " + parcelableEBike.getModelo();

        HashMap lead = new HashMap();
        lead.put("referencia", referencia);
        lead.put("nombre", nombre);
        lead.put("email", email);
        lead.put("telefono", telefono);
        lead.put("codigo_postal", postal);
        lead.put("mensaje", mensaje);

        Backendless.Persistence.of("lead").save(lead, new AsyncCallback<Map>()
        {
            public void handleResponse(Map response)
            {
                Toast.makeText(EBikeDetailActivity.this, "Menssaje enviado", Toast.LENGTH_LONG).show();
                cleanForm();

                String messageBody = "Referencia: " + referencia + '\n'
                        + "Nombre: " + nombre + '\n'
                        + "Email: " + email + '\n'
                        + "Telefono: " + telefono + '\n'
                        + "Postal: " + postal + '\n'
                        + "Mensaje: " + mensaje + '\n';

                Backendless.Messaging.sendTextEmail("myebike: " + referencia, messageBody, "myebikeapp@gmail.com", new AsyncCallback<Void>()
                {
                    @Override
                    public void handleResponse(Void aVoid)
                    {
                    }

                    @Override
                    public void handleFault(BackendlessFault backendlessFault)
                    {
                    }
                });
            }

            public void handleFault(BackendlessFault fault)
            {
                Log.d(TAG, fault.getMessage());
                Toast.makeText(EBikeDetailActivity.this, fault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean checkNombre(String nombre){

        if (nombre.equals("")){
            nameLayout.setError("Nombre incorrecto");
            return false;
        }
        nameLayout.setError(null);
        return true;
    }

    private boolean checkEmail(String email){
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError(null);
            return true;
        }
        emailLayout.setError("Email incorrecto");
        return false;
    }

    private boolean checkPhone(String phone){
        if (phone.equals("") || android.util.Patterns.PHONE.matcher(phone).matches()) {
            phoneLayout.setError(null);
            return true;
        }
        phoneLayout.setError("Telefono incorrecto");
        return false;
    }

    private void cleanForm()
    {
        nameText.setText(null);
        emailText.setText(null);
        phoneText.setText(null);
        postalText.setText(null);
        messageText.setText(null);
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