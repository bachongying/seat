package com.bacy.seat.group;

import com.bacy.seat.people.Person;
import org.apache.poi.xssf.usermodel.XSSFCell;

public class Seat {
    private XSSFCell cell;
    private Person person;
    private SeatGroup group;

    public Seat(XSSFCell cell, SeatGroup group) {
        this.cell = cell;
        this.group = group;
    }

    public SeatGroup getGroup() {
        return group;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public boolean isEmpty(){
        return person==null;
    }

    public XSSFCell getCell() {
        return cell;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "person=" + person +
                ", location=(row=" + cell.getRowIndex()+",col="+cell.getColumnIndex() + ")" +
                '}';
    }
}
