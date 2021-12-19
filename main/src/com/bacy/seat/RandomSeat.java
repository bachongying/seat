package com.bacy.seat;

import com.bacy.seat.ui.HeiMuManagerUI;
import com.bacy.seat.ui.LoadingUI;
import com.bacy.seat.ui.MainUI;
import com.bacy.seat.ui.PreviewUI;
import com.bacy.seat.util.Util;

import javax.swing.*;
import java.util.Scanner;

public class RandomSeat {
    public static String ver = "1.3";
    public static int verId = 4;
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
                Util.log("切换到调试模式");
            }
            if (s.equalsIgnoreCase("nolog")) {
                Util.log("取消日志输出");
                showLog = false;
            }
        }
        new Thread(){
            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                while (true){
                    String s = scanner.nextLine();
                    Util.log("收到信息->"+s);
                    onReceiveMessage(s);
                }
            }
        }.start();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Util.log("设置界面主题成功");
        } catch (Exception e) {
            Util.log("设置界面主题失败");
            e.printStackTrace();
        }
        LoadingUI.showUI();
        //PreviewUI.showUI();
    }
    public static void onReceiveMessage(String message){
        if(message.equalsIgnoreCase("ShowHeiMu")){
            Util.log("显示黑幕管理");
            HeiMuManagerUI.showUI();
        }else if(message.equalsIgnoreCase("Test")){
            Util.log("显示测试内容");
            MainUI.test();
        }else if(message.equalsIgnoreCase("ReloadUI")){
            Util.log("重新加载UI");
            MainUI.reloadUI();
        }else if(message.equalsIgnoreCase("Reload")){
            Util.log("重新加载软件");
            MainUI.frame.dispose();
            LoadingUI.showUI();
        }else if(message.equalsIgnoreCase("help")){
            Util.log("ReloadUI重新加载UI\r\nReload重新加载软件\r\nTest显示测试内容\r\nShowHeiMu显示黑幕管理");
        }else{
            Util.log("不合法命令");
        }
    }
}
