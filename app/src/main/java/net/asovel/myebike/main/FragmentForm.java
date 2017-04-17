package net.asovel.myebike.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

import net.asovel.myebike.R;

import java.util.HashMap;
import java.util.Map;

public class FragmentForm extends Fragment
{
    public static final String TAG = FragmentForm.class.getSimpleName();

    private EditText nameText;
    private TextInputLayout nameLayout;
    private EditText emailText;
    private TextInputLayout emailLayout;
    private EditText phoneText;
    private TextInputLayout phoneLayout;
    private EditText postalText;
    private TextInputLayout postalLayout;
    private EditText messageText;
    private Switch switchConditions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state)
    {
        super.onActivityCreated(state);
        initUserForm();
    }

    private void initUserForm()
    {
        nameText = (EditText) getView().findViewById(R.id.user_name);
        nameLayout = (TextInputLayout) getView().findViewById(R.id.tilayout_name);
        emailText = (EditText) getView().findViewById(R.id.user_email);
        emailLayout = (TextInputLayout) getView().findViewById(R.id.tilayout_email);
        phoneText = (EditText) getView().findViewById(R.id.user_phone);
        phoneLayout = (TextInputLayout) getView().findViewById(R.id.tilayout_phone);
        postalText = (EditText) getView().findViewById(R.id.user_postal);
        postalLayout = (TextInputLayout) getView().findViewById(R.id.tilayout_postal);
        messageText = (EditText) getView().findViewById(R.id.user_message);
        switchConditions = (Switch) getView().findViewById(R.id.conditions_switch);
        Button contactarButton = (Button) getView().findViewById(R.id.button_contactar);

        SharedPreferences prefs = getContext().getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        String email = prefs.getString("email", "");
        emailText.setText(email);

        TextView conditionsText = (TextView) getView().findViewById(R.id.conditions_text);
        conditionsText.setMovementMethod(LinkMovementMethod.getInstance());
        String links = getString(R.string.form_conditions_links);
        conditionsText.setText(Html.fromHtml(links));


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

        postalText.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus) {
                    checkEmail(postalText.getText().toString());
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
        if (!switchConditions.isChecked()) {
            Toast.makeText(getContext(), "Debes aceptar las condiciones de uso", Toast.LENGTH_LONG).show();
            return;
        }

        final String nombre = nameText.getText().toString();
        if (!checkNombre(nombre)) {
            Toast.makeText(getContext(), "Nombre incorrecto", Toast.LENGTH_LONG).show();
            return;
        }

        final String email = emailText.getText().toString();
        if (!checkEmail(email)) {
            Toast.makeText(getContext(), "Email incorrecto", Toast.LENGTH_LONG).show();
            return;
        }

        final String telefono = phoneText.getText().toString();
        if (!checkPhone(telefono)) {
            Toast.makeText(getContext(), "Telefono incorrecto", Toast.LENGTH_LONG).show();
            return;
        }

        final String postal = postalText.getText().toString();
        final String mensaje = messageText.getText().toString();

        HashMap lead = new HashMap();
        lead.put("nombre", nombre);
        lead.put("email", email);
        lead.put("telefono", telefono);
        lead.put("codigo_postal", postal);
        lead.put("mensaje", mensaje);

        Backendless.Persistence.of("lead").save(lead, new AsyncCallback<Map>()
        {
            public void handleResponse(Map response)
            {
                Toast.makeText(getContext(), "Menssaje enviado", Toast.LENGTH_LONG).show();
                cleanForm();

                String messageBody = "Nombre: " + nombre + '\n'
                        + "Email: " + email + '\n'
                        + "Telefono: " + telefono + '\n'
                        + "Postal: " + postal + '\n'
                        + "Mensaje: " + mensaje + '\n';

                Backendless.Messaging.sendTextEmail("myebike", messageBody, "myebikeapp@gmail.com", new AsyncCallback<Void>()
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
                Toast.makeText(getContext(), fault.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean checkNombre(String nombre)
    {

        if (nombre.equals("")) {
            nameLayout.setError("Nombre incorrecto");
            return false;
        }
        nameLayout.setError(null);
        return true;
    }

    private boolean checkEmail(String email)
    {
        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError(null);
            return true;
        }
        emailLayout.setError("Email incorrecto");
        return false;
    }

    private boolean checkPhone(String phone)
    {
        int length = phone.length();

        if (phone.matches("[0-9]+") && length >= 9 && length <= 15) {
            phoneLayout.setError(null);
            return true;
        }
        phoneLayout.setError("Telefono incorrecto");
        return false;
    }

    private boolean checkPostal(String postal)
    {

        if (postal.matches("[0-9]+") && postal.length() == 5) {
            postalLayout.setError(null);
            return true;
        }
        postalLayout.setError("Codigo postal incorrecto");
        return false;
    }

    private void cleanForm()
    {
        switchConditions.setChecked(false);
        nameText.setText(null);
        emailText.setText(null);
        phoneText.setText(null);
        postalText.setText(null);
        messageText.setText(null);
    }
}