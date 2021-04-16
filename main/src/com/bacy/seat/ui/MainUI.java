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
    public static void showUI(){
        Util.log("Loading MainUI...");
        JFrame frame = new JFrame("座位抽取机 " + RandomSeat.ver);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setSize(300,200);
        int windowWidth = frame.getWidth();
        int windowHeight = frame.getHeight();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        frame.setLocation(screenWidth/2-windowWidth/2, screenHeight/2-windowHeight/2);

        JPanel panel = new JPanel();
        panel.setBounds(0,0,280,100);
        //panel.setLayout(null);

        JLabel label = new JLabel("设置:");
        panel.add(label);
        JCheckBox box = new JCheckBox("按照男女比例抽取",true);
        panel.add(box);
        JCheckBox box1 = new JCheckBox("按照成绩抽取",true);
        panel.add(box1);
        JCheckBox box2 = new JCheckBox("保存成绩");
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
        button = new JButton("test");
        button.setBounds(120,30,80,25);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                ArrangeUI.debug(box.isSelected(),box1.isSelected(),box2.isSelected());
            }
        });
        if(RandomSeat.debug) panel1.add(button);
        button = new JButton("reload");
        button.setBounds(200,30,80,25);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                frame.dispose();
                MainUI.showUI();
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
}
