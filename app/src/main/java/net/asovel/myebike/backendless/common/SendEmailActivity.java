package net.asovel.myebike.backendless.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.backendless.Backendless;

import net.asovel.myebike.R;


public class SendEmailActivity extends Activity
{
    private EditText emailEdit;
    private Button sendButton;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.send_email);

        initUI();
    }

    private void initUI()
    {
        //emailEdit = (EditText) findViewById(R.id.emailAddressEdit);
        //sendButton = (Button) findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onSendButtonClicked();
            }
        });
    }

    private void onSendButtonClicked()
    {
        String email = emailEdit.getText().toString();
        String code = getIntent().getStringExtra("code");
        String method = getIntent().getStringExtra("method");
        String table = getIntent().getStringExtra("table");

        String subject = String.format("Code generation sample for: %s in %s table", method, table);

        Backendless.Messaging.sendTextEmail(subject, code, email, new DefaultCallback<Void>(SendEmailActivity.this)
        {
            @Override
            public void handleResponse(Void response)
            {
                super.handleResponse(response);
                Toast.makeText(getBaseContext(), "Email sent.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}