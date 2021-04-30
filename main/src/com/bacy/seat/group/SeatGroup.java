package com.bacy.seat.group;

import com.bacy.seat.people.Person;
import com.bacy.seat.util.Util;
import com.bacy.seat.util.heimu.AbstractHeiMu;
import com.bacy.seat.util.heimu.heimus.DeskMate;

import java.util.*;

public class SeatGroup {
    private ArrayList<Person> people;
    private ArrayList<Seat> seats;
    private String id;
    public SeatGroup(String id){
        this.id = id;
        this.seats = new ArrayList<>();
        this.people = new ArrayList<>();
    }

    public void addSeat(Seat seat){
        this.seats.add(seat);
        this.count++;
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public ArrayList<Seat> getSeats() {
        return seats;
    }

    public String getId() {
        return id;
    }

    public void arrangeSeat(boolean sex, boolean saveCount, Random random){
        people.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o2.getHeiMus().size()-o1.getHeiMus().size();
            }
        });
        //黑幕
        for(Person person:people){
            if(person.getHeiMus().size()==0)continue;
            if(person.getSeat()!=null)continue;
            for(AbstractHeiMu heiMu: person.getHeiMus()){
                if(heiMu instanceof DeskMate){
                    DeskMate hei = (DeskMate) heiMu;
                    if(people.contains(hei.getPersonA())&&people.contains(hei.getPersonB())){
                        Seat seat = null;
                        Seat deskMate = null;
                        int var = 0;
                        while (deskMate==null){
                            seat= Util.getRandomElement(this.getEmptySeat(),random);
                            deskMate= Util.getRandomElement(getDeskMate(seat),random);
                            if(deskMate!=null && !deskMate.isEmpty()){
                                deskMate=null;
                            }
                            var++;
                            if(var>50)break;
                        }
                        if(var<=50){
                            this.setPersonToSeat(hei.getPersonA(),seat,saveCount);
                            this.setPersonToSeat(hei.getPersonB(),deskMate,saveCount);
                        }
                    }
                }
            }
        }

        if(sex){
            //男生
            for(Person person:people) {
                if (person.getSeat() != null) continue;
                if (!person.isMale()) continue;
                ArrayList<Seat> seats = this.getEmptyOrNoMaleDeskMateSeats();
                Seat seat;
                if(seats.size()>0){
                    seat = Util.getRandomElement(seats,random);
                }else{
                    seat = Util.getRandomElement(this.getEmptySeat(),random);
                }
                this.setPersonToSeat(person,seat,saveCount);
            }
            //女生
            for(Person person:people) {
                if (person.getSeat() != null) continue;
                Seat seat=Util.getRandomElement(this.getEmptySeat(),random);
                this.setPersonToSeat(person,seat,saveCount);
            }
        }else{
            for(Person person:people) {
                if (person.getSeat() != null) continue;
                Seat seat=Util.getRandomElement(this.getEmptySeat(),random);
                this.setPersonToSeat(person,seat,saveCount);
            }
        }
    }

    private ArrayList<Seat> getEmptyOrNoMaleDeskMateSeats(){
        ArrayList<Seat> var = this.getEmptySeat();
        Iterator<Seat> iterator = var.iterator();
        while (iterator.hasNext()){
            Seat seat = iterator.next();
            if(this.hasDeskMateAndDeskMateHasMalePerson(seat))iterator.remove();
        }
        return var;
    }

    private void setPersonToSeat(Person person, Seat seat, boolean saveCount){
        seat.setPerson(person);
        person.setSeat(seat);
        if(saveCount){
            seat.getCell().setCellValue(person.getCount()+person.getName());
        }else{
            seat.getCell().setCellValue(person.getName());
        }
    }

    public int getEmptySeatSize(){
        return seats.size()-people.size();
    }

    private ArrayList<Seat> getEmptySeat(){
        ArrayList<Seat> seats = new ArrayList<>();
        for(Seat seat:this.seats){
            if(seat.isEmpty()){
                seats.add(seat);
            }
        }
        return seats;
    }

    private boolean hasDeskMateAndDeskMateHasMalePerson(Seat seat){
        for(Seat seat1:getDeskMate(seat)){
            if(!seat1.isEmpty()&&seat1.getPerson().isMale()) return true;
        }
        return false;
    }

    private ArrayList<Seat> getDeskMate(Seat seat){
        ArrayList<Seat> seats = new ArrayList<>();
        for(Seat seat1:this.seats){
            if(seat1==seat)continue;
            if(seat1.getCell().getRowIndex()==seat.getCell().getRowIndex()&&Math.abs(seat1.getCell().getColumnIndex()-seat.getCell().getColumnIndex())==1){
                seats.add(seat1);
            }
        }
        return seats;
    }

    public void reset(){
        for(Seat seat:this.seats){
            seat.setPerson(null);
        }
        this.count = this.seats.size();
        this.people = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "SeatGroup{" +
                "id='" + id + '\'' +
                '}';
    }

    public void addPerson(Person person){
        this.people.add(person);
    }

    //用于分配成绩组
    private int count = 0;
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    //用于分配男女分数
    private int male = 0;
    private int maleUse = 0;
    public void setMale(int male) {
        this.male = male;
    }
    public void setMaleUse(int maleUse) {
        this.maleUse = maleUse;
    }
    public int getMale() {
        return male;
    }
    public int getMaleUse() {
        return maleUse;
    }
    public boolean canAddMale(){
        return maleUse<male;
    }
}
