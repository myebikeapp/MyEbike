package net.asovel.myebike.main;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import net.asovel.myebike.R;
import net.asovel.myebike.backendless.data.Marca;
import net.asovel.myebike.backendless.data.Tienda;
import net.asovel.myebike.backendless.data.TiendaLista;
import net.asovel.myebike.resultadosebikes.MapaActivity;
import net.asovel.myebike.utils.AnalyticsApplication;
import net.asovel.myebike.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class FragmentMapa extends Fragment implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMyLocationButtonClickListener
{
    private static final String TAG = FragmentMapa.class.getSimpleName();

    private static final int PETICION_PERMISO_LOCALIZACION = 101;

    private Tracker tracker;

    private MapView mapView;
    private GoogleMap map;
    private List<Tienda> tiendas;
    private Object object = new Object();
    private volatile boolean flag = false;

    private Marker lastMarker;
    private float lastZoom;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mapView = (MapView) view.findViewById(R.id.map);

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int paddingBottom = (int) (50 * displayMetrics.density);

        String caller = getArguments().getString(Constants.CALLER, "");
        if (caller.equals(MainActivity.TAG))
            mapView.setPadding(0, 0, 0, paddingBottom);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle state)
    {
        super.onActivityCreated(state);

        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        tracker = application.getDefaultTracker();

        String label = getResources().getString(R.string.FragmentMap_label);
        getActivity().setTitle(label);

        String caller = getArguments().getString(Constants.CALLER, "");

        if (caller.equals(MainActivity.TAG))
            queryTiendas();
        else
            queryMarca();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        String caller = getArguments().getString(Constants.CALLER, "");

        if (caller.equals(MainActivity.TAG))
            tracker.setScreenName(MainActivity.TAG + " --> " + TAG);
        else
            tracker.setScreenName(MapaActivity.TAG + " --> " + TAG);

        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        mapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private void queryMarca()
    {
        BackendlessDataQuery dataQuery = new BackendlessDataQuery();
        QueryOptions queryOptions = new QueryOptions();

        Bundle bundle = getArguments();

        String nombre = bundle.getString(Constants.NOMBRE_MARCA);

        if (nombre == null)
            return;

        String whereClause = "nombre = '" + nombre + "'";
        dataQuery.setWhereClause(whereClause);

        queryOptions.addRelated("tiendas");
        dataQuery.setQueryOptions(queryOptions);

        Backendless.Data.of(Marca.class).find(dataQuery, new AsyncCallback<BackendlessCollection<Marca>>()
        {
            @Override
            public void handleResponse(BackendlessCollection<Marca> response)
            {
                Marca marca = response.getCurrentPage().get(0);
                tiendas = marca.getTiendas();

                setUpMarcadores();
            }

            @Override
            public void handleFault(BackendlessFault fault)
            {
                Log.d(TAG, fault.getMessage());
                Toast.makeText(getContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void queryTiendas()
    {
        Backendless.Data.of(TiendaLista.class).find(new AsyncCallback<BackendlessCollection<TiendaLista>>()
        {
            @Override
            public void handleResponse(BackendlessCollection<TiendaLista> response)
            {
                TiendaLista tiendaLista = response.getCurrentPage().get(0);
                tiendas = tiendaLista.getLista();

                setUpMarcadores();
            }

            @Override
            public void handleFault(BackendlessFault fault)
            {
                Log.d(TAG, fault.getMessage());
                Toast.makeText(getContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setUpMarcadores()
    {
        synchronized (object) {
            while (!flag) {
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        int numTiendas = tiendas.size();
        String label = getResources().getString(R.string.FragmentMap_label) + " (" + numTiendas + ")";
        getActivity().setTitle(label);

        for (int i = 0; i < numTiendas; i++) {
            Tienda tienda = tiendas.get(i);

            Double latitud = tienda.getLatitud();
            Double longitud = tienda.getLongitud();

            if (latitud != null && longitud != null) {
                LatLng latLng = new LatLng(latitud, longitud);
                Marker marker = map.addMarker(new MarkerOptions().position(latLng));
                marker.setTag(tienda);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        this.map = googleMap;

        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMapToolbarEnabled(false);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PETICION_PERMISO_LOCALIZACION);

        } else {
            map.setMyLocationEnabled(true);
            map.setOnMyLocationButtonClickListener(this);
        }

        CameraUpdate camUpd = CameraUpdateFactory.newLatLngZoom(new LatLng(40.41, -3.69), 5);
        map.moveCamera(camUpd);

        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener()
        {
            @Override
            public void onCameraMove()
            {
                if (lastMarker != null && lastMarker.isInfoWindowShown()) {
                    if (map.getCameraPosition().zoom < lastZoom)
                        lastMarker.hideInfoWindow();
                }
                lastZoom = map.getCameraPosition().zoom;
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            public boolean onMarkerClick(Marker marker)
            {
                lastMarker = marker;
                marker.showInfoWindow();
                LatLng latLng = marker.getPosition();
                float zoom = map.getCameraPosition().zoom;
                zoom = (zoom >= 15) ? zoom : 15;
                CameraUpdate camUp = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
                map.animateCamera(camUp);

                return true;
            }
        });

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

                View view = getActivity().getLayoutInflater().inflate(R.layout.maps_marker, null);
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

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener()
        {
            @Override
            public void onInfoWindowClick(Marker marker)
            {
                onWindowClick(marker);
            }
        });

        flag = true;
        synchronized (object) {
            object.notify();
        }
    }

    private void onWindowClick(Marker marker)
    {
        final Tienda tienda = (Tienda) marker.getTag();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View customTitle = getActivity().getLayoutInflater().inflate(R.layout.maps_dialog_title, null);
        TextView textView = (TextView) customTitle.findViewById(R.id.dialog_title);
        textView.setText("Contactar con " + tienda.getNombre_tienda());

        builder.setCustomTitle(customTitle);

        final String phone = String.valueOf(tienda.getTelefono());
        final String url = tienda.getPagina_web();
        final String email = tienda.getEmail();

        List<String> values = new ArrayList<>();
        final List<Integer> drawables = new ArrayList<>();

        if (phone != null && phone != "null") {
            values.add(phone);
            drawables.add(R.drawable.ic_dialog_phone);
        }
        if (url != null) {
            values.add(url);
            drawables.add(R.drawable.ic_dialog_web);
        }
        if (email != null) {
            values.add(email);
            drawables.add(R.drawable.ic_dialog_email);
        }
        CustomAdapter adapter = new CustomAdapter(getContext(), R.layout.maps_dialog, R.id.dialog_text, values.toArray(new String[0]), drawables.toArray(new Integer[0]));

        builder.setAdapter(adapter, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int position)
            {
                dialog.cancel();

                int id = drawables.get(position);
                switch (id) {
                    case R.drawable.ic_dialog_phone:
                        call(phone, tienda.getNombre_tienda());
                        break;
                    case R.drawable.ic_dialog_web:
                        showWeb(url, tienda.getNombre_tienda());
                        break;
                    case R.drawable.ic_dialog_email:
                        sendEmail(email, tienda.getNombre_tienda());
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();
            }
        });
        builder.create();
        builder.show();
    }

    private void call(String phone, String nombreTienda)
    {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(Constants.CATEGORY_TIENDA)
                .setAction("Teléfono")
                .setLabel(nombreTienda)
                .build());

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);

    }

    private void showWeb(String url, String nombreTienda)
    {
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(Constants.CATEGORY_TIENDA)
                .setAction("Web")
                .setLabel(nombreTienda)
                .build());

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url + Constants.UTM));
        startActivity(intent);
    }

    private void sendEmail(String email, String nombreTienda)
    {

           /* Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:?subject=" + "myebikeapp@gmail.com" + "&body=" + "bodyalñkdjalñsjdañsld");
            intent.setData(data);
            startActivity(intent);*/

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(Constants.CATEGORY_TIENDA)
                .setAction("Correo")
                .setLabel(nombreTienda)
                .build());

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("vnd.android.cursor.item/email");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"myebikeapp@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "My Email Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "My email content");
        startActivity(Intent.createChooser(intent, "Send mail using..."));
    }

    @Override
    public boolean onMyLocationButtonClick()
    {
        return false;
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == PETICION_PERMISO_LOCALIZACION) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                map.setMyLocationEnabled(true);
                map.setOnMyLocationButtonClickListener(this);
            }
        }
    }

    public class CustomAdapter extends ArrayAdapter<String>
    {
        private Integer[] drawables;

        public CustomAdapter(Context context, int resource, int textViewResourceId, String[] values, Integer[] drawables)
        {
            super(context, resource, textViewResourceId, values);
            this.drawables = drawables;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.maps_dialog, null);
            }

            ImageView imageView = (ImageView) convertView.findViewById(R.id.dialog_image);
            TextView textView = (TextView) convertView.findViewById(R.id.dialog_text);

            imageView.setImageResource(drawables[position]);
            textView.setText(getItem(position));

            return convertView;
        }
    }
}

