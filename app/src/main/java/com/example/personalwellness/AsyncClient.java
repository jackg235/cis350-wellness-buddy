package com.example.personalwellness;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class AsyncClient extends AsyncTask<URL, String, String> {

    public static int mentalHealth = 0;
    public static int physicalHealth = 0;
    public static int stress = 0;
    public static int community = 0;
    public static int accountNum = 0;

    private static final String TAG = AsyncClient.class.getSimpleName();

    protected String doInBackground(URL... urls) {
        try {
            URL url = urls[0];
            // create connection and send HTTP request
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            Log.d(TAG, "----------- created connection ");
            conn.setRequestMethod("GET");
            conn.connect();
            Scanner in = new Scanner(url.openStream());
            String msg = in.nextLine();
            ArrayList<String> userInfo = new ArrayList<>();

            while (in.hasNextLine()) {
                userInfo.add(in.nextLine());
            }

            getUserFromText(userInfo);

            return "";
        }catch (Exception e) {
            return e.toString();
        }
    }

    public void getUserFromText(ArrayList<String> userInfo) {
        CurrentUser curr = CurrentUser.getCurrentUser();
        String name = null;
        String username = null;
        String password = null;
        for (String s : userInfo) {
            String[] pieces = s.split("'");
            if (pieces.length >= 3) {
                if (pieces[1].equals("name")) {
                    name = pieces[3];
                    curr.updateName(name);
                }
                if (pieces[1].equals("username")) {
                    username = pieces[3];
                    curr.updateUsername(username);
                }
                if (pieces[1].equals("password")) {
                    password = pieces[3];
                    curr.updatePassword(password);
                }
            }
        }
        if (name != null && username != null && password != null) {
            for (String s : userInfo) {
                String[] pieces = s.split("'");
                if (pieces.length >= 3) {
                    if (pieces[1].equals("stress")) {

                        try {
                            stress = Integer.parseInt(pieces[3]);
                            curr.updateStress(stress);
                        } catch (Exception e) {
                            curr.updateStress(0);
                        };
                    }

                    if (pieces[1].equals("physicalHealth")) {

                        try {
                            physicalHealth = Integer.parseInt(pieces[3]);
                            curr.updatePhysicalHealth(physicalHealth);
                        } catch (Exception e) {
                            curr.updatePhysicalHealth(0);
                        };
                    }


                    if (pieces[1].equals("mentalHealth")) {

                        try {
                            mentalHealth = Integer.parseInt(pieces[3]);
                            curr.updateMentalHealth(mentalHealth);
                        } catch (Exception e) {
                            curr.updateMentalHealth(0);
                        };
                    }


                    if (pieces[1].equals("accountNum")) {

                        try {
                            accountNum = Integer.parseInt(pieces[3]);
                            curr.updateAccountNum(accountNum);
                        } catch (Exception e) {
                            curr.updateAccountNum(0);

                        };
                    }


                    if (pieces[1].equals("community")) {

                        try {
                            community = Integer.parseInt(pieces[3]);
                            curr.updateCommunity(community);
                        } catch (Exception e) {
                            curr.updateCommunity(0);
                        };
                    }
                }
            }
        }
    }
}
