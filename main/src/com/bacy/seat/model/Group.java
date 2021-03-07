package com.bacy.seat.model;

import com.bacy.seat.people.Person;
import com.bacy.seat.util.Util;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Group {
    private String id;
    private ArrayList<Seat> seats = new ArrayList<>();
    private int fewerSexCount = 0;

    public Group(String id) {
        this.id = id;
    }

    public void checkDeskMate(){
        for(Seat seat:this.seats){
            if(seat.getDeskMate()!=null)continue;
            XSSFCell cell = seat.getCell();
            for(Seat seat1:this.seats){
                XSSFCell cell1 = seat1.getCell();
                if(cell.getRowIndex()==cell1.getRowIndex()&&Math.abs(cell.getColumnIndex()-cell1.getColumnIndex())==1){
                    seat.setDeskMate(seat1);
                    seat1.setDeskMate(seat);
                    break;
                }
            }
        }
    }

    public void setFewerSexCount(int fewerSexCount) {
        this.fewerSexCount = fewerSexCount;
    }

    public int getFewerSexCount() {
        return fewerSexCount;
    }

    public boolean needFewerSex(boolean fewerSexIsMale){
        return fewerSexCount>getFewerSex(fewerSexIsMale);
    }

    public int getFewerSex(boolean fewerSexIsMale){
        int count = 0;
        for(Seat seat:seats){
            if(seat.hasPerson()){
                if(fewerSexIsMale&&seat.getPerson().isMale()){
                    count++;
                }else if(!seat.getPerson().isMale()){
                    count++;
                }
            }
        }
        return count;
    }

    //我觉得有几率出bug(有单人)
    public boolean putPerson(Person person, boolean fewerSex, Random random,boolean mark){
        ArrayList<Seat> usefulList = getUnUsedSeats();
        if(fewerSex){
            Iterator<Seat> iterator = usefulList.iterator();
            while (iterator.hasNext()){
                Seat seat = iterator.next();
                if(seat.hasDeskMate()&&seat.getDeskMate().hasPerson()&&seat.getDeskMate().getPerson().isMale()==person.isMale()){
                    iterator.remove();
                }
            }
        }else{
            Iterator<Seat> iterator = usefulList.iterator();
            while (iterator.hasNext()){
                Seat seat = iterator.next();
                if(mark&&seat.hasDeskMate()&&seat.getDeskMate().hasPerson()&&seat.getDeskMate().getPerson().isMarkGood()!=person.isMarkGood()){
                    seat.setPerson(person,true);
                    return true;
                }
            }
        }
        Seat seat = Util.getRandomElement(usefulList,random);
        if(seat==null){
            return false;
        }
        seat.setPerson(person,true);
        return true;
    }

    public void addSeat(Seat seat){
        this.seats.add(seat);
    }

    public String getId() {
        return id;
    }

    public ArrayList<Seat> getUnUsedSeats() {
        ArrayList<Seat> seats = new ArrayList<>();
        for(Seat seat:this.seats){
            if(!seat.hasPerson()) seats.add(seat);
        }
        return seats;
    }

    public ArrayList<Seat> getSeats(){
        return seats;
    }

    @Override
    public String toString() {
        return "{" +
                "组ID" + id +
                '}';
    }
}
