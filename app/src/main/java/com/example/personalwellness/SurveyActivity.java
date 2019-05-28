package com.example.personalwellness;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;

public class SurveyActivity extends AppCompatActivity {

    private TempSurvey mSurvey = new TempSurvey();

    private TextView mQuestionNumberView;
    private TextView mQuestionView;
    private Button mButtonChoice1;
    private Button mButtonChoice2;
    private Button mButtonChoice3;
    private Button mPrev;
    private int mQuestionNumber = 0;

    private static final String TAG = SurveyActivity.class.getSimpleName();
    ResourceDB resourceDB = ResourceDB.getResourceDB();
    Proc proc = new Proc(resourceDB);
    private HashMap<Integer, String> surveyResponses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        String extra = getIntent().getStringExtra("currName");
        Log.d("---------------survey has name", extra);
        CurrentUser curr = CurrentUser.getCurrentUser();

        surveyResponses = new HashMap<Integer, String>();

        mQuestionNumberView = (TextView) findViewById(R.id.question_number);
        mQuestionView = (TextView) findViewById(R.id.question);

        mButtonChoice1 = (Button) findViewById(R.id.choice1);
        mButtonChoice2 = (Button) findViewById(R.id.choice2);
        mButtonChoice3 = (Button) findViewById(R.id.choice3);
        mPrev = (Button) findViewById(R.id.previous_question);

        updateQuestion();

        mButtonChoice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                surveyResponses.put(mQuestionNumber, mButtonChoice1.getText().toString());
                updateQuestion();
                Log.d(TAG, "-----------answer choice 1, question answer: " + mButtonChoice1.getText().toString());
            }
        });

        mButtonChoice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                surveyResponses.put(mQuestionNumber, mButtonChoice2.getText().toString());
                updateQuestion();
                Log.d(TAG, "-----------answer choice 2, question answer: " + mButtonChoice2.getText().toString());
            }
        });

        mButtonChoice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                surveyResponses.put(mQuestionNumber, mButtonChoice3.getText().toString());
                updateQuestion();
                Log.d(TAG, "-----------answer choice 3, question answer: " + mButtonChoice3.getText().toString());
            }
        });
        mPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousQuestion();
                Log.d(TAG, "-----------previous question");
            }
        });

    }

    /*
    Method will update the question if the user has not finished the survey. Else,
    it will create a new user in the database and segue to the home screen
     */
    private void updateQuestion() {
        if (mSurvey.checkQuestionAvailable(mQuestionNumber)) {
            mQuestionView.setText(mSurvey.getQuestion(mQuestionNumber));
            mButtonChoice1.setText(mSurvey.getChoice1(mQuestionNumber));
            mButtonChoice2.setText(mSurvey.getChoice2(mQuestionNumber));
            mButtonChoice3.setText(mSurvey.getChoice3(mQuestionNumber));
            mQuestionNumberView.setText(mQuestionNumber + "");
            mQuestionNumber++;
        } else {
            // if we have completed all questions in the survey, create the new user
            CurrentUser curr = CurrentUser.getCurrentUser();
            //analysis of survey answers
            assignHealthValues(curr);
            // create the user in the database
            createUser();
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

            //0 : sc, 1 : mh, 2 : ph, 3 : d, 4 : sl, 5 : st
            Intent i = new Intent(SurveyActivity.this, HomeActivity.class);
            Log.d("----------------- survey curr", curr.getCommunity()+"");
            i.putExtra("maxScore", maxMessage);
            startActivity(i);
        }
    }
    /*
    Method handles the "previous question" button
     */
    private void previousQuestion() {
        if (mQuestionNumber > 0) {
            mQuestionNumber--;
            mQuestionNumberView.setText(mQuestionNumber + "");
            mQuestionView.setText(mSurvey.getQuestion(mQuestionNumber));
            mButtonChoice1.setText(mSurvey.getChoice1(mQuestionNumber));
            mButtonChoice2.setText(mSurvey.getChoice2(mQuestionNumber));
            mButtonChoice3.setText(mSurvey.getChoice3(mQuestionNumber));
        }
    }
    /*
    Method calls our async task to create a new user in the database
     */
    public void createUser() {
        try {
            CurrentUser user = CurrentUser.getCurrentUser();
            URL url = new URL("http://10.0.2.2:3001/jazz" + "?username=" + user.getUserName()
                    + "&password=" + user.getPassword()
                    + "&name=" + user.getName()
                    + "&mentalHealth=" + user.getMentalHealth()
                    + "&stress=" + user.getStress()
                    + "&physicalHealth=" + user.getPhysicalHealth()
                    + "&community=" + user.getCommunity()
                    + "&sleep=" + user.getSleep());
            AsyncTask<URL, String, String> task = new AsyncCreateClient();
            task.execute(url);
            Log.d(TAG, "----------- reached createUser " + url);
        } catch (Exception e) {
        }
    }

    /*
    Method assigns values to the user based on survey responses
     */
    private void assignHealthValues(CurrentUser curr) {
        int stress = 0;
        int sleep = 0;
        int mentalHealth = 0;
        int community = 0;
        int ph = 0;
        if (surveyResponses.get(1).equals("Poor")) {
            mentalHealth += 2;
        } else if (surveyResponses.get(1).equals("Average")) {
            mentalHealth += 1;
        }
        if (surveyResponses.get(4).equals("Yes")) {
            mentalHealth += 2;
        } else if (surveyResponses.get(4).equals("I'm not sure")) {
            mentalHealth += 1;
        }
        if (surveyResponses.get(5).equals("Very much")) {
            sleep += 3;
        } else if (surveyResponses.get(5).equals("Somewhat")) {
            sleep += 1;
        }
        if (surveyResponses.get(6).equals("Not at all")) {
            ph += 3;
        } else if (surveyResponses.get(6).equals("Once or twice")) {
            ph += 1;
        }
        if (surveyResponses.get(7).equals("Very much")) {
            stress += 3;
        } else if (surveyResponses.get(7).equals("Somewhat")) {
            stress += 2;
        }
        if (surveyResponses.get(8).equals("Not at all")) {
            community += 3;
        } else if (surveyResponses.get(8).equals("Somewhat")) {
            community += 1;
        }
        curr.updateSleep(sleep);
        curr.updateMentalHealth(mentalHealth);
        curr.updateCommunity(community);
        curr.updatePhysicalHealth(ph);
        curr.updateStress(stress);
    }
}
