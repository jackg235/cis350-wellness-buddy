package com.example.personalwellness;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/*
This class allows us to create a user and send it to the database
 */
class AsyncCreateClient extends AsyncTask<URL, String, String> {

    private static final String TAG = AsyncClient.class.getSimpleName();

    @Override
    protected String doInBackground(URL... urls) {
        try {

            URL url = urls[0];
            // create connection and send HTTP request
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setChunkedStreamingMode(0);
            conn.connect();
            // get the current user to add to mongo
            CurrentUser curr = CurrentUser.getCurrentUser();
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("name",curr.getName());
            jsonBody.put("username",curr.getUserName());
            jsonBody.put("password",curr.getPassword());
            jsonBody.put("stress",curr.getStress());
            jsonBody.put("physicalHealth",curr.getPhysicalHealth());
            jsonBody.put("sleep",curr.getSleep());
            jsonBody.put("community",curr.getCommunity());
            Log.d(TAG, "Create----------- created JSON " + jsonBody.toString());

            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(jsonBody.toString());
            writer.flush();
            writer.close();
            out.close();
            conn.disconnect();
            return "";
        }catch (Exception e) {
            return e.toString();
        }
    }
}
