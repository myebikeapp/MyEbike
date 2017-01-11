package net.asovel.myebike.resultadosebikes;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.asovel.myebike.R;
import net.asovel.myebike.backendless.common.DefaultCallback;
import net.asovel.myebike.backendless.common.Defaults;
import net.asovel.myebike.backendless.data.Marca;
import net.asovel.myebike.backendless.data.Tienda;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String NOMBRE_MARCA = "NOMBRE_MARCA";

    private GoogleMap map;
    private List<Tienda> tiendas;
    private Object object = new Object();
    private volatile boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        launchQuery();
    }

    private void launchQuery() {
        Bundle bundle = getIntent().getExtras();

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();

        String nombre = bundle.getString(NOMBRE_MARCA, "sin nombre");
        String whereClause = "nombre = '" + nombre + "'";
        dataQuery.setWhereClause(whereClause);

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.addRelated("tiendas");

        dataQuery.setQueryOptions(queryOptions);

        Backendless.Persistence.of(Marca.class).find(dataQuery, new DefaultCallback<BackendlessCollection<Marca>>(this) {
            @Override
            public void handleResponse(BackendlessCollection<Marca> response) {
                super.handleResponse(response);

                Marca marca = response.getCurrentPage().get(0);
                tiendas = marca.getTiendas();

                while (!flag) {
                    try {
                        object.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                setUpMarcadores();
            }
        });
    }

    private void setUpMarcadores() {
        for (int i = 0; i < tiendas.size(); i++) {
            Tienda tienda = tiendas.get(i);

            Double latitud = tienda.getLatitud();
            Double longitud = tienda.getLongitud();

            if (latitud != null && longitud != null) {
                LatLng latLng = new LatLng(latitud, longitud);
                Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(tienda.getNombre_tienda()));
                marker.setTag(tienda);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;

        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);

        CameraUpdate camUpd = CameraUpdateFactory.newLatLngZoom(new LatLng(40.41, -3.69), 5);
        map.moveCamera(camUpd);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                if (marker.isInfoWindowShown())
                    marker.hideInfoWindow();
                else
                    marker.showInfoWindow();
                LatLng latLng = marker.getPosition();
                CameraUpdate camUp = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                map.animateCamera(camUp);

                return true;
            }
        });

        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View view = getLayoutInflater().inflate(R.layout.maps_marker, null);
                Tienda tienda = (Tienda) marker.getTag();

                String nombre = tienda.getNombre_tienda();
                if (nombre != null) {
                    TextView nombreV = (TextView) view.findViewById(R.id.marker_nombre);
                    nombreV.setText(nombre);
                    nombreV.setVisibility(View.VISIBLE);
                }

                String ciudad = tienda.getCiudad();
                if (ciudad != null) {
                    TextView ciudadV = (TextView) view.findViewById(R.id.marker_ciudad);
                    ciudadV.setText(ciudad);
                    ciudadV.setVisibility(View.VISIBLE);
                }

                String direccion = tienda.getDireccion();
                if (direccion != null) {
                    TextView direccionV = (TextView) view.findViewById(R.id.marker_direccion);
                    direccionV.setText(direccion);
                    direccionV.setVisibility(View.VISIBLE);
                }

                String codigo = tienda.getCodigo_postal();
                if (codigo != null) {
                    TextView codigoV = (TextView) view.findViewById(R.id.marker_codigo);
                    codigoV.setText(codigo);
                    codigoV.setVisibility(View.VISIBLE);
                }

                Integer telefono = tienda.getTelefono();
                if (telefono != null) {
                    TextView telefonoV = (TextView) view.findViewById(R.id.marker_telefono);
                    telefonoV.setText("" + telefono.intValue());
                    telefonoV.setVisibility(View.VISIBLE);
                }

                String web = tienda.getPagina_web();
                if (web != null) {
                    TextView webV = (TextView) view.findViewById(R.id.marker_web);
                    webV.setText(web);
                    webV.setVisibility(View.VISIBLE);
                }

                String email = tienda.getEmail();
                if (email != null) {
                    TextView emailV = (TextView) view.findViewById(R.id.marker_email);
                    emailV.setText(email);
                    emailV.setVisibility(View.VISIBLE);
                }
                return view;
            }
        });

        synchronized (object) {
            object.notify();
        }
        flag = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}