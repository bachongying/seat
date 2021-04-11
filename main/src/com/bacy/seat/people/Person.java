package com.bacy.seat.people;

import com.bacy.seat.group.MarkGroup;
import com.bacy.seat.group.Seat;
import com.bacy.seat.group.SeatGroup;
import com.bacy.seat.util.heimu.AbstractHeiMu;

import java.util.ArrayList;

public class Person {
    private String name;
    private boolean isMale;
    private int count;
    private MarkGroup markGroup;
    private ArrayList<AbstractHeiMu> heiMus;
    private Seat seat;
    private SeatGroup group;
    public Person(String name, boolean isMale, int count){
        this.name = name;
        this.isMale = isMale;
        this.count = count;
        this.heiMus = new ArrayList<>();
    }

    public void setMarkGroup(MarkGroup markGroup) {
        this.markGroup = markGroup;
    }

    public MarkGroup getMarkGroup() {
        return markGroup;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setGroup(SeatGroup group) {
        this.group = group;
    }

    public SeatGroup getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public boolean isMale() {
        return isMale;
    }

    public ArrayList<AbstractHeiMu> getHeiMus() {
        return heiMus;
    }

    public void addHeiMu(AbstractHeiMu heiMu){
        this.heiMus.add(heiMu);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", isMale=" + isMale +
                ", count=" + count +
                '}';
    }
}
