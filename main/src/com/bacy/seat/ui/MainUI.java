package com.bacy.seat.ui;

import com.bacy.seat.RandomSeat;
import com.bacy.seat.model.Group;
import com.bacy.seat.model.Model;
import com.bacy.seat.model.Seat;
import com.bacy.seat.people.People;
import com.bacy.seat.people.Person;
import com.bacy.seat.util.HeiMu;
import com.bacy.seat.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

public class MainUI {
    public static void showUI(){
        Util.log("初始化主界面...");
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
        button.setBounds(170,0,110,25);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                try{
                    start(box.isSelected(),box1.isSelected(),box2.isSelected());
                }catch (Exception e){
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,"错误\r\n错误原因:"+e.toString(),"ERROR",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel1.add(button);
        JLabel label1 = new JLabel("Made by 勿忘落樱");
        label1.setBounds(10,30,280,25);
        panel1.add(label1);
        frame.add(panel1);

        frame.setVisible(true);
    }

    public static void start(boolean sex,boolean mark,boolean savemark) throws Exception{
        Util.log("初始化抽取");
        Random random = new Random(System.currentTimeMillis());
        for(Group group:Model.groups){
            for(Seat seat:group.getSeats()){
                seat.setPerson(null,false);
            }
        }
        for(Person person: People.people){
            person.setSeat(null,false);
        }
        HeiMu.setPeopleWithHeiMu(random);
        Util.log("开始抽取");
        if(sex){
            Util.log("正在分类性别");
            ArrayList<Person> male = new ArrayList<>();
            ArrayList<Person> female = new ArrayList<>();
            for(Person person: People.people){
                if(person.isMale()){
                    male.add(person);
                }else {
                    female.add(person);
                }
            }
            ArrayList<Person> more;
            ArrayList<Person> fewer;
            boolean fewerSexIsMale = false;
            if(female.size()>male.size()){
                fewer=male;
                fewerSexIsMale=true;
                more=female;
            }else{
                fewer=female;
                fewerSexIsMale=false;
                more=male;
            }
            float allleft=fewer.size()+more.size();
            float fewerleft=fewer.size();
            Collections.shuffle(Model.groups);
            for(Group group:Model.groups){
                int count = Math.round(fewerleft/allleft*group.getSeats().size());
                group.setFewerSexCount(count);
                allleft-=group.getSeats().size();
                fewerleft-=count;
            }
            int count = 0;
            boolean front = true;
            Iterator<Person> iterator = fewer.iterator();
            while (iterator.hasNext()){
                if(iterator.next().hasHeiMu())iterator.remove();
            }
            iterator = more.iterator();
            while (iterator.hasNext()){
                if(iterator.next().hasHeiMu())iterator.remove();
            }
            if(!mark){
                Collections.shuffle(fewer);
                Collections.shuffle(more);
            }
            while (fewer.size()>0){
                Collections.shuffle(Model.groups);
                for(Group group:Model.groups){
                    if(group.needFewerSex(fewerSexIsMale)&&fewer.size()>0){
                        Person person = front?fewer.get(0):fewer.get(fewer.size()-1);
                        Util.log(person.toString()+"分配至"+group+"-"+group.putPerson(person,true,random,mark));
                        fewer.remove(person);
                    }
                }
                front=!front;
                count++;
                if(count>100)throw new Exception("请重试");
            }
            count=0;
            front=true;
            while (more.size()>0){
                Collections.shuffle(Model.groups);
                for(Group group:Model.groups){
                    if(group.getUnUsedSeats().size()>0 && more.size()>0){
                        Person person = front?more.get(0):more.get(more.size()-1);
                        Util.log(person.toString()+"分配至"+group);
                        if(group.putPerson(person,false,random,mark)){
                            more.remove(person);
                            Util.log("成功");
                        }
                    }
                }
                front=!front;
                count++;
                if(count>100)throw new Exception("请重试");
            }
        }else{
            ArrayList<Person> people = new ArrayList<>();
            people.addAll(People.people);
            Iterator<Person> iterator = people.iterator();
            while (iterator.hasNext()) {
                if(iterator.next().hasHeiMu())iterator.remove();
            }
            int count=0;
            boolean front=true;
            while (people.size()>0){
                Collections.shuffle(Model.groups);
                for(Group group:Model.groups){
                    if(group.getUnUsedSeats().size()>0 && people.size()>0){
                        Person person = front?people.get(0):people.get(people.size()-1);
                        Util.log(person.toString()+"分配至"+group);
                        if(group.putPerson(person,false,random,mark)){
                            people.remove(person);
                            Util.log("成功");
                        }
                    }
                }
                front=!front;
                count++;
                if(count>100)throw new Exception("请重试");
            }
        }
        for(Group group:Model.groups){
            for(Seat seat:group.getSeats()){
                seat.getCell().setCellValue((savemark?seat.getPerson().getCount():"")+seat.getPerson().getName());
            }
        }
        FileOutputStream outputStream = new FileOutputStream("座位表.xlsx");
        Model.workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
        Util.log("成功");
        JOptionPane.showMessageDialog(null,"成功抽取,已保存至'座位表.xlsx'","SUCCESS",JOptionPane.INFORMATION_MESSAGE);
    }
}
