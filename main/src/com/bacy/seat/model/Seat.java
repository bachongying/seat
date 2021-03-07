package com.bacy.seat.model;

import com.bacy.seat.people.Person;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class Seat {
    private Group parentGroup;//所属的组
    private XSSFCell cell;//这个座位对应的单元格
    private Person person;//这个座位上的人
    private Seat deskMate;//这个座位的同桌

    public Seat(Group parentGroup, XSSFCell cell) {
        this.parentGroup = parentGroup;
        this.cell = cell;
    }

    public XSSFCell getCell() {
        return cell;
    }

    public Group getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(Group parentGroup) {
        this.parentGroup = parentGroup;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person,boolean saveToPerson) {
        this.person = person;
        if(saveToPerson)this.person.setSeat(this,false);
    }

    public boolean hasPerson(){
        return this.person!=null;
    }

    public boolean hasDeskMate(){
        return deskMate!=null;
    }

    public Seat getDeskMate() {
        return deskMate;
    }

    public void setDeskMate(Seat deskMate) {
        this.deskMate = deskMate;
    }

    public String getLocation(){
        return "("+cell.getRowIndex()+","+cell.getColumnIndex()+")";
    }

    @Override
    public String toString() {
        return "{" +
                "所属组 " + parentGroup +
                ",所属人 " + person +
                ",位置 "+ getLocation() +
                ",同桌 " + (deskMate!=null?deskMate.getPerson():null) +
                '}';
    }
}
