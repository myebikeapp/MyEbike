package net.asovel.myebike.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import net.asovel.myebike.LoginActivity;
import net.asovel.myebike.R;
import net.asovel.myebike.resultadosebikes.EBikeListActivity;

import java.util.ArrayList;

public class MyEBike extends Fragment {

    public static final String NAME = MyEBike.class.getName();

    public static final String[] USO = {null, "urbana", "interurbana", "montana"};
    public static final String[] CUADRO = {null, "abierto", "cerrado", "plegable"};
    public static final int[] DIAMETRO_RUEDA = {0, 0, 16, 24, 26, 27, 28, 29};
    public static final String[] SUSPENSION = {null, "no susp", "solo sillin", "delantera", "delantera/sillin", "full susp"};
    public static final int[] AUTONOMIA = {30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 121};
    public static final String[] MOTOR = {null, "delantero", "central", "trasero"};
    public static final int[] PRESUPUESTO = {600, 800, 1000, 1200, 1500, 1800, 2500, 5000, 5001};

    private Spinner spinnerUso;
    private Spinner spinnerCuadro;
    private Spinner spinnerRueda;
    private Spinner spinnerSuspension;
    private Spinner spinnerAutonomiaInf;
    private Spinner spinnerAutonomiaSup;
    private Spinner spinnerMotor;
    private Spinner spinnerPresupuestoInf;
    private Spinner spinnerPresupuestoSup;
    private FloatingActionButton btnBuscar;

