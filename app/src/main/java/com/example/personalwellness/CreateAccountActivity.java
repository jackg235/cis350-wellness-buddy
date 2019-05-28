package com.example.personalwellness;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.io.Serializable;
import java.net.URL;

public class CreateAccountActivity extends AppCompatActivity {

    private ResourceDB database;
    private EditText nameET, usernameET, passwordET;
    private Button backButton, registerButton;
    private TextView errorTV;
    final Context context = this;
    private Button alertButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        errorTV = (TextView) findViewById(R.id.caErrorText);
        nameET = (EditText) findViewById(R.id.caNameEditText);
        usernameET = (EditText) findViewById(R.id.caUsernameEditText);
        passwordET = (EditText) findViewById(R.id.caPasswordEditText);
        backButton = (Button) findViewById(R.id.caBackButton);
        registerButton = (Button) findViewById(R.id.caRegisterButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameString = nameET.getText().toString();
                String usernameString = usernameET.getText().toString();
                String passwordString = passwordET.getText().toString();

                // check to make sure that the user has filled all of the fields to create an account
                if (passwordString == null || usernameString == null || nameString == null ||
                passwordString.equals("") || usernameString.equals("") || nameString.equals("")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(CreateAccountActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Please fill all fields");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                // check to see if the username is taken
                String realUser = "";
                try {
                    Log.d("create account ", "----------- checking for existing username ");
                    URL url = new URL("http://10.0.2.2:3001/person?username=" + usernameString);
                    AsyncTask<URL, String, String> task = new AsyncClientCheckForUser();
                    task.execute(url);
                    realUser = task.get();
                } catch (Exception e) {

                }
                // if the username is taken...
                if (realUser.equals("bad")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(CreateAccountActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Username is taken");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {
                    // create the user and segue to the survey
                    CurrentUser curr = CurrentUser.getCurrentUser();
                    curr.updateName(nameString);
                    curr.updateUsername(usernameString);
                    curr.updatePassword(passwordString);
                    Intent i = new Intent(CreateAccountActivity.this,
                            SurveyActivity.class);
                    i.putExtra("currName", curr.getName());
                    startActivity(i);
                }
            }
        });
    }
}