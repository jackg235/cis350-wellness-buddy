package com.example.personalwellness;

/**
 * Created by jackgoettle on 3/21/19.
 */

public class TempSurvey {

    private String mQuestions [] = {
            "Overall, how would you rate your mental health?",
            "When did you get your last mental health examination?",
            "How knowledgeable are you about mental health related issues? ",
            "Have you had feelings of depression within the past 6 months?",
            "Have you experienced trouble sleeping over the past month?",
            "Approximately how many times per week do you exercise?",
            "How much has stress been affecting your wellbeing over the past month?",
            "To what extent do you feel you are a member of a community at Penn?"
    };


    private String mChoices [][] = {
            {"Excellent", "Average", "Poor"},
            {"Over 6 months ago", "Less than 6 months ago", "I've never had one"},
            {"Very knowledgeable", "Somewhat knowledgeable", "Not knowledgeable"},
            {"Yes", "No", "I'm not sure"},
            {"Very much", "Somewhat", "Not at all"},
            {"Multiple", "Once or twice", "Not at all"},
            {"Very much", "Somewhat", "Not at all"},
            {"Very much", "Somewhat", "Not at all"}

    };

    public boolean checkQuestionAvailable(int a) {
        if (a >= mQuestions.length) {
            return false;
        }
        return true;
    }


    public String getQuestion(int a) {
        String question = mQuestions[a];
        return question;
    }


    public String getChoice1(int a) {
        String choice0 = mChoices[a][0];
        return choice0;
    }


    public String getChoice2(int a) {
        String choice1 = mChoices[a][1];
        return choice1;
    }

    public String getChoice3(int a) {
        String choice2 = mChoices[a][2];
        return choice2;
    }
}
