package com.example.personalwellness;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private EditText usernameET, passwordET;
    private Button loginButton, createAccountButton;
    public String usernameString;
    public String passwordString;
    ResourceDB resourceDB = ResourceDB.getResourceDB();
    Proc proc = new Proc(resourceDB);
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameET = (EditText) findViewById(R.id.usernameEditText);
        passwordET = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        createAccountButton = (Button) findViewById(R.id.createAccountButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameString = usernameET.getText().toString();
                passwordString = passwordET.getText().toString();

                // check if the user exists in the database
                String realUser = "";
                try {
                    Log.d("create account ", "----------- checking for existing username ");
                    URL url = new URL("http://10.0.2.2:3001/person?username=" + usernameString);
                    AsyncTask<URL, String, String> task = new AsyncClientCheckForUser();
                    task.execute(url);
                    realUser = task.get();
                } catch (Exception e) {

                }

                // if the user does not exist in the database...
                if (realUser.equals("good")) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage("Invalid user");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else {

                    // if the user does exist in the database, create the singleton object
                    CurrentUser curr = CurrentUser.getCurrentUser();
                    curr.updateCommunity(AsyncClientCheckForUser.community);
                    curr.updateMentalHealth(AsyncClientCheckForUser.mentalHealth);
                    curr.updatePhysicalHealth(AsyncClientCheckForUser.physicalHealth);
                    curr.updateStress(AsyncClientCheckForUser.stress);
                    curr.updateSleep(0);

                    // this will populate the singleton user class
                    getUser(usernameString);

                    // check for correct password
                    if (checkValidUser(passwordString, curr.getPassword())) {
                        //resourceDB.clearResources();
                        Intent i = new Intent(MainActivity.this,
                                HomeActivity.class);
                        int max = proc.getRecs(curr);
                        String maxMessage = "";
                        if (max == 0) {
                            maxMessage = "mh";
                        } else if (max == 1) {
                            maxMessage = "st";
                        } else if (max == 2) {
                            maxMessage = "ph";
                        } else if (max == 3) {
                            maxMessage = "sc";
                        } else {
                            maxMessage = "sl";
                        }
                        i.putExtra("maxScore", maxMessage);
                        startActivity(i);
                    } else {
                        // if the user has entered a bad password...
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setTitle("Error");
                        alertDialog.setMessage("Invalid password");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                }
            }
        });

        // if the user clicks the create account button, segue to the create account activity page
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(MainActivity.this,
                        CreateAccountActivity.class);
                startActivity(i2);
            }
        });

    }

    /*
    Method checks if the password entered by the user matches the one in the database
     */
    private boolean checkValidUser(String inputPassword, String truePassword) {
        if (truePassword.equals(inputPassword)) {
            return true;
        }
        return false;
    }
    /*
    Method calls our async task to populate the current user singleton object
     */
    public static String getUser(String username) {
        try {
            Log.d(TAG, "----------- entering main activity get user ");
            URL url = new URL("http://10.0.2.2:3001/person?username=" + username);
            AsyncTask<URL, String, String> task = new AsyncClient();
            task.execute(url);
            String name = task.get();
            return name;
        } catch (Exception e) {
            return e.toString();
        }
    }
}