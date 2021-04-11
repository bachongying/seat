package com.bacy.seat.ui;

import com.bacy.seat.group.SeatGroupManager;
import com.bacy.seat.people.PeopleManager;
import com.bacy.seat.util.Util;
import com.bacy.seat.util.heimu.HeiMuManager;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class LoadingUI {
    public static void showUI(){
        Util.log("Loading LoadingUI...");
        JFrame frame = new JFrame("Loading");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setSize(220,120);
        int windowWidth = frame.getWidth();
        int windowHeight = frame.getHeight();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        frame.setLocation(screenWidth/2-windowWidth/2, screenHeight/2-windowHeight/2);
        JLabel label = new JLabel("加载名单...");
        label.setBounds(10,10,180,20);
        frame.add(label);
        JProgressBar bar = new JProgressBar(0,2);
        bar.setBounds(10,40,180,20);
        frame.add(bar);
        frame.setVisible(true);

        Util.log("Loading people");
        label.setText("加载名单...");
        bar.setValue(0);
        frame.repaint();
        try {
            PeopleManager.initPeople();
        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            JOptionPane.showMessageDialog(null,"错误,请检查\'名单.xlsx\'是否正确配置\r\n错误原因:"+sw.toString(),"ERROR",JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        try {
            HeiMuManager.initHeiMu();
        }catch (Exception e){
            e.printStackTrace();
        }

        Util.log("Loading seat");
        label.setText("加载座位模板...");
        bar.setValue(1);
        frame.repaint();
        try {
            SeatGroupManager.initSeatGroup();
        }catch (Exception e){
            e.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            JOptionPane.showMessageDialog(null,"错误,请检查\'座位模板.xlsx\'是否正确配置\r\n错误原因:"+sw.toString(),"ERROR",JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        Util.log("Complete");
        label.setText("完成...");
        bar.setValue(2);
        frame.repaint();
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        frame.dispose();
        MainUI.showUI();
    }
}
