package com.example.personalwellness;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class AsyncClientCheckForUser extends AsyncTask<URL, String, String> {

    public static int mentalHealth = 0;
    public static int physicalHealth = 0;
    public static int stress = 0;
    public static int community = 0;
    public static int accountNum = 0;

    private static final String TAG = AsyncClientCheckForUser.class.getSimpleName();

    /*
    Method checks whether or not a username exists in the database. If this method returns
    "bad", then the user does exist. If this method returns "good", then the user
    does not exist in the database.
     */
    protected String doInBackground(URL... urls) {
        try {
            URL url = urls[0];
            // create connection and send HTTP request
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            Log.d(TAG, "----------- created connection ");
            conn.setRequestMethod("GET");
            conn.connect();// read first line of data that is returned

            Scanner in = new Scanner(url.openStream());
            String user = in.nextLine();
            JSONObject userJSON = new JSONObject(user);
            mentalHealth = Integer.parseInt(userJSON.getString("mentalHealth"));
            physicalHealth = Integer.parseInt(userJSON.getString("physicalHealth"));
            stress = Integer.parseInt(userJSON.getString("stress"));
            community = Integer.parseInt(userJSON.getString("community"));

            boolean realUser = false;

            // check to see if the user exists in the database
            while (in.hasNextLine()) {
                if (in.nextLine().contains("username")) {
                    realUser = true;
                }
            }


            if (realUser) {
                return "bad";
            }
            return "good";

        }catch (Exception e) {
            return e.toString();
        }
    }
}
