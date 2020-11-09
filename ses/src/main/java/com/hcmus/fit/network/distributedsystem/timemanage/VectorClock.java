package com.hcmus.fit.network.distributedsystem.timemanage;

import java.util.Arrays;

public class VectorClock {
    public static boolean compareLessThan(int[] timeStamp1, int[] timeStamp2){
        for(int i = 0; i < timeStamp1.length; i++){
            if(timeStamp1[i] >= timeStamp2[i]){
                return false;
            }
        }
        return true;
    }

    public static boolean compareLessThanEqualTo(int[] timeStamp1, int[] timeStamp2){
        for(int i =0; i < timeStamp1.length; i++){
            if(timeStamp1[i] > timeStamp2[i]){
                return false;
            }
        }
        return true;
    }

    public static int[] getMax(int[] timeStamp1, int[] timeStamp2){
        int[] maxArray = timeStamp1;
        for(int i = 0; i < timeStamp1.length; i++){
            if(timeStamp1[i] < timeStamp2[i]){
                maxArray[i] = timeStamp2[i];
            }
        }
        return maxArray;
    }

    public static String toString(TimeStamp vectorClock)
    {
        return Arrays.toString(vectorClock.getTimestampProcess());

    }
}
