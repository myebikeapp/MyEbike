package net.asovel.myebike.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import net.asovel.myebike.R;
import net.asovel.myebike.resultadosebikes.EBikeListActivity;

import java.util.ArrayList;

public class MyEbike extends Fragment
{
    private RadioGroup radioGroupUso;
    private Spinner spinnerDiametroRueda;
    private Spinner spinnerAutonomia;
    private Spinner spinnerPrecio;
    private RadioGroup radioGroupSuspension;
    private RadioGroup radioGroupCuadro;
    private RadioGroup radioGroupUbicacionMotor;
    private FloatingActionButton btnBuscar;

    private String uso;
    private String diametroRueda;
    private String autonomia;
    private String precio;
    private String suspension;
    private String cuadro;
    private String ubicacionMotor;

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
        radioGroupUso = (RadioGroup) getView().findViewById(R.id.radioGroup_tipo_uso);

        spinnerDiametroRueda = (Spinner) getView().findViewById(R.id.spinner_diametro_rueda);
        ArrayAdapter<CharSequence> adaptador = ArrayAdapter.createFromResource(getContext(), R.array.diametros_array, R.layout.asistente_spinner_title);
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerDiametroRueda.setAdapter(adaptador);

        spinnerAutonomia = (Spinner) getView().findViewById(R.id.spinner_autonomia);
        adaptador = ArrayAdapter.createFromResource(getContext(), R.array.autonomia_array, R.layout.asistente_spinner_title);
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerAutonomia.setAdapter(adaptador);

        spinnerPrecio = (Spinner) getView().findViewById(R.id.spinner_precio);
        adaptador = ArrayAdapter.createFromResource(getContext(), R.array.precios_array, R.layout.asistente_spinner_title);
        adaptador.setDropDownViewResource(R.layout.asistente_spinner_list);
        spinnerPrecio.setAdapter(adaptador);

        radioGroupSuspension = (RadioGroup) getView().findViewById(R.id.radioGroup_suspension);
        radioGroupCuadro = (RadioGroup) getView().findViewById(R.id.radioGroup_cuadro);
        radioGroupUbicacionMotor = (RadioGroup) getView().findViewById(R.id.radioGroup_ubicacion_motor);

        btnBuscar = (FloatingActionButton) getActivity().findViewById(R.id.btn_buscar_ebikes);

        setupListeners();
    }

    public void setupListeners()
    {
        final RadioButton btnUsoUrbana = (RadioButton) getView().findViewById(R.id.uso_urbana);
        btnUsoUrbana.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (uso != null && uso.equals("uso = 'urbana'"))
                {
                    radioGroupUso.clearCheck();
                    uso = null;
                } else
                    uso = "uso = 'urbana'";
            }
        });

        final RadioButton btnUsoInterurbana = (RadioButton) getView().findViewById(R.id.uso_interurbana);
        btnUsoInterurbana.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (uso != null && uso.equals("uso = 'interurbana'"))
                {
                    radioGroupUso.clearCheck();
                    uso = null;
                } else
                    uso = "uso = 'interurbana'";
            }
        });

        final RadioButton btnUsoMontana = (RadioButton) getView().findViewById(R.id.uso_montana);
        btnUsoMontana.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (uso != null && uso.equals("uso = 'montana'"))
                {
                    radioGroupUso.clearCheck();
                    uso = null;
                } else
                    uso = "uso = 'montana'";
            }
        });

        spinnerDiametroRueda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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

        spinnerPrecio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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

        spinnerAutonomia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
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

        final RadioButton btnSuspDelanteraSillin = (RadioButton) getView().findViewById(R.id.suspension_delantera_sillin);
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
        });


        final RadioButton btnCuadroAbierto = (RadioButton) getView().findViewById(R.id.cuadro_abierto);
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

        final RadioButton btnCuadroSemiAbierto = (RadioButton) getView().findViewById(R.id.cuadro_semi_abierto);
        btnCuadroSemiAbierto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (cuadro != null && cuadro.equals("tipo_cuadro = 'semi-abierto'"))
                {
                    radioGroupCuadro.clearCheck();
                    cuadro = null;
                } else
                {
                    cuadro = "tipo_cuadro = 'semi-abierto'";
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
        });


        final RadioButton btnMotorTrasero= (RadioButton) getView().findViewById(R.id.ubicacion_trasero);
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

        final RadioButton btnMotorCentral= (RadioButton) getView().findViewById(R.id.ubicacion_central);
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

        final RadioButton btnMotorDelantero= (RadioButton) getView().findViewById(R.id.ubicacion_delantero);
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