    private String uso;
    private String cuadro;
    private int diametroRuedaInf;
    private int diametroRuedaSup;
    private String suspension;
    private int autonomiaInf = AUTONOMIA[0];
    private int autonomiaSup = AUTONOMIA[AUTONOMIA.length - 1];
    private String motor;
    private int presupuestoInf = PRESUPUESTO[0];
    private int presupuestoSup = PRESUPUESTO[PRESUPUESTO.length - 1];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_myebike, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        iniUI();
    }

    private void iniUI() {
        spinnerUso = (Spinner) getView().findViewById(R.id.spinner_uso);
        String[] values = getResources().getStringArray(R.array.uso);
        CustomAdapter adaptador = new CustomAdapter(getActivity(), R.layout.asistente_spinner_title, R.id.text_spinner_subtitle,
                values, "Tipo de uso", 0);
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerUso.setAdapter(adaptador);

        spinnerCuadro = (Spinner) getView().findViewById(R.id.spinner_cuadro);
        values = getResources().getStringArray(R.array.cuadro);
        adaptador = new CustomAdapter(getActivity(), R.layout.asistente_spinner_title, R.id.text_spinner_subtitle,
                values, "Cuadro", 0);
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerCuadro.setAdapter(adaptador);

        spinnerRueda = (Spinner) getView().findViewById(R.id.spinner_rueda);
        values = getResources().getStringArray(R.array.tamano_rueda);
        adaptador = new CustomAdapter(getActivity(), R.layout.asistente_spinner_title, R.id.text_spinner_subtitle,
                values, "Tamaño de la rueda", 0);
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerRueda.setAdapter(adaptador);

        spinnerSuspension = (Spinner) getView().findViewById(R.id.spinner_suspension);
        values = getResources().getStringArray(R.array.suspension);
        adaptador = new CustomAdapter(getActivity(), R.layout.asistente_spinner_title, R.id.text_spinner_subtitle,
                values, "Suspensión", 0);
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerSuspension.setAdapter(adaptador);

        spinnerAutonomiaInf = (Spinner) getView().findViewById(R.id.spinner_autonomia_inf);
        values = getResources().getStringArray(R.array.autonomia);
        adaptador = new CustomAdapter(getActivity(), R.layout.asistente_spinner_title, R.id.text_spinner_subtitle,
                values, "Desde", 0, "Todos");
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerAutonomiaInf.setAdapter(adaptador);

        spinnerAutonomiaSup = (Spinner) getView().findViewById(R.id.spinner_autonomia_sup);
        adaptador = new CustomAdapter(getActivity(), R.layout.asistente_spinner_title, R.id.text_spinner_subtitle,
                values, "Hasta", AUTONOMIA.length - 1, "Todos");
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerAutonomiaSup.setAdapter(adaptador);
        spinnerAutonomiaSup.setSelection(AUTONOMIA.length - 1);

        spinnerMotor = (Spinner) getView().findViewById(R.id.spinner_motor);
        values = getResources().getStringArray(R.array.motor);
        adaptador = new CustomAdapter(getActivity(), R.layout.asistente_spinner_title, R.id.text_spinner_subtitle,
                values, "Ubicación del motor", 0);
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerMotor.setAdapter(adaptador);

        spinnerPresupuestoInf = (Spinner) getView().findViewById(R.id.spinner_presupuesto_inf);
        values = getResources().getStringArray(R.array.presupuesto);
        adaptador = new CustomAdapter(getActivity(), R.layout.asistente_spinner_title, R.id.text_spinner_subtitle,
                values, "Desde", 0, "Todos");
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerPresupuestoInf.setAdapter(adaptador);

        spinnerPresupuestoSup = (Spinner) getView().findViewById(R.id.spinner_presupuesto_sup);
        adaptador = new CustomAdapter(getActivity(), R.layout.asistente_spinner_title, R.id.text_spinner_subtitle,
                values, "Hasta", PRESUPUESTO.length - 1, "Todos");
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerPresupuestoSup.setAdapter(adaptador);
        spinnerPresupuestoSup.setSelection(PRESUPUESTO.length - 1);

        btnBuscar = (FloatingActionButton) getActivity().findViewById(R.id.btn_buscar_ebikes);

        setupListeners();
    }

    public class CustomAdapter extends ArrayAdapter<String> {
        private LayoutInflater inflater;
        private String title;
        private int specialPosition;
        private String specialItem;

        public CustomAdapter(Activity context, int resource, int textViewResourceId, String[] values, String title,
                             int specialPosition) {
            super(context, resource, textViewResourceId, values);
            this.inflater = context.getLayoutInflater();
            this.title = title;
            this.specialPosition = specialPosition;
        }

        public CustomAdapter(Activity context, int resource, int textViewResourceId, String[] values, String title,
                             int specialPosition, String specialItem) {
            super(context, resource, textViewResourceId, values);
            this.inflater = context.getLayoutInflater();
            this.title = title;
            this.specialPosition = specialPosition;
            this.specialItem = specialItem;
        }

        @Override
        @TargetApi(23)
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.asistente_spinner_title, null);
            }
            TextView txtTitle = (TextView) convertView.findViewById(R.id.text_spinner_title);
            txtTitle.setText(title);

            String value = getItem(position);
            TextView txtSubTitle = (TextView) convertView.findViewById(R.id.text_spinner_subtitle);
            txtSubTitle.setText(value);

            if (position == specialPosition) {
                txtSubTitle.setTextSize(14);
                txtSubTitle.setTextColor(Color.GRAY);
                if (specialItem != null)
                    txtSubTitle.setText(specialItem);
            } else {
                txtSubTitle.setTextSize(18);

                int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                int color;
                if (currentapiVersion < 23)
                    color = getResources().getColor(R.color.colorAccent);
                else
                    color = getResources().getColor(R.color.colorAccent, null);

                txtSubTitle.setTextColor(color);
            }

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {

                convertView = inflater.inflate(R.layout.asistente_spinner_list, null);
            }
            String value = getItem(position);
            TextView txtItem = (TextView) convertView.findViewById(R.id.text_spinner);
            txtItem.setText(value);

            return convertView;
        }
    }

    public void setupListeners() {
        spinnerUso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                uso = USO[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerCuadro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                cuadro = CUADRO[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerRueda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                diametroRuedaInf = DIAMETRO_RUEDA[position * 2];
                diametroRuedaSup = DIAMETRO_RUEDA[position * 2 + 1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerSuspension.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                suspension = SUSPENSION[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerAutonomiaInf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                if (autonomiaSup < AUTONOMIA[position]) {
                    spinnerAutonomiaInf.setSelection(spinnerAutonomiaSup.getSelectedItemPosition());
                    spinnerAutonomiaSup.setSelection(position);
                } else {
                    autonomiaInf = AUTONOMIA[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerAutonomiaSup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                if (autonomiaInf > AUTONOMIA[position]) {
                    spinnerAutonomiaSup.setSelection(spinnerAutonomiaInf.getSelectedItemPosition());
                    spinnerAutonomiaInf.setSelection(position);
                } else {
                    autonomiaSup = AUTONOMIA[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerMotor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                motor = MOTOR[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerPresupuestoInf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                if (presupuestoSup < PRESUPUESTO[position]) {
                    spinnerPresupuestoInf.setSelection(spinnerPresupuestoSup.getSelectedItemPosition());
                    spinnerPresupuestoSup.setSelection(position);
                } else {
                    presupuestoInf = PRESUPUESTO[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerPresupuestoSup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                if (presupuestoInf > PRESUPUESTO[position]) {
                    spinnerPresupuestoSup.setSelection(spinnerPresupuestoInf.getSelectedItemPosition());
                    spinnerPresupuestoInf.setSelection(position);
                } else {
                    presupuestoSup = PRESUPUESTO[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = getActivity().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
                String email = prefs.getString("email", "");

                Intent intent;
                Bundle bundle = new Bundle();

                ArrayList<String> listClauses = setupWhereClause();
                bundle.putStringArrayList(EBikeListActivity.WHERECLAUSE, listClauses);

                if (email.equals("")) {
                    intent = new Intent(getContext(), LoginActivity.class);
                    bundle.putString("CALLER", NAME);
                } else {
                    intent = new Intent(getContext(), EBikeListActivity.class);
                }

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public ArrayList<String> setupWhereClause() {
        ArrayList<String> listClauses = new ArrayList<>();

        if (uso != null)
            listClauses.add("uso = '" + uso + "'");
        if (cuadro != null)
            listClauses.add("tipo_cuadro = '" + cuadro + "'");
        if (diametroRuedaInf != 0)
            listClauses.add("tamano_ruedas >= " + diametroRuedaInf + " and tamano_ruedas <= " + diametroRuedaSup);
        if (suspension != null)
            listClauses.add("suspension = '" + suspension + "'");
        if (autonomiaInf != AUTONOMIA[0])
            listClauses.add("autonomia >= " + autonomiaInf);
        if (autonomiaSup != AUTONOMIA[AUTONOMIA.length - 1])
            listClauses.add("autonomia <= " + autonomiaSup);
        if (motor != null)
            listClauses.add("ubicacion_motor = '" + motor + "'");
        if (presupuestoInf != PRESUPUESTO[0])
            listClauses.add("precio_SORT2 >= " + presupuestoInf);
        if (presupuestoSup != PRESUPUESTO[PRESUPUESTO.length - 1])
            listClauses.add("precio_SORT2 <= " + presupuestoSup);

        if (listClauses.size() < 1)
            return null;

        return listClauses;
    }

    public void limpiarSeleccion() {
        spinnerUso.setSelection(0);
        spinnerCuadro.setSelection(0);
        spinnerRueda.setSelection(0);
        spinnerSuspension.setSelection(0);
        spinnerAutonomiaInf.setSelection(0);
        spinnerAutonomiaSup.setSelection(AUTONOMIA.length - 1);
        spinnerMotor.setSelection(0);
        spinnerPresupuestoInf.setSelection(0);
        spinnerPresupuestoSup.setSelection(PRESUPUESTO.length - 1);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_myebike, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_limpiar:
                limpiarSeleccion();
                return true;
        }
        return false;
    }
}