package com.bacy.seat.group;

import com.bacy.seat.people.Person;

import java.util.ArrayList;

public class MarkGroup {
    private ArrayList<Person> people;
    private ArrayList<Person> male;
    private ArrayList<Person> female;
    private ArrayList<SeatGroup> accessableGroup;
    private int count;//编号
    private int size;//人数

    public MarkGroup(int count, int size, ArrayList<SeatGroup> accessableGroup) {
        this.count = count;
        this.accessableGroup = accessableGroup;
        this.size = size;
        this.people = new ArrayList<>();
        this.male = new ArrayList<>();
        this.female = new ArrayList<>();
    }

    public ArrayList<Person> getPeople() {
        return people;
    }

    public int getCount() {
        return count;
    }

    public ArrayList<SeatGroup> getAccessableGroup() {
        return accessableGroup;
    }

    public int getSize() {
        return size;
    }

    public void addPerson(Person person){
        this.people.add(person);
        if(person.isMale()){
            this.male.add(person);
        }else {
            this.female.add(person);
        }
    }

    public ArrayList<Person> getFemale() {
        return female;
    }

    public ArrayList<Person> getMale() {
        return male;
    }

    public void removePerson(Person person){
        this.people.remove(person);
        if(person.isMale()){
            this.male.remove(person);
        }else{
            this.female.remove(person);
        }
    }

    public void removeAccessableGroup(SeatGroup group){
        this.accessableGroup.remove(group);
    }

    @Override
    public String toString() {
        return "MarkGroup{" +
                "count=" + count +
                ", accessableGroupSize=" + accessableGroup.size() +
                ", people=" + people +
                ", accessableGroup=" + accessableGroup +
                '}';
    }
}
