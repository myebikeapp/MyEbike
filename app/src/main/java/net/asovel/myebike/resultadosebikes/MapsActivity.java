package net.asovel.myebike.resultadosebikes;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.asovel.myebike.R;
import net.asovel.myebike.backendless.common.DefaultCallback;
import net.asovel.myebike.backendless.common.Defaults;
import net.asovel.myebike.backendless.data.Marca;
import net.asovel.myebike.backendless.data.Tienda;
import net.asovel.myebike.main.MainActivity;
import net.asovel.myebike.utils.Constants;
import net.asovel.myebike.utils.WebActivity;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMyLocationButtonClickListener {

    private static final String TAG = MapsActivity.class.getSimpleName();

    private static final int PETICION_PERMISO_LOCALIZACION = 101;

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

        String nombre = bundle.getString(Constants.NOMBRE_MARCA, "sin nombre");
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

                synchronized (object) {
                    while (!flag) {
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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
                Marker marker = map.addMarker(new MarkerOptions()
                        .position(latLng));
                marker.setTag(tienda);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.map = googleMap;

       // map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PETICION_PERMISO_LOCALIZACION);

        } else {
            map.setMyLocationEnabled(true);
            map.setOnMyLocationButtonClickListener(this);
        }

        CameraUpdate camUpd = CameraUpdateFactory.newLatLngZoom(new LatLng(40.41, -3.69), 5);
        map.moveCamera(camUpd);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                if (marker.isInfoWindowShown())
                    marker.hideInfoWindow();
                else
                    marker.showInfoWindow();
                LatLng latLng = marker.getPosition();
                float zoom = map.getCameraPosition().zoom;
                zoom = (zoom >= 15) ? zoom : 15;
                CameraUpdate camUp = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
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

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                onWindowClick(marker);
            }
        });

        flag = true;
        synchronized (object) {
            object.notify();
        }
    }

    private void onWindowClick(Marker marker) {
        Tienda tienda = (Tienda) marker.getTag();
        String url = tienda.getPagina_web();

        if (url != null) {
            Intent intent = new Intent(this, WebActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.URL, url);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {

        /*CameraUpdate camUpd = CameraUpdateFactory.zoomTo(14);
        map.moveCamera(camUpd);*/
        return false;
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == PETICION_PERMISO_LOCALIZACION) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                map.setMyLocationEnabled(true);
                map.setOnMyLocationButtonClickListener(this);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (!getIntent().getExtras().getString(Constants.CALLER, "").equals("")) {
                    Intent intent = new Intent(this, MainActivity.class);
                    NavUtils.navigateUpTo(this, intent);
                } else
                    NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

/*import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener

private static final int PETICION_CONFIG_UBICACION = 201;

private GoogleApiClient apiClient;
private LocationRequest locRequest;
private Location location;

apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

    private void enableLocation() {

        locRequest = new LocationRequest();
        locRequest.setInterval(2000);
        locRequest.setFastestInterval(1000);
        locRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        LocationSettingsRequest locSettingsRequest =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(locRequest)
                        .build();

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(apiClient, locSettingsRequest);

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult locationSettingsResult) {

                final Status status = locationSettingsResult.getStatus();

                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        Log.d(TAG, "Configuración correcta");
                        startLocationUpdates();
                        break;

                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            Log.d(TAG, "Se requiere actuación del usuario");
                            status.startResolutionForResult(MapsActivity.this, PETICION_CONFIG_UBICACION);

                        } catch (IntentSender.SendIntentException e) {
                            Log.d(TAG, "Error al intentar solucionar configuración de ubicación");
                        }
                        break;

                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.d(TAG, "No se puede cumplir la configuración de ubicación necesaria");
                        Toast.makeText(MapsActivity.this, "No se puede cumplir la configuración de ubicación necesaria", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PETICION_CONFIG_UBICACION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.d(TAG, "El usuario no ha realizado los cambios de configuración necesarios");
                        break;
                }
                break;
        }
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PETICION_PERMISO_LOCALIZACION);

        } else {
            Log.d(TAG, "Inicio de recepción de ubicaciones");
            LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locRequest, this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        synchronized (this.location) {
            this.location = location;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Error grave al conectar con Google Play Services", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

*/