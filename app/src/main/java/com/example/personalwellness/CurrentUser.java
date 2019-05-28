package com.example.personalwellness;

import java.util.List;

public class CurrentUser {
    private static CurrentUser singleton = null;
    private String name, username, password;
    private int accountNum = 0 , mentalHealth = 0, stress = 0, physicalHealth = 0,
            community = 0, sleep = 0;
    private List<Resource> personalRecs = null;

    private CurrentUser () {

    }

    public static CurrentUser getCurrentUser () {
        if (singleton == null) {
            singleton = new CurrentUser();
        }
        return singleton;
    }

    public String getUserName() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getAccountNum() {
        return accountNum;
    }

    public int getMentalHealth() {
        return mentalHealth;
    }

    public int getStress() {
        return stress;
    }

    public int getPhysicalHealth() {
        return physicalHealth;
    }

    public int getSleep() {
        return  sleep;
    }

    public int getCommunity() {
        return community;
    }

    public void updateStress(int update) {
        stress = update;
    }

    public void updateMentalHealth(int update) {
        mentalHealth = update;
    }

    public void updateName(String update) {
        name = update;
    }

    public void updateUsername(String update) {
        username = update;
    }

    public void updatePassword(String update) {
        password = update;
    }

    public void updatePhysicalHealth(int update) {
        physicalHealth = update;
    }

    public void updateSleep(int update) {
        sleep = update;
    }

    public void updateCommunity(int update) {
        community = update;
    }

    public void updatePersonalRecs(List<Resource> personalRecs) { this.personalRecs = personalRecs; }

    public void updateAccountNum(int update) {
        accountNum = update;
    }
}
