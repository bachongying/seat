package com.bacy.seat.ui;

import com.bacy.seat.util.Util;
import com.bacy.seat.util.heimu.AbstractHeiMu;
import com.bacy.seat.util.heimu.HeiMuManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class HeiMuManagerUI {
    public static JLabel current = null;
    public static Map<JLabel,AbstractHeiMu> map = new HashMap<>();
    public static void showUI(){
        Util.log("Loading HeiMuManagerUI...");
        JFrame frame = new JFrame("黑幕管理");
        frame.setLayout(null);
        frame.setSize(600+15,800+30+40);
        frame.setResizable(false);
        Util.centerFrame(frame);

        JPanel panel = new JPanel();
        //panel.setLayout(new GridLayout(1,5,200,20));
        //panel.setLayout(null);
        JScrollPane scrollPane = new JScrollPane(panel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(0,0,600,800);
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(600,load(panel)+20));

        current=null;
        JButton button = new JButton("删除");
        button.setBounds(0,800,120,30);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(current!=null){
                    AbstractHeiMu heiMu = map.get(current);
                    if(heiMu!=null){
                        HeiMuManager.removeHeiMu(heiMu);
                        load(panel);
                    }
                }
            }
        });
        JButton button1 = new JButton("编辑");
        button1.setBounds(120,800,120,30);
        button1.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(current!=null){
                    AbstractHeiMu heiMu = map.get(current);
                    if(heiMu!=null){
                        try {
                            String var = heiMu.getJson().toString();
                            var = JOptionPane.showInputDialog("输入",heiMu.getJson());
                            JsonObject object = new Gson().fromJson(var,JsonObject.class);
                            heiMu.readData(object);
                            load(panel);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null,"参数错误","ERROR",JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        JButton button2 = new JButton("添加");
        button2.setBounds(240,800,120,30);
        button2.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Class<? extends AbstractHeiMu> aClass = (Class<? extends AbstractHeiMu>)HeiMuManager.classes[JOptionPane.showOptionDialog(null,"选择类型","选择",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,HeiMuManager.classes,HeiMuManager.classes[0])];
                try {
                    AbstractHeiMu heiMu = aClass.newInstance();
                    String var = JOptionPane.showInputDialog("输入",heiMu.getDefaultJson());
                    Gson gson = new Gson();
                    JsonObject object = gson.fromJson(var,JsonObject.class);
                    heiMu.readData(object);
                    if(heiMu.isAvailable()){
                        HeiMuManager.addHeiMu(heiMu);
                        load(panel);
                    }else{
                        JOptionPane.showMessageDialog(null,"参数错误","ERROR",JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(null,"参数错误","ERROR",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JButton button3 = new JButton("重加载");
        button3.setBounds(360,800,120,30);
        button3.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    HeiMuManager.initHeiMu();
                }catch (Exception ee){}
                load(panel);
                JOptionPane.showMessageDialog(null,"成功");
            }
        });
        JButton button4 = new JButton("保存");
        button4.setBounds(480,800,120,30);
        button4.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    HeiMuManager.saveHeiMu();
                    JOptionPane.showMessageDialog(null,"成功");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    ex.printStackTrace(pw);
                    JOptionPane.showMessageDialog(null,"保存失败\r\n原因:"+sw.toString(),"ERROR",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        frame.add(button);
        frame.add(button1);
        frame.add(button2);
        frame.add(button3);
        frame.add(button4);

        frame.add(scrollPane);
        frame.setVisible(true);
    }

    public static int load(JPanel panel){
        panel.removeAll();
        map = new HashMap<>();
        int var = 0;
        for(AbstractHeiMu heiMu: HeiMuManager.heiMus){
            JLabel label = new JLabel(heiMu.getJson().toString());
            label.setBounds(0,20*var,600,20);
            label.setOpaque(true);
            label.setBackground(Color.white);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(current!=null){
                        current.setBackground(Color.white);
                    }
                    if(current==label){
                        current.setBackground(Color.white);
                        current=null;
                    }else {
                        current=label;
                        current.setBackground(Color.gray);
                    }
                }
            });
            panel.add(label);
            map.put(label,heiMu);
            var++;
        }
        panel.repaint();
        return var;
    }
}
