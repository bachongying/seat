package com.bacy.seat.ui;

import com.bacy.seat.group.MarkGroup;
import com.bacy.seat.group.SeatGroup;
import com.bacy.seat.group.SeatGroupManager;
import com.bacy.seat.people.PeopleManager;
import com.bacy.seat.people.Person;
import com.bacy.seat.util.Util;
import com.bacy.seat.util.heimu.HeiMuManager;

import javax.swing.*;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class ArrangeUI {
    public static void showNormalUI(boolean sex,boolean mark,boolean saveCount)throws Exception{
        Util.log("Loading ArrangeUI...");
        JFrame frame = new JFrame("Arranging...");
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setSize(300,180);
        int windowWidth = frame.getWidth();
        int windowHeight = frame.getHeight();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        frame.setLocation(screenWidth/2-windowWidth/2, screenHeight/2-windowHeight/2);
        JLabel label = new JLabel("准备中...");
        label.setBounds(10,10,260,20);
        frame.add(label);
        JProgressBar bar = new JProgressBar(0,8);//7+1
        bar.setBounds(10,40,260,20);
        frame.add(bar);
        JLabel label1 = new JLabel("等待任务中...");
        label1.setBounds(10,70,260,20);
        frame.add(label1);
        JProgressBar bar1 = new JProgressBar(0,1);
        bar1.setBounds(10,100,260,20);
        frame.add(bar1);

        ArrangeThread thread = new ArrangeThread(sex,mark,false,saveCount);
        thread.start();
        new Thread(){
            @Override
            public void run() {
                while (thread.isAlive()){
                    bar1.setMaximum(thread.bar2Max);
                    bar.setValue(thread.bar1);
                    bar1.setValue(thread.bar2);
                    label.setText(thread.lable1);
                    label1.setText(thread.lable2);
                    frame.repaint();
                }
                if(thread.e!=null){
                    Exception e = thread.e;
                    e.printStackTrace();
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    JOptionPane.showMessageDialog(null,"错误\r\n错误原因:"+sw.toString(),"ERROR",JOptionPane.ERROR_MESSAGE);
                }
                frame.dispose();
            }
        }.start();
        frame.repaint();
        frame.setVisible(true);
    }

    public static void debug(boolean sex,boolean mark,boolean saveCount){
        Util.log("Loading...");
        JFrame frame = new JFrame("debug");
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
        JLabel label = new JLabel("0/0");
        label.setBounds(10,10,180,20);
        frame.add(label);
        JProgressBar bar = new JProgressBar(0,1);
        bar.setBounds(10,40,180,20);
        frame.add(bar);
        frame.setVisible(true);

        int times = 1000;
        bar.setMaximum(times);
        long time = System.currentTimeMillis();
        DebugThread thread = new DebugThread(times,sex,mark,saveCount);
        Util.showLog=false;
        thread.start();
        new Thread(){
            @Override
            public void run() {
                while (thread.isAlive()){
                    bar.setValue(thread.count);
                    label.setText(thread.count+"/"+times);
                    frame.repaint();
                }
                JOptionPane.showMessageDialog(null,"错误次数:"+thread.errorTimes+"/10000\r\n时间:"+(System.currentTimeMillis()-time),"ERROR",JOptionPane.ERROR_MESSAGE);
                frame.dispose();
                Util.showLog=true;
            }
        }.start();
    }

    public static class DebugThread extends Thread{
        public int errorTimes;
        public int count;
        private int max;
        private boolean sex;
        private boolean mark;
        private boolean saveCount;
        public DebugThread(int max, boolean sex, boolean mark, boolean saveCount) {
            this.max = max;
            this.sex = sex;
            this.mark = mark;
            this.saveCount = saveCount;
        }

        @Override
        public void run() {
            for(int a=0;a<max;a++){
                count=a;
                ArrangeThread thread = new ArrangeThread(sex,mark,true,saveCount);
                thread.start();
                while (thread.isAlive()){
                }
                if(thread.e!=null)errorTimes++;
            }
        }
    }

    public static class ArrangeThread extends Thread{
        private boolean sex;
        private boolean mark;
        private boolean debug;
        private boolean saveCount;
        public int bar1,bar2;
        public int bar2Max;
        public String lable1,lable2;
        public Exception e;
        public ArrangeThread(boolean sex, boolean mark, boolean debug, boolean saveCount){
            this.sex = sex;
            this.mark = mark;
            this.debug = debug;
            this.saveCount = saveCount;
        }
        public void setValue(int bar1,int bar2,int bar2Max,String lable1,String lable2,int delay){
            if(!debug){
                this.bar1 = bar1;
                this.bar2 = bar2;
                this.bar2Max = bar2Max;
                this.lable1 = lable1;
                this.lable2 = lable2;
                try {
                    Thread.sleep(delay);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        @Override
        public void run(){
            try{
                //reset
                Util.log("Reset");
                this.setValue(1,bar2,bar2Max,"重置数据...",lable2,50);
                Random random = new Random();
                this.setValue(bar1,0,SeatGroupManager.getGroups().size(),lable1,"重置座位组...",5);
                for(SeatGroup seatGroup: SeatGroupManager.getGroups()) {
                    seatGroup.reset();
                    this.setValue(bar1,bar2+1,bar2Max,lable1,lable2,5);
                }
                this.setValue(bar1,0,PeopleManager.getPeople().size(),lable1,"重置人...",5);
                for(Person person:PeopleManager.getPeople()) {
                    person.setSeat(null);
                    person.setGroup(null);
                    this.setValue(bar1,bar2+1,bar2Max,lable1,lable2,2);
                }

                //先分配成绩组
                Util.log("Arranging markGroup");
                this.setValue(2,0,1,"初始化成绩组...", "...",50);
                ArrayList<MarkGroup> markGroups = new ArrayList<>();
                boolean flag = true;
                int count = 0;
                while (flag) {
                    flag = false;
                    ArrayList<SeatGroup> accessableGroup = new ArrayList<>();
                    for (SeatGroup seatGroup : SeatGroupManager.getGroups()) {
                        if (seatGroup.getCount() > 0) {
                            accessableGroup.add(seatGroup);
                            seatGroup.setCount(seatGroup.getCount() - 1);
                            if (seatGroup.getCount() > 0) flag = true;
                        }
                    }
                    if (accessableGroup.size() > 0) {
                        markGroups.add(new MarkGroup(count, accessableGroup.size(), accessableGroup));
                        Util.log("New markGroup(count=" + count + ", accessableGroup=" + accessableGroup + ")");
                    }
                    count++;
                }

                //打乱数组
                Util.log("Shuffle List");
                this.setValue(3,bar2,bar2Max,"初始化分配成绩组...","打乱中...",100);
                markGroups = Util.ShuffleList(markGroups);

                //安排各组人
                Util.log("Arranging markGroup's people");
                this.setValue(4,bar2,markGroups.size(),"分配各个人到成绩组...","...(0/"+markGroups.size()+")",50);
                if(mark) {
                    Iterator<MarkGroup> iterator = markGroups.iterator();
                    MarkGroup markGroup = iterator.next();
                    for (Person person : PeopleManager.getPeople()) {
                        if (markGroup.getSize() <= markGroup.getPeople().size()) {
                            markGroup = iterator.next();
                        }
                        markGroup.addPerson(person);
                        person.setMarkGroup(markGroup);
                    }
                    this.setValue(bar1,bar2+1,bar2Max,lable1,"...("+(bar2+1)+"/"+bar2Max+")",10);
                }
                //安排黑幕
                HeiMuManager.arrangeHeiMu(mark);

                Util.log("Start arranging");
                this.setValue(5,0,1,"分配各个人到座位组...",lable2,50);
                if(sex && mark) {
                    //先排人少的组，从这个组的成绩组中拿人
                    this.setValue(bar1,bar2,bar2Max,lable1,"打乱数组",20);
                    ArrayList<SeatGroup> groups = SeatGroupManager.getGroups();
                    groups = Util.ShuffleList(groups);
            /*groups.sort(new Comparator<SeatGroup>() {
                @Override
                public int compare(SeatGroup o1, SeatGroup o2) {
                    return o1.getEmptySeatSize()-o2.getEmptySeatSize();
                }
            });*/
                    this.setValue(bar1,bar2,PeopleManager.getPeople().size(),lable1,"计算男女人数",5);
                    int peopleLeft = 0;
                    int maleLeft = 0;
                    for (Person person : PeopleManager.getPeople()) {
                        if (person.getGroup() != null) continue;//可能需要算上有黑幕的
                        peopleLeft++;
                        if (person.isMale()) {
                            maleLeft++;
                        }
                        this.setValue(bar1,bar2+1,bar2Max,lable1,lable2,1);
                    }
                    this.setValue(bar1,0,groups.size(),lable1,"分配各座位组的人...(0/"+groups.size()+")",5);
                    for (SeatGroup group : groups) {
                        ArrayList<MarkGroup> accessibleGroup = new ArrayList<>();
                        for (MarkGroup markGroup : markGroups) {
                            if (markGroup.getAccessableGroup().contains(group)) accessibleGroup.add(markGroup);
                        }
                        //获取大约男性别人数
                        int maleRange = Math.round((float) maleLeft / (float) peopleLeft * (float) group.getEmptySeatSize());
                        int maleUse = 0;
                        Util.log(group+" may has " + maleRange + " male people");
                        //先拿只有一个性别的组
                        Iterator<MarkGroup> iterator = accessibleGroup.iterator();
                        while (iterator.hasNext()) {
                            MarkGroup group1 = iterator.next();
                            if (group1.getMale().size() == 0) {
                                //只剩女
                                Person person = Util.getRandomElement(group1.getFemale(), random);
                                group.addPerson(person);
                                person.setGroup(group);
                                group1.removeAccessableGroup(group);
                                group1.removePerson(person);
                                iterator.remove();
                                peopleLeft--;
                            } else if (group1.getFemale().size() == 0) {
                                //只剩男
                                Person person = Util.getRandomElement(group1.getMale(), random);
                                group.addPerson(person);
                                person.setGroup(group);
                                group1.removeAccessableGroup(group);
                                group1.removePerson(person);
                                iterator.remove();
                                maleLeft--;
                                peopleLeft--;
                                maleUse++;
                            }
                        }
                        //性别不强制要求
                        accessibleGroup = Util.ShuffleList(accessibleGroup);
                        for (MarkGroup markGroup : accessibleGroup) {
                            if (maleUse < maleRange) {//需要男生
                                Person person = Util.getRandomElement(markGroup.getMale(), random);
                                group.addPerson(person);
                                person.setGroup(group);
                                markGroup.removePerson(person);
                                markGroup.removeAccessableGroup(group);
                                maleUse++;
                                maleLeft--;
                                peopleLeft--;
                            } else if (maleUse >= maleRange) {//需要女生
                                Person person = Util.getRandomElement(markGroup.getFemale(), random);
                                group.addPerson(person);
                                person.setGroup(group);
                                markGroup.removePerson(person);
                                markGroup.removeAccessableGroup(group);
                                peopleLeft--;
                            }/*else{//随便
                        Person person = Util.getRandomElement(markGroup.getPeople(),random);
                        group.addPerson(person);
                        person.setGroup(group);
                        markGroup.removePerson(person);
                        markGroup.removeAccessableGroup(group);
                        peopleLeft--;
                        if(person.isMale()){
                            maleUse++;
                            maleLeft--;
                        }
                    }*/
                        }
                        Util.log(group + " has " + maleUse + " male people");
                        this.setValue(bar1,bar2+1,bar2Max,lable1,"分配各座位组的人...("+(bar2+1)+"/"+groups.size()+")",100);
                    }
                }else if(mark){
                    this.setValue(bar1,0,markGroups.size(),lable1,"分配各成绩组的人到座位...(0/"+markGroups.size()+")",5);
                    for (MarkGroup markGroup1 : markGroups) {
                        for (Person person : markGroup1.getPeople()) {
                            SeatGroup group = Util.getRandomElement(person.getMarkGroup().getAccessableGroup(), random);
                            group.addPerson(person);
                            person.setGroup(group);
                            person.getMarkGroup().removeAccessableGroup(group);
                        }
                        this.setValue(bar1,bar2+1,bar2Max,lable1,"分配各成绩组的人到座位...("+(bar2+1)+"/"+markGroups.size()+")",25);
                    }
                }else if(sex){
                    //安排男女人数
                    this.setValue(bar1,0,PeopleManager.getPeople().size(),lable1,"分配男女人数...",5);
                    int male = 0;
                    for(Person person:PeopleManager.getPeople()){
                        if(person.isMale()){
                            male++;
                        }
                    }
                    ArrayList<SeatGroup> groups = Util.ShuffleList(SeatGroupManager.getGroups());
                    int left = PeopleManager.getPeople().size();
                    int maleLeft = male;
                    for(SeatGroup group:groups) {
                        int var = Math.round((float) maleLeft / (float) left * (float) group.getEmptySeatSize());
                        group.setMale(var);
                        left -= group.getEmptySeatSize();
                        maleLeft -= var;
                        this.setValue(bar1,bar2+1,bar2Max,lable1,lable2,5);
                    }
                    this.setValue(bar1,0,PeopleManager.getPeople().size(),lable1,"安排男生...("+bar2+"/"+bar2Max+")",5);
                    //安排男生
                    for(Person person:PeopleManager.getPeople()){
                        if(!person.isMale())continue;
                        if(person.getGroup()!=null)continue;
                        ArrayList<SeatGroup> accessableGroup = new ArrayList<>();
                        for(SeatGroup group:SeatGroupManager.getGroups()){
                            if(group.canAddMale()){
                                accessableGroup.add(group);
                            }
                        }
                        SeatGroup group = Util.getRandomElement(accessableGroup, random);
                        group.addPerson(person);
                        person.setGroup(group);
                        maleLeft--;
                        this.setValue(bar1,bar2+1,bar2Max,lable1,"安排男生...("+(bar2+1)+"/"+bar2Max+")",25);
                    }
                    this.setValue(bar1,bar2,bar2Max,lable1,"安排女生...("+bar2+"/"+bar2Max+")",5);
                    //安排女生
                    for(Person person:PeopleManager.getPeople()){
                        if(person.isMale())continue;
                        if(person.getGroup()!=null)continue;
                        ArrayList<SeatGroup> accessableGroup = new ArrayList<>();
                        for(SeatGroup group:SeatGroupManager.getGroups()){
                            if(group.getEmptySeatSize()>0){
                                accessableGroup.add(group);
                            }
                        }
                        SeatGroup group = Util.getRandomElement(accessableGroup, random);
                        group.addPerson(person);
                        person.setGroup(group);
                        this.setValue(bar1,bar2+1,bar2Max,lable1,"安排女生...("+(bar2+1)+"/"+bar2Max+")",25);
                    }
                }else{
                    this.setValue(bar1,0,PeopleManager.getPeople().size(),lable1,"分配...(0/"+PeopleManager.getPeople().size()+")",5);
                    for(Person person:PeopleManager.getPeople()){
                        if(person.getGroup()!=null)continue;
                        ArrayList<SeatGroup> accessableGroup = new ArrayList<>();
                        for(SeatGroup group:SeatGroupManager.getGroups()){
                            if(group.getEmptySeatSize()>0){
                                accessableGroup.add(group);
                            }
                        }
                        SeatGroup group = Util.getRandomElement(accessableGroup, random);
                        group.addPerson(person);
                        person.setGroup(group);
                        this.setValue(bar1,bar2+1,bar2Max,lable1,"分配...("+(bar2+1)+"/"+PeopleManager.getPeople().size()+")",10);
                    }
                }

                Util.log("Arranging group seats");
                this.setValue(6,0,SeatGroupManager.getGroups().size(),"分配各组的座位...","...0/"+SeatGroupManager.getGroups().size(),5);
                for(SeatGroup group:SeatGroupManager.getGroups()) {
                    group.arrangeSeat(sex, saveCount, random);
                    this.setValue(bar1,bar2+1,bar2Max,lable1,"..."+(bar2+1)+"/"+bar2Max,75);
                }

                Util.log("Saving");
                this.setValue(7,0,1,"保存...","...",5);
                if(!debug) {
                    //保存
                    SimpleDateFormat df = new SimpleDateFormat("HH-mm-ss-SSS");
                    Date date = new Date();
                    String fileName;
                    FileOutputStream outputStream = new FileOutputStream(fileName="座位表" + "[" + df.format(date) + "]" + ".xlsx");
                    Util.workbook.write(outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Util.log("Success");
                    this.setValue(8,1,bar2Max,"完成...","...",5);
                    JOptionPane.showMessageDialog(null,"已保存至\'"+fileName+"\'","成功",JOptionPane.INFORMATION_MESSAGE);
                }
            }catch (Exception e){
                this.e=e;
            }
        }
    }


    //backup
    public static void arrangeCLEAR(boolean sex, boolean mark, boolean save, boolean saveCount) throws Exception{
        //reset
        Random random = new Random();
        Util.log("Reset");
        for(SeatGroup seatGroup: SeatGroupManager.getGroups()) {
            seatGroup.reset();
        }
        for(Person person:PeopleManager.getPeople()) {
            person.setSeat(null);
            person.setGroup(null);
        }

        //先分配成绩组
        Util.log("Arranging markGroup");
        ArrayList<MarkGroup> markGroups = new ArrayList<>();
        boolean flag = true;
        int count = 0;
        while (flag) {
            flag = false;
            ArrayList<SeatGroup> accessableGroup = new ArrayList<>();
            for (SeatGroup seatGroup : SeatGroupManager.getGroups()) {
                if (seatGroup.getCount() > 0) {
                    accessableGroup.add(seatGroup);
                    seatGroup.setCount(seatGroup.getCount() - 1);
                    if (seatGroup.getCount() > 0) flag = true;
                }
            }
            if (accessableGroup.size() > 0) {
                markGroups.add(new MarkGroup(count, accessableGroup.size(), accessableGroup));
                Util.log("New markGroup(count=" + count + ", accessableGroup=" + accessableGroup + ")");
            }
            count++;
        }

        //打乱数组
        Util.log("Shuffle List");
        markGroups = Util.ShuffleList(markGroups);

        //安排各组人
        Util.log("Arranging markGroup's people");
        if(mark) {
            Iterator<MarkGroup> iterator = markGroups.iterator();
            MarkGroup markGroup = iterator.next();
            for (Person person : PeopleManager.getPeople()) {
                if (markGroup.getSize() <= markGroup.getPeople().size()) {
                    markGroup = iterator.next();
                }
                markGroup.addPerson(person);
                person.setMarkGroup(markGroup);
            }
        }
        //安排黑幕
        HeiMuManager.arrangeHeiMu(mark);

        if(sex && mark){
            //先排人少的组，从这个组的成绩组中拿人
            ArrayList<SeatGroup> groups = SeatGroupManager.getGroups();
            groups = Util.ShuffleList(groups);
            /*groups.sort(new Comparator<SeatGroup>() {
                @Override
                public int compare(SeatGroup o1, SeatGroup o2) {
                    return o1.getEmptySeatSize()-o2.getEmptySeatSize();
                }
            });*/
            int peopleLeft = 0;
            int maleLeft = 0;
            for(Person person:PeopleManager.getPeople()){
                if(person.getGroup()!=null)continue;//可能需要算上有黑幕的
                peopleLeft++;
                if(person.isMale()){
                    maleLeft++;
                }
            }
            for(SeatGroup group:groups){
                ArrayList<MarkGroup> accessibleGroup = new ArrayList<>();
                for(MarkGroup markGroup:markGroups){
                    if(markGroup.getAccessableGroup().contains(group))accessibleGroup.add(markGroup);
                }
                //获取大约男性别人数
                int maleRange = Math.round((float) maleLeft/(float) peopleLeft*(float) group.getEmptySeatSize());
                int maleUse = 0;
                //先拿只有一个性别的组
                Iterator<MarkGroup> iterator = accessibleGroup.iterator();
                while (iterator.hasNext()){
                    MarkGroup group1 = iterator.next();
                    if(group1.getMale().size()==0){
                        //只剩女
                        Person person = Util.getRandomElement(group1.getFemale(),random);
                        group.addPerson(person);
                        person.setGroup(group);
                        group1.removeAccessableGroup(group);
                        group1.removePerson(person);
                        iterator.remove();
                        peopleLeft--;
                    }else if(group1.getFemale().size()==0){
                        //只剩男
                        Person person = Util.getRandomElement(group1.getMale(),random);
                        group.addPerson(person);
                        person.setGroup(group);
                        group1.removeAccessableGroup(group);
                        group1.removePerson(person);
                        iterator.remove();
                        maleLeft--;
                        peopleLeft--;
                        maleUse++;
                    }
                }
                //性别不强制要求
                accessibleGroup = Util.ShuffleList(accessibleGroup);
                for(MarkGroup markGroup:accessibleGroup){
                    if(maleUse<maleRange){//需要男生
                        Person person = Util.getRandomElement(markGroup.getMale(),random);
                        group.addPerson(person);
                        person.setGroup(group);
                        markGroup.removePerson(person);
                        markGroup.removeAccessableGroup(group);
                        maleUse++;
                        maleLeft--;
                        peopleLeft--;
                    }else if(maleUse>=maleRange){//需要女生
                        Person person = Util.getRandomElement(markGroup.getFemale(),random);
                        group.addPerson(person);
                        person.setGroup(group);
                        markGroup.removePerson(person);
                        markGroup.removeAccessableGroup(group);
                        peopleLeft--;
                    }/*else{//随便
                        Person person = Util.getRandomElement(markGroup.getPeople(),random);
                        group.addPerson(person);
                        person.setGroup(group);
                        markGroup.removePerson(person);
                        markGroup.removeAccessableGroup(group);
                        peopleLeft--;
                        if(person.isMale()){
                            maleUse++;
                            maleLeft--;
                        }
                    }*/
                }
            }
        }else if(mark){
            for (MarkGroup markGroup1 : markGroups) {
                for (Person person : markGroup1.getPeople()) {
                    SeatGroup group = Util.getRandomElement(person.getMarkGroup().getAccessableGroup(), random);
                    group.addPerson(person);
                    person.setGroup(group);
                    person.getMarkGroup().removeAccessableGroup(group);
                }
            }
        }else if(sex){
            //安排男女人数
            int male = 0;
            for(Person person:PeopleManager.getPeople()){
                if(person.isMale()){
                    male++;
                }
            }
            ArrayList<SeatGroup> groups = Util.ShuffleList(SeatGroupManager.getGroups());
            int left = PeopleManager.getPeople().size();
            int maleLeft = male;
            for(SeatGroup group:groups) {
                int var = Math.round((float) maleLeft / (float) left * (float) group.getEmptySeatSize());
                group.setMale(var);
                left -= group.getEmptySeatSize();
                maleLeft -= var;
            }
            //安排男生
            for(Person person:PeopleManager.getPeople()){
                if(!person.isMale())continue;
                if(person.getGroup()!=null)continue;
                ArrayList<SeatGroup> accessableGroup = new ArrayList<>();
                for(SeatGroup group:SeatGroupManager.getGroups()){
                    if(group.canAddMale()){
                        accessableGroup.add(group);
                    }
                }
                SeatGroup group = Util.getRandomElement(accessableGroup, random);
                group.addPerson(person);
                person.setGroup(group);
                maleLeft--;
            }

            //安排女生
            for(Person person:PeopleManager.getPeople()){
                if(person.isMale())continue;
                if(person.getGroup()!=null)continue;
                ArrayList<SeatGroup> accessableGroup = new ArrayList<>();
                for(SeatGroup group:SeatGroupManager.getGroups()){
                    if(group.getEmptySeatSize()>0){
                        accessableGroup.add(group);
                    }
                }
                SeatGroup group = Util.getRandomElement(accessableGroup, random);
                group.addPerson(person);
                person.setGroup(group);
            }
        }else{
            for(Person person:PeopleManager.getPeople()){
                if(person.getGroup()!=null)continue;
                ArrayList<SeatGroup> accessableGroup = new ArrayList<>();
                for(SeatGroup group:SeatGroupManager.getGroups()){
                    if(group.getEmptySeatSize()>0){
                        accessableGroup.add(group);
                    }
                }
                SeatGroup group = Util.getRandomElement(accessableGroup, random);
                group.addPerson(person);
                person.setGroup(group);
            }
        }

        for(SeatGroup group:SeatGroupManager.getGroups()) {
            group.arrangeSeat(sex, saveCount, random);
        }

        if(save) {
            //保存
            SimpleDateFormat df = new SimpleDateFormat("HH-mm-ss-S");
            Date date = new Date();
            FileOutputStream outputStream = new FileOutputStream("座位表" + "[" + df.format(date) + "]" + ".xlsx");
            Util.workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            //frame.dispose();
        }
    }
}
