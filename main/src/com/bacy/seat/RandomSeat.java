package com.bacy.seat;

import com.bacy.seat.ui.LoadingUI;
import com.bacy.seat.util.Util;

public class RandomSeat {
    public static String ver = "1.1";
    public static int verId = 2;
    public static boolean debug = false;
    public static boolean showLog = true;
    public static void main(String[] args) {
        for(String s:args) {
            if (s.equalsIgnoreCase("getVersion")) {
                System.out.println(verId);
                System.exit(0);
            }
            if (s.equalsIgnoreCase("debug")) {
                debug = true;
                Util.log("You are now in the DEBUG mode");
            }
            if (s.equalsIgnoreCase("nolog")) {
                Util.log("There is not log ever");
                showLog = false;
            }
        }
        Util.log("HELLO WORLD");
        LoadingUI.showUI();
    }
}
