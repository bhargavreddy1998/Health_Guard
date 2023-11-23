package com.example.myloginapp;

import java.util.List;

public class RespRateCode {

    public int callRespiratoryCalculator(List xcoordinates,List ycoordinates,List zcoordinates) {
        float previousValue = 0f;
        float currentValue = 0f;
        previousValue = 10f;
        int k = 0;

        for (int i = 0; i < 1280; i++) {
            currentValue = (float) Math.sqrt(Math.pow((Double) xcoordinates.get(i), 2.0))+
                    (float) Math.sqrt(Math.pow((Double) ycoordinates.get(i), 2.0))+
                    (float) Math.sqrt(Math.pow((Double) zcoordinates.get(i), 2.0));;

            if (Math.abs(previousValue - currentValue) > 0.15) {
                k++;
            }

            previousValue = currentValue;
        }

        double ret = (double) k / 45.00;

        return (int) (ret * 30);
    }

}
