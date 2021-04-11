package com.bacy.seat;

import com.bacy.seat.ui.LoadingUI;
import com.bacy.seat.util.Util;

public class RandomSeat {
    public static String ver = "1.0";
    public static int verId = 1;
    public static void main(String[] args) {
        for(String s:args){
            if(s.equalsIgnoreCase("getVersion")){
                System.out.println(verId);
                System.exit(0);
            }
        }
        Util.log("HELLO WORLD");
        LoadingUI.showUI();
    }
}
