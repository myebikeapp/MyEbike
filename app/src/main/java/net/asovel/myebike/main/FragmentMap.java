package net.asovel.myebike.main;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.backendless.Backendless;
import com.backendless.BackendlessCollection;
import com.backendless.persistence.BackendlessDataQuery;
import com.backendless.persistence.QueryOptions;
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
import net.asovel.myebike.backendless.common.DefaultCallback;
import net.asovel.myebike.backendless.data.Marca;
import net.asovel.myebike.backendless.data.Tienda;
import net.asovel.myebike.backendless.data.TiendaLista;
import net.asovel.myebike.utils.Constants;
import net.asovel.myebike.utils.WebActivity;

import java.util.List;

public class FragmentMap extends Fragment implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback,
        GoogleMap.OnMyLocationButtonClickListener
{
    private static final String TAG = FragmentMap.class.getSimpleName();
    private static final int PETICION_PERMISO_LOCALIZACION = 101;

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

        String label = getResources().getString(R.string.FragmentMap_label);
        getActivity().setTitle(label);

        String caller = getArguments().getString(Constants.CALLER, "");
        if (caller.equals(MainActivity.TAG)) {
            queryTiendas();
            //QueryTiendasTask task = new QueryTiendasTask();
            //task.execute();
            return;
        }
        queryMarca();
    }

    @Override
    public void onResume()
    {
        super.onResume();
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

        Backendless.Data.of(Marca.class).find(dataQuery, new DefaultCallback<BackendlessCollection<Marca>>(getContext())
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

    private void queryTiendas()
    {
        Backendless.Data.of(TiendaLista.class).find(new DefaultCallback<BackendlessCollection<TiendaLista>>(getContext())
        {
            @Override
            public void handleResponse(BackendlessCollection<TiendaLista> response)
            {
                super.handleResponse(response);

                TiendaLista tiendaLista = response.getCurrentPage().get(0);
                tiendas = tiendaLista.getLista();

                setUpMarcadores();
            }
        });
    }

    private class QueryTiendasTask extends AsyncTask<Void, Void, Boolean>
    {
        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected Boolean doInBackground(Void... values)
        {
            BackendlessDataQuery dataQuery = new BackendlessDataQuery();
            QueryOptions queryOptions = new QueryOptions();

            queryOptions.setPageSize(100);
            dataQuery.setQueryOptions(queryOptions);

            BackendlessCollection<Tienda> response1 = Backendless.Data.of(Tienda.class).find(dataQuery);
            int numObjects = response1.getTotalObjects();
            tiendas = response1.getCurrentPage();

            queryOptions.setOffset(100);
            dataQuery.setQueryOptions(queryOptions);

            BackendlessCollection<Tienda> response2 = Backendless.Data.of(Tienda.class).find(dataQuery);
            tiendas.addAll(response2.getCurrentPage());

            queryOptions.setPageSize(numObjects - 200);
            queryOptions.setOffset(200);
            dataQuery.setQueryOptions(queryOptions);

            BackendlessCollection<Tienda> response3 = Backendless.Data.of(Tienda.class).find(dataQuery);
            tiendas.addAll(response3.getCurrentPage());

            return true;
        }

        @Override
        protected void onProgressUpdate(Void... values)
        {
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            setUpMarcadores();
        }

        @Override
        protected void onCancelled()
        {
        }
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
        Tienda tienda = (Tienda) marker.getTag();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Contactar con " + tienda.getNombre_tienda());

        final String phone = String.valueOf(tienda.getTelefono());
        final String url = tienda.getPagina_web();
        final String email = tienda.getEmail();

        String[] values = new String[]{phone, url, email};
        CustomAdapter adapter = new CustomAdapter(getContext(), R.layout.maps_dialog, R.id.dialog_text, values);

        builder.setAdapter(adapter, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int position)
            {
                dialog.cancel();

                switch (position) {
                    case 0:
                        call(phone);
                        break;
                    case 1:
                        showWeb(url);
                        break;
                    case 2:
                        sendEmail(email);
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

    private void call(String phone)
    {
        if (phone != null) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phone));
            startActivity(intent);
        }
    }

    private void showWeb(String url)
    {
        if (url != null) {
            Intent intent = new Intent(getContext(), WebActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.URL, url);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private void sendEmail(String email)
    {
        if (email != null) {
           /* Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:?subject=" + email + "&body=" + "bodyalñkdjalñsjdañsld");
            intent.setData(data);
            startActivity(intent);*/

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setType("vnd.android.cursor.item/email");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"adria.bosk@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "My Email Subject");
            intent.putExtra(Intent.EXTRA_TEXT, "My email content");
            startActivity(Intent.createChooser(intent, "Send mail using..."));
        }
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
        private int[] drawables = {R.drawable.ic_menu_buscador, R.drawable.ic_menu_bici, R.drawable.ic_menu_noticias};

        public CustomAdapter(Context context, int resource, int textViewResourceId, String[] values)
        {
            super(context, resource, textViewResourceId, values);
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

