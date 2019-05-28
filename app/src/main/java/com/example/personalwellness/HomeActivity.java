package com.example.personalwellness;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.net.URL;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        try {
            Log.d("HomeActivity ", "-----------  setting resource DB");
            URL url = new URL("http://10.0.2.2:3002/all");
            AsyncTask<URL, String, String> task = new AsyncResourceClient();
            task.execute(url);
            task.get();
        } catch (Exception e) {

        }
        String extra = getIntent().getStringExtra("maxScore");
        Log.d("-----------------max Score", extra);

        ImageButton capsButton = (ImageButton) findViewById(R.id.mentalhealth);
        capsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int1 = new Intent(HomeActivity.this,CAPSPage.class);
                startActivity(int1);
            }
        });

        ImageButton dietButton = (ImageButton) findViewById(R.id.diet);
        dietButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int2 = new Intent(HomeActivity.this,DietPage.class);
                startActivity(int2);
            }
        });


        ImageButton stressButton = (ImageButton) findViewById(R.id.stress);
        stressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int3 = new Intent(HomeActivity.this,StressPage.class);
                startActivity(int3);
            }
        });


        ImageButton fitnessButton = (ImageButton) findViewById(R.id.fitness);
        fitnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int4 = new Intent(HomeActivity.this,FitnessPage.class);
                startActivity(int4);
            }
        });

        ImageButton sleepButton = (ImageButton) findViewById(R.id.sleep);
        sleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int5 = new Intent(HomeActivity.this,SleepPage.class);
                startActivity(int5);
            }
        });

        ImageButton communityButton = (ImageButton) findViewById(R.id.community);
        communityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int6 = new Intent(HomeActivity.this,CommunityPage.class);
                startActivity(int6);
            }
        });

        Button recsButton = (Button) findViewById(R.id.recommendations);
        recsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent int7 = new Intent(HomeActivity.this,RecsPage.class);
                int7.putExtra("maxCategory", extra);
                startActivity(int7);
            }
        });
    }
}
