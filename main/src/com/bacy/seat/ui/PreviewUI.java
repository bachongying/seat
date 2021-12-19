package com.bacy.seat.ui;

import com.bacy.seat.group.Seat;
import com.bacy.seat.group.SeatGroup;
import com.bacy.seat.group.SeatGroupManager;
import com.bacy.seat.people.Person;
import com.bacy.seat.util.Util;
import org.apache.poi.xssf.usermodel.XSSFCell;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PreviewUI {
    private static JLabel currentLabel1;
    private static JLabel currentLabel2;
    private static Map<JLabel,Seat> map;
    public static void showUI(boolean var1,boolean var2,boolean var3){
        Util.log("Loading PreviewUI...");
        JFrame frame = new JFrame("预览");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        //frame.setResizable(false);
        //frame.setSize(800+15,560+20+40);//40上边界

        JPanel panel = new JPanel();
        //panel.setLayout(new GridLayout(1,5,200,20));
        panel.setLayout(null);
        JScrollPane scrollPane = new JScrollPane(panel,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

//        for(int var = 0;var<=100;var++){
//            JLabel label = new JLabel(var+""+var+""+var+""+var+""+var);
//            label.setBounds(0,var*20,100,20);
//            panel.add(label);
//        }
//
//        panel.setPreferredSize(new Dimension(1000,20*100));

        int maxX = 0;
        int maxY = 0;
        currentLabel1=currentLabel2=null;
        map = new HashMap<>();
        for(SeatGroup seatGroup: SeatGroupManager.getGroups()){
            for(Seat seat: seatGroup.getSeats()){
                Person person = seat.getPerson();
                String name = Util.getValue(seat.getCell());
//                if(person==null){
//                    name = "null";
//                }else {
//                    name=person.getName();
//                }
                int row = seat.getCell().getRowIndex();
                int col = seat.getCell().getColumnIndex();
                maxX = Math.max(maxX,col*75);
                maxY = Math.max(maxY,row*20);
                JLabel label = new JLabel(name);
                if(person!=null){
                    if(person.isMale()){
                        label.setForeground(Color.blue);
                    }else{
                        label.setForeground(Color.black);
                    }
                }
                label.setBounds(col*75,row*20,75,20);
                label.setOpaque(true);
                label.setBackground(Color.white);
                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        //System.out.println("Click "+person);
                        JLabel clickLabel = label;
                        if(clickLabel == currentLabel1){
                            //如果等于当前1的,就把1重置了
                            currentLabel1.setBackground(Color.white);
                            currentLabel1=null;
                            //如果当前2存在,设置为当前1
                            if(currentLabel2!=null){
                                currentLabel1=currentLabel2;
                                currentLabel2=null;
                            }
                        }else if(clickLabel == currentLabel2){
                            //如果等于当前2,就交换
//                            currentLabel2.setBackground(Color.white);
//                            currentLabel2=null;
                            Seat seat1 = map.get(currentLabel1);
                            Seat seat2 = map.get(currentLabel2);
                            Util.log("交换"+seat1+"<->"+seat2);
                            Person person1 = seat1.getPerson();
                            Person person2 = seat2.getPerson();
                            seat2.setPerson(person1);
                            seat1.setPerson(person2);
                            //交换人
                            XSSFCell cell1 = seat1.getCell();
                            XSSFCell cell2 = seat2.getCell();
                            String cell1s = Util.getValue(cell1);
                            String cell2s = Util.getValue(cell2);
                            cell1.setCellValue(cell2s);
                            cell2.setCellValue(cell1s);
                            //交换单元格内容

                            //设置人
                            Person person = seat1.getPerson();
                            String name = cell2s;
//                            if(person==null){
//                                name = "null";
//                            }else {
//                                name=person.getName();
//                            }
                            currentLabel1.setText(name);
                            if(person!=null){
                                if(person.isMale()){
                                    currentLabel1.setForeground(Color.blue);
                                }else{
                                    currentLabel1.setForeground(Color.black);
                                }
                            }
                            person = seat2.getPerson();
                            name = cell1s;
//                            if(person==null){
//                                name = "null";
//                            }else {
//                                name=person.getName();
//                            }
                            currentLabel2.setText(name);
                            if(person!=null){
                                if(person.isMale()){
                                    currentLabel2.setForeground(Color.blue);
                                }else{
                                    currentLabel2.setForeground(Color.black);
                                }
                            }

                            currentLabel1.setBackground(Color.white);
                            currentLabel2.setBackground(Color.white);
                            currentLabel1=currentLabel2=null;
                        }else{
                            //如果当前1不存在就设置为当前1
                            if(currentLabel1==null){
                                currentLabel1=clickLabel;
                                currentLabel1.setBackground(Color.gray);
                            }else{
                                if(currentLabel2!=null){
                                    currentLabel2.setBackground(Color.white);
                                }
                                currentLabel2=clickLabel;
                                currentLabel2.setBackground(Color.gray);
                            }
                        }
                    }
                });
                panel.add(label);
                map.put(label,seat);
            }
        }
        panel.setPreferredSize(new Dimension(maxX+75,maxY+20));
        frame.add(scrollPane);

        JButton button1 = new JButton("保存");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ee){
                try{
                    SimpleDateFormat df = new SimpleDateFormat("HH-mm-ss-SSS");
                    Date date = new Date();
                    String fileName;
                    FileOutputStream outputStream = new FileOutputStream(fileName="座位表" + "[" + df.format(date) + "]" + ".xlsx");
                    Util.workbook.write(outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Util.log("保存完成");
                    //this.setValue(8,1,bar2Max,"完成...","...",5);
                    JOptionPane.showMessageDialog(null,"已保存至\'"+fileName+"\'","成功",JOptionPane.INFORMATION_MESSAGE);
                }catch (Exception e){
                    e.printStackTrace();
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    JOptionPane.showMessageDialog(null,"错误\r\n错误原因:"+sw.toString(),"ERROR",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        JButton button2 = new JButton("下一个");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ee) {
                try{
                    //frame.dispose();
                    frame.dispose();
                    ArrangeUI.showNormalUI(var1,var2,var3);
                }catch (Exception e){
                    e.printStackTrace();
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    JOptionPane.showMessageDialog(null,"错误\r\n错误原因:"+sw.toString(),"ERROR",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        frame.add(button1);
        frame.add(button2);

        frame.repaint();
        //frame.pack();

        frame.setSize(Math.min(1500,maxX+75+15+20),Math.min(1000,maxY+20+30+40+20));
        Util.centerFrame(frame);
        frame.setMinimumSize(new Dimension(415,100));
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                button1.setBounds(0,frame.getHeight()-40-30,200,30);
                button2.setBounds(frame.getWidth()-15-200,frame.getHeight()-40-30,200,30);
                scrollPane.setBounds(0,0,frame.getWidth()-15,frame.getHeight()-40-30);
            }
        });

        frame.setVisible(true);
    }
}
