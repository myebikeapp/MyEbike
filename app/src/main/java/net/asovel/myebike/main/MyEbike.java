package net.asovel.myebike.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import net.asovel.myebike.R;
import net.asovel.myebike.resultadosebikes.EBikeListActivity;

import java.util.ArrayList;

public class MyEbike extends Fragment
{
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
    private String diametroRueda;
    private String suspension;
    private String autonomia;
    private String ubicacionMotor;
    private String precio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_myebike, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state)
    {
        super.onActivityCreated(state);

        iniUI();
    }

    private void iniUI()
    {
        spinnerUso = (Spinner) getView().findViewById(R.id.spinner_uso);
        String[] values = getResources().getStringArray(R.array.uso_array);
        CustomAdapter adaptador = new CustomAdapter(getActivity(), R.layout.asistente_spinner_title, R.id.text_spinner_subtitle,
                values, "Tipo de uso");
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerUso.setAdapter(adaptador);

        spinnerCuadro = (Spinner) getView().findViewById(R.id.spinner_cuadro);
        values = getResources().getStringArray(R.array.cuadro_array);
        adaptador = new CustomAdapter(getActivity(), R.layout.asistente_spinner_title, R.id.text_spinner_subtitle,
                values, "Cuadro");
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerCuadro.setAdapter(adaptador);

        spinnerRueda = (Spinner) getView().findViewById(R.id.spinner_rueda);
        values = getResources().getStringArray(R.array.rueda_array);
        adaptador = new CustomAdapter(getActivity(), R.layout.asistente_spinner_title, R.id.text_spinner_subtitle,
                values, "Tamaño de la rueda");
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerRueda.setAdapter(adaptador);

        spinnerSuspension = (Spinner) getView().findViewById(R.id.spinner_suspension);
        values = getResources().getStringArray(R.array.suspension_array);
        adaptador = new CustomAdapter(getActivity(), R.layout.asistente_spinner_title, R.id.text_spinner_subtitle,
                values, "Suspensión");
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerSuspension.setAdapter(adaptador);

        spinnerAutonomiaInf = (Spinner) getView().findViewById(R.id.spinner_autonomia_inf);
        values = getResources().getStringArray(R.array.autonomia_array);
        adaptador = new CustomAdapter(getActivity(), R.layout.asistente_spinner_title, R.id.text_spinner_subtitle,
                values, "Desde", "Todos");
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerAutonomiaInf.setAdapter(adaptador);

        spinnerAutonomiaSup = (Spinner) getView().findViewById(R.id.spinner_autonomia_sup);
        values = getResources().getStringArray(R.array.autonomia_array);
        adaptador = new CustomAdapter(getActivity(), R.layout.asistente_spinner_title, R.id.text_spinner_subtitle,
                values, "Hasta", "Todos");
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerAutonomiaSup.setAdapter(adaptador);

        spinnerMotor = (Spinner) getView().findViewById(R.id.spinner_motor);
        values = getResources().getStringArray(R.array.motor_array);
        adaptador = new CustomAdapter(getActivity(), R.layout.asistente_spinner_title, R.id.text_spinner_subtitle,
                values, "Ubicación del motor");
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerMotor.setAdapter(adaptador);

        spinnerPresupuestoInf = (Spinner) getView().findViewById(R.id.spinner_presupuesto_inf);
        values = getResources().getStringArray(R.array.presupuesto_array);
        adaptador = new CustomAdapter(getActivity(), R.layout.asistente_spinner_title, R.id.text_spinner_subtitle,
                values, "Desde", "Todos");
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerPresupuestoInf.setAdapter(adaptador);

        spinnerPresupuestoSup = (Spinner) getView().findViewById(R.id.spinner_presupuesto_sup);
        values = getResources().getStringArray(R.array.presupuesto_array);
        adaptador = new CustomAdapter(getActivity(), R.layout.asistente_spinner_title, R.id.text_spinner_subtitle,
                values, "Hasta", "Todos");
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerPresupuestoSup.setAdapter(adaptador);

        btnBuscar = (FloatingActionButton) getActivity().findViewById(R.id.btn_buscar_ebikes);

        setupListeners();
    }

    public class CustomAdapter extends ArrayAdapter<String>
    {
        private LayoutInflater inflater;
        private String title;
        private String firstItem;

        public CustomAdapter(Activity context, int resource, int textViewResourceId, String[] values, String title)
        {
            super(context, resource, textViewResourceId, values);
            this.inflater = context.getLayoutInflater();
            this.title = title;
        }

        public CustomAdapter(Activity context, int resource, int textViewResourceId, String[] values, String title,
                             String firstItem)
        {
            super(context, resource, textViewResourceId, values);
            this.inflater = context.getLayoutInflater();
            this.title = title;
            this.firstItem = firstItem;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.asistente_spinner_title, null);
            }
            TextView txtTitle = (TextView) convertView.findViewById(R.id.text_spinner_title);
            txtTitle.setText(title);

            String value = getItem(position);
            TextView txtSubTitle = (TextView) convertView.findViewById(R.id.text_spinner_subtitle);
            txtSubTitle.setText(value);

            if (position == 0)
            {
                txtSubTitle.setTextSize(14);
                txtSubTitle.setTextColor(Color.GRAY);
                if (firstItem != null)
                    txtSubTitle.setText(firstItem);
            } else
            {
                txtSubTitle.setTextSize(18);
                txtSubTitle.setTextColor(Color.BLUE);
            }

            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.asistente_spinner_list, null);
            }
            String value = getItem(position);
            TextView txtItem = (TextView) convertView.findViewById(R.id.text_spinner);
            txtItem.setText(value);

            return convertView;
        }
    }

    public void setupListeners()
    {
        spinnerUso.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        uso = null;
                        break;
                    case 1:
                        uso = "uso = 'urbana'";
                        break;
                    case 2:
                        uso = "uso = 'interurbana'";
                        break;
                    case 3:
                        uso = "uso = 'montana'";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        spinnerCuadro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        cuadro = null;
                        break;
                    case 1:
                        cuadro = "tipo_cuadro = 'abierto'";
                        break;
                    case 2:
                        cuadro = "tipo_cuadro = 'cerrado'";
                        break;
                    case 3:
                        cuadro = "tipo_cuadro = 'plegable'";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        spinnerRueda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        diametroRueda = null;
                        break;
                    case 1:
                        diametroRueda = "tamano_ruedas >= 16 and tamano_ruedas <= 24";
                        break;
                    case 2:
                        diametroRueda = "tamano_ruedas >= 26 and tamano_ruedas <= 27";
                        break;
                    case 3:
                        diametroRueda = "tamano_ruedas >= 28 and tamano_ruedas <= 29";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        spinnerSuspension.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        suspension = null;
                        break;
                    case 1:
                        diametroRueda = "suspension = 'no susp'";
                        break;
                    case 2:
                        diametroRueda = "suspension = 'solo sillin'";
                        break;
                    case 3:
                        diametroRueda = "suspension = 'delantera'";
                        break;
                    case 4:
                        diametroRueda = "suspension = 'delantera/sillin'";
                        break;
                    case 5:
                        diametroRueda = "suspension = 'full susp'";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        spinnerAutonomiaInf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        autonomia = null;
                        break;
                    case 1:
                        autonomia = "autonomia >= 30 and autonomia <= 50";
                        break;
                    case 2:
                        autonomia = "autonomia >= 50 and autonomia <= 80";
                        break;
                    case 3:
                        autonomia = "autonomia >= 80 and autonomia <= 120";
                        break;
                    case 4:
                        autonomia = "autonomia >= 120 and autonomia <= 150";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        spinnerAutonomiaSup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        diametroRueda = null;
                        break;
                    case 1:
                        diametroRueda = "tamano_ruedas = 16";
                        break;
                    case 2:
                        diametroRueda = "tamano_ruedas = 18";
                        break;
                    case 3:
                        diametroRueda = "tamano_ruedas = 20";
                        break;
                    case 4:
                        diametroRueda = "tamano_ruedas = 24";
                        break;
                    case 5:
                        diametroRueda = "tamano_ruedas = 26";
                        break;
                    case 6:
                        diametroRueda = "tamano_ruedas = 27.5";
                        break;
                    case 7:
                        diametroRueda = "tamano_ruedas = 28";
                        break;
                    case 8:
                        diametroRueda = "tamano_ruedas = 29";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        spinnerPresupuestoInf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
            {
                switch (position)
                {
                    case 0:
                        precio = null;
                        break;
                    case 1:
                        precio = "precio_SORT2 >= 600 and precio_SORT2 <= 800";
                        break;
                    case 2:
                        precio = "precio_SORT2 >= 800 and precio_SORT2 <= 1000";
                        break;
                    case 3:
                        precio = "precio_SORT2 >= 1000 and precio_SORT2 <= 1200";
                        break;
                    case 4:
                        precio = "precio_SORT2 >= 1200 and precio_SORT2 <= 1500";
                        break;
                    case 5:
                        precio = "precio_SORT2 >= 1500 and precio_SORT2 <= 1800";
                        break;
                    case 6:
                        precio = "precio_SORT2 >= 1800 and precio_SORT2 <= 2500";
                        break;
                    case 7:
                        precio = "precio_SORT2 >= 2500 and precio_SORT2 <= 5000";
                        break;
                    case 8:
                        precio = "precio_SORT2 >= 5000";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        btnBuscar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getContext(), EBikeListActivity.class);
                Bundle bundle = new Bundle();
                ArrayList<String> listClauses = setupWhereClause();
                bundle.putStringArrayList(EBikeListActivity.WHERECLAUSE, listClauses);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public ArrayList<String> setupWhereClause()
    {
        ArrayList<String> listClauses = new ArrayList<>();

        if (uso != null)
            listClauses.add(uso);
        if (diametroRueda != null)
            listClauses.add(diametroRueda);
        if (autonomia != null)
            listClauses.add(autonomia);
        if (precio != null)
            listClauses.add(precio);
        if (suspension != null)
            listClauses.add(suspension);
        if (cuadro != null)
            listClauses.add(cuadro);
        if (ubicacionMotor != null)
            listClauses.add(ubicacionMotor);

        if (listClauses.size() < 1)
            return null;

        return listClauses;
    }
}

       /* final RadioButton btnMotorTrasero = (RadioButton) getView().findViewById(R.id.ubicacion_trasero);
        btnMotorTrasero.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (ubicacionMotor != null && ubicacionMotor.equals("ubicacion_motor = 'trasero'"))
                {
                    radioGroupUbicacionMotor.clearCheck();
                    ubicacionMotor = null;
                } else
                {
                    ubicacionMotor = "ubicacion_motor = 'trasero'";
                }
            }
        });

        final RadioButton btnMotorCentral = (RadioButton) getView().findViewById(R.id.ubicacion_central);
        btnMotorCentral.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (ubicacionMotor != null && ubicacionMotor.equals("ubicacion_motor = 'central'"))
                {
                    radioGroupUbicacionMotor.clearCheck();
                    ubicacionMotor = null;
                } else
                {
                    ubicacionMotor = "ubicacion_motor = 'central'";
                }
            }
        });

        final RadioButton btnMotorDelantero = (RadioButton) getView().findViewById(R.id.ubicacion_delantero);
        btnMotorDelantero.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (ubicacionMotor != null && ubicacionMotor.equals("ubicacion_motor = 'delantero'"))
                {
                    radioGroupUbicacionMotor.clearCheck();
                    ubicacionMotor = null;
                } else
                {
                    ubicacionMotor = "ubicacion_motor = 'delantero'";
                }
            }
        });*/

       /* final RadioButton btnCuadroAbierto = (RadioButton) getView().findViewById(R.id.cuadro_abierto);
        btnCuadroAbierto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (cuadro != null && cuadro.equals("tipo_cuadro = 'abierto'"))
                {
                    radioGroupCuadro.clearCheck();
                    cuadro = null;
                } else
                {
                    cuadro = "tipo_cuadro = 'abierto'";
                }
            }
        });

        final RadioButton btnCuadroCerrado = (RadioButton) getView().findViewById(R.id.cuadro_cerrado);
        btnCuadroCerrado.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (cuadro != null && cuadro.equals("tipo_cuadro = 'cerrado'"))
                {
                    radioGroupCuadro.clearCheck();
                    cuadro = null;
                } else
                {
                    cuadro = "tipo_cuadro = 'cerrado'";
                }
            }
        });

        final RadioButton btnCuadroPlegable = (RadioButton) getView().findViewById(R.id.cuadro_plegable);
        btnCuadroPlegable.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (cuadro != null && cuadro.equals("tipo_cuadro = 'plegable'"))
                {
                    radioGroupCuadro.clearCheck();
                    cuadro = null;
                } else
                {
                    cuadro = "tipo_cuadro = 'plegable'";
                }
            }
        });*/

       /* final RadioButton btnSuspDelanteraSillin = (RadioButton) getView().findViewById(R.id.suspension_delantera_sillin);
        btnSuspDelanteraSillin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (suspension != null && suspension.equals("suspension = 'delantera/sillin'"))
                {
                    radioGroupSuspension.clearCheck();
                    suspension = null;
                } else
                {
                    suspension = "suspension = 'delantera/sillin'";
                }
            }
        });

        final RadioButton btnSuspSoloSillin = (RadioButton) getView().findViewById(R.id.suspension_solo_sillin);
        btnSuspSoloSillin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (suspension != null && suspension.equals("suspension = 'solo sillin'"))
                {
                    radioGroupSuspension.clearCheck();
                    suspension = null;
                } else
                {
                    suspension = "suspension = 'solo sillin'";
                }
            }
        });

        final RadioButton btnSuspFull = (RadioButton) getView().findViewById(R.id.suspension_full_suspension);
        btnSuspFull.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (suspension != null && suspension.equals("suspension = 'full susp'"))
                {
                    radioGroupSuspension.clearCheck();
                    suspension = null;
                } else
                {
                    suspension = "suspension = 'full susp'";
                }
            }
        });

        final RadioButton btnSuspDelantera = (RadioButton) getView().findViewById(R.id.suspension_delantera);
        btnSuspDelantera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (suspension != null && suspension.equals("suspension = 'delantera'"))
                {
                    radioGroupSuspension.clearCheck();
                    suspension = null;
                } else
                {
                    suspension = "suspension = 'delantera'";
                }
            }
        });

        final RadioButton btnSuspNoSusp = (RadioButton) getView().findViewById(R.id.suspension_no_suspension);
        btnSuspNoSusp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (suspension != null && suspension.equals("suspension = 'no susp'"))
                {
                    radioGroupSuspension.clearCheck();
                    suspension = null;
                } else
                {
                    suspension = "suspension = 'no susp'";
                }
            }
        });*/
