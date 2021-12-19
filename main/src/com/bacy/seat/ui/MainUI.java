package com.bacy.seat.ui;

import com.bacy.seat.RandomSeat;
import com.bacy.seat.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MainUI {
    public static JFrame frame;
    private static JCheckBox box,box1,box2;
    public static void showUI(){
        Util.log("Loading MainUI...");
        frame = new JFrame("座位抽取机 " + RandomSeat.ver);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setSize(300,200);
        Util.centerFrame(frame);

        JPanel panel = new JPanel();
        panel.setBounds(0,0,280,100);
        //panel.setLayout(null);

        JLabel label = new JLabel("设置:");
        panel.add(label);
        box = new JCheckBox("按照男女比例抽取",true);
        panel.add(box);
        box1 = new JCheckBox("按照成绩抽取",true);
        panel.add(box1);
        box2 = new JCheckBox("保存成绩");
        panel.add(box2);
        frame.add(panel);

        JPanel panel1 = new JPanel();
        panel1.setBounds(0,100,280,55);
        panel1.setLayout(null);
        JButton button = new JButton("开始抽取");
        button.setBounds(120,RandomSeat.debug?0:30,160,25);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try{
                    //frame.dispose();
                    ArrangeUI.showNormalUI(box.isSelected(),box1.isSelected(),box2.isSelected());
                }catch (Exception e){
                    e.printStackTrace();
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    JOptionPane.showMessageDialog(null,"错误\r\n错误原因:"+sw.toString(),"ERROR",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel1.add(button);
        button = new JButton("调试工具");
        button.setBounds(120,30,160,25);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                test();
            }
        });
        if(RandomSeat.debug) panel1.add(button);
        JLabel label1 = new JLabel("Made by 勿忘落樱");
        label1.setBounds(10,30,280,25);
        panel1.add(label1);
        label1 = new JLabel("DEBUG MODE");
        label1.setBounds(10,5,280,25);
        if(RandomSeat.debug) panel1.add(label1);
        frame.add(panel1);

        frame.setVisible(true);
    }

    public static void reloadUI(){
        frame.dispose();
        MainUI.showUI();
    }

    public static void test(){
        int var = JOptionPane.showOptionDialog(null,"选择需要的工具,可以使用命令,具体请输入help查看","select",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,new String[]{"测试稳定性/速度","测试预览界面","重新加载主界面","重新加载软件","黑幕管理界面"},"测试稳定性/速度");
        switch (var){
            case 0:
                ArrangeUI.debug(box.isSelected(),box1.isSelected(),box2.isSelected());
                break;
            case 1:
                PreviewUI.showUI(false,false,false);
                break;
            case 4:
                HeiMuManagerUI.showUI();
                break;
            case 3:
                frame.dispose();
                LoadingUI.showUI();
                break;
            case 2:
                reloadUI();
                break;
        }
    }
}
