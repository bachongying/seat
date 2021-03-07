package com.bacy.seat.ui;

import com.bacy.seat.model.Model;
import com.bacy.seat.people.People;
import com.bacy.seat.util.Util;

import javax.swing.*;
import java.awt.*;

public class LoadingUI {
    public static void showUI(){
        Util.log("初始化加载界面...");
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
        JLabel label = new JLabel("检测更新中...");
        label.setBounds(10,10,180,20);
        frame.add(label);
        JProgressBar bar = new JProgressBar(0,3);
        bar.setBounds(10,40,180,20);
        frame.add(bar);
        frame.setVisible(true);
        try{
            Util.checkUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
        label.setText("加载名单...");
        bar.setValue(1);
        frame.repaint();
        try {
            People.loadPeople();
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"错误,请检查\'名单.xlsx\'是否正确配置\r\n错误原因:"+e.toString(),"ERROR",JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        label.setText("加载座位模板...");
        bar.setValue(2);
        frame.repaint();
        try {
            Model.loadModel();
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"错误,请检查\'座位模板.xlsx\'是否正确配置\r\n错误原因:"+e.toString(),"ERROR",JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        Util.log("加载完成");
        label.setText("完成...");
        bar.setValue(3);
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
