package com.bacy.seat.people;

import com.bacy.seat.model.Seat;
import com.bacy.seat.util.HeiMu;

import java.util.ArrayList;

public class Person {
    private String name;
    private boolean male;
    private int count;
    private boolean markGood = false;

    private ArrayList<HeiMu> heiMus = new ArrayList<>();
    private Seat seat;

    public Person(String name, boolean male, int count) {
        this.name = name;
        this.male = male;
        this.count = count;
    }

    public boolean isMarkGood() {
        return markGood;
    }

    public void setMarkGood(boolean markGood) {
        this.markGood = markGood;
    }

    public boolean hasHeiMu() {
        return heiMus.size()>0;
    }

    public void addHeiMu(HeiMu heiMu){
        this.heiMus.add(heiMu);
    }

    public ArrayList<HeiMu> getHeiMus() {
        return heiMus;
    }

    public void setSeat(Seat seat,boolean saveToSeat) {
        this.seat = seat;
        if(saveToSeat)this.seat.setPerson(this,false);
    }

    public boolean hasSeat(){
        return this.seat!=null;
    }

    public Seat getSeat(){
        return this.seat;
    }

    public String getName() {
        return name;
    }

    public boolean isMale() {
        return male;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "{" +
                "姓名 " + name +
                ",性别 " + (male?"男":"女") +
                '}';
    }
}
