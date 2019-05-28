package com.example.personalwellness;
import java.util.ArrayList;
import java.util.List;

public class Proc {
    private ResourceDB db;

    public Proc(ResourceDB db){
        this.db = db;
    }

    public int getRecs (CurrentUser user) {
        List<Resource> recs = new ArrayList<Resource>();
        //0 : sc, 1 : mh, 2 : ph, 3 : d, 4 : sl, 5 : st
        int[] scores = new int[6];
        scores[0] = user.getMentalHealth();
        scores[1] = user.getStress();
        scores[2] = user.getPhysicalHealth();
        scores[3] = user.getCommunity();
        scores[4] = user.getSleep();
        int j = getMax(scores);
        return j;
    }

    private int getMax (int[] scores) {
        int maxCategory = 0;
        int maxInt = 0;
        //0 : sc, 1 : mh, 2 : ph, 3 : d, 4 : sl, 5 : st
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > maxInt) {
                maxInt = scores[i];
                maxCategory = i;
            }
        }
        return maxCategory;
    }

}
