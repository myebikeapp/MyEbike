package net.asovel.myebike.resultadosebikes;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import net.asovel.myebike.R;
import net.asovel.myebike.backendless.common.DefaultCallback;
import net.asovel.myebike.backendless.common.Defaults;
import net.asovel.myebike.backendless.data.Marca;
import net.asovel.myebike.backendless.data.Tienda;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback
{
    public static final String NOMBRE_MARCA = "NOMBRE_MARCA";

    private GoogleMap map;

    private List<Tienda> tiendas;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Backendless.setUrl(Defaults.SERVER_URL);
        Backendless.initApp(this, Defaults.APPLICATION_ID, Defaults.SECRET_KEY, Defaults.VERSION);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //launchQuery();
    }

    private void launchQuery()
    {
        Bundle bundle = getIntent().getExtras();

        BackendlessDataQuery dataQuery = new BackendlessDataQuery();

        String nombre = bundle.getString(NOMBRE_MARCA);
        String whereClause = "nombre = '" + nombre + "'";
        dataQuery.setWhereClause(whereClause);

        QueryOptions queryOptions = new QueryOptions();

        queryOptions.addRelated("tiendas");

        dataQuery.setQueryOptions(queryOptions);

        Backendless.Persistence.of(Marca.class).find(dataQuery, new DefaultCallback<BackendlessCollection<Marca>>(this)
        {
            @Override
            public void handleResponse(BackendlessCollection<Marca> response)
            {
                super.handleResponse(response);

                Marca marca = response.getCurrentPage().get(0);
                tiendas = marca.getTiendas();
                setUpMarcadores();
            }
        });
    }

    private void setUpMarcadores()
    {
        for (int i = 0; i < tiendas.size(); i++)
        {
            Tienda tienda = tiendas.get(i);
            LatLng latLng = new LatLng(Double.valueOf(tienda.getLatitud()), Double.valueOf(tienda.getLongitud()));
            Marker marker = map.addMarker(new MarkerOptions().position(latLng).title(tienda.getNombre_tienda()));
            marker.setTag(tienda);
        }
    }

    private void insertarMarcador()
    {

    }

    private void mostrarLineas()
    {
        PolylineOptions lineas = new PolylineOptions()
                .add(new LatLng(45.0, -12.0))
                .add(new LatLng(45.0, 5.0))
                .add(new LatLng(34.5, 5.0))
                .add(new LatLng(34.5, -12.0))
                .add(new LatLng(45.0, -12.0));

        lineas.width(8);
        lineas.color(Color.RED);

        map.addPolyline(lineas);
    }

    private void mostrarPoligono()
    {
        PolygonOptions rectangulo = new PolygonOptions()
                .add(new LatLng(45.0, -12.0),
                        new LatLng(45.0, 5.0),
                        new LatLng(34.5, 5.0),
                        new LatLng(34.5, -12.0),
                        new LatLng(45.0, -12.0));

        rectangulo.strokeWidth(8);
        rectangulo.strokeColor(Color.RED);

        map.addPolygon(rectangulo);
    }

    private void cambiarOpciones()
    {
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.getUiSettings().setZoomControlsEnabled(true);
    }


    private void animarMadrid()
    {
        LatLng madrid = new LatLng(40.417325, -3.683081);

        CameraPosition camPos = new CameraPosition.Builder()
                .target(madrid)   //Centramos el mapa en Madrid
                .zoom(19)         //Establecemos el zoom en 19
                .bearing(45)      //Establecemos la orientación con el noreste arriba
                .tilt(70)         //Bajamos el punto de vista de la cámara 70 grados
                .build();

        CameraUpdate camUpd3 =
                CameraUpdateFactory.newCameraPosition(camPos);

        map.animateCamera(camUpd3);
    }

    private void obtenerPosicion()
    {
        CameraPosition camPos = map.getCameraPosition();

        LatLng coordenadas = camPos.target;
        double latitud = coordenadas.latitude;
        double longitud = coordenadas.longitude;


        Toast.makeText(this, "Lat: " + latitud + " | Long: " + longitud, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        return true;
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
    public void onMapReady(GoogleMap googleMap)
    {
        this.map = googleMap;

        launchQuery();

        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);
        map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
        {
            @Override
            public View getInfoWindow(Marker marker)
            {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker)
            {
                View view = getLayoutInflater().inflate(R.layout.maps_marker, null);

                TextView nombre = (TextView) view.findViewById(R.id.marker_nombre);
                TextView ciudad = (TextView) view.findViewById(R.id.marker_ciudad);
                TextView direccion = (TextView) view.findViewById(R.id.marker_direccion);
                TextView codigo = (TextView) view.findViewById(R.id.marker_codigo);
                TextView telefono = (TextView) view.findViewById(R.id.marker_telefono);
                TextView web = (TextView) view.findViewById(R.id.marker_web);
                TextView email = (TextView) view.findViewById(R.id.marker_email);

                Tienda tienda = (Tienda) marker.getTag();

                nombre.setText(tienda.getNombre_tienda());
                ciudad.setText(tienda.getCiudad());
                direccion.setText(tienda.getDireccion() + " " + tienda.getNumero());
                codigo.setText(tienda.getCodigo_postal());
                telefono.setText("" + tienda.getTelefono());
                web.setText(tienda.getPagina_web());
                email.setText(tienda.getEmail());

                return view;
            }
        });

        CameraUpdate camUpd1 = CameraUpdateFactory.newLatLngZoom(new LatLng(40.41, -3.69), 5);
        map.moveCamera(camUpd1);


        /*
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            public void onMapClick(LatLng point)
            {
                Projection proj = map.getProjection();
                Point coord = proj.toScreenLocation(point);

                Toast.makeText(MapsActivity.this,
                        "Click\n" +
                                "Lat: " + point.latitude + "\n" +
                                "Lng: " + point.longitude + "\n" +
                                "X: " + coord.x + " - Y: " + coord.y,
                        Toast.LENGTH_LONG).show();
            }
        });

        map.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener()
        {
            @Override
            public void onCameraMoveStarted(int i)
            {

            }
        });

        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener()
        {
            @Override
            public void onCameraMove()
            {

            }
        });

        map.setOnCameraMoveCanceledListener(new GoogleMap.OnCameraMoveCanceledListener()
        {
            @Override
            public void onCameraMoveCanceled()
            {

            }
        });

        map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener()
        {
            @Override
            public void onCameraIdle()
            {
                CameraPosition position = map.getCameraPosition();
                Toast.makeText(MapsActivity.this,
                        "Cambio Cámara\n" +
                                "Lat: " + position.target.latitude + "\n" +
                                "Lng: " + position.target.longitude + "\n" +
                                "Zoom: " + position.zoom + "\n" +
                                "Orientación: " + position.bearing + "\n" +
                                "Ángulo: " + position.tilt,
                        Toast.LENGTH_SHORT).show();

            }
        });*/

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            public boolean onMarkerClick(Marker marker)
            {
                if (marker.isInfoWindowShown())
                    marker.hideInfoWindow();
                else
                    marker.showInfoWindow();
                LatLng latLng = marker.getPosition();
                CameraUpdate camUp = CameraUpdateFactory.newLatLngZoom(latLng, 15 );
                map.animateCamera(camUp);


                return true;
            }
        });
    }
}
