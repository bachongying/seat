package com.bacy.seat.util;

import com.bacy.seat.model.Group;
import com.bacy.seat.model.Model;
import com.bacy.seat.model.Seat;
import com.bacy.seat.people.People;
import com.bacy.seat.people.Person;
import com.google.gson.JsonObject;

import java.util.*;

public abstract class HeiMu {
    private int id;//优先级

    public HeiMu(int id) {
        this.id = id;
    }

    protected abstract void setPerson(ArrayList<Person> people);

    protected abstract ArrayList<Group> getGroups(Person person, ArrayList<Group> groups);

    protected abstract ArrayList<Seat> getSeats(Person person, ArrayList<Seat> seats);

    protected abstract boolean isPersonHeiMu(Person person);

    public int getId() {
        return id;
    }

    private static ArrayList<HeiMu> heiMus = new ArrayList<>();
    public static void addHeiMu(HeiMu heiMu){
        heiMus.add(heiMu);
    }
    public static HeiMu getHeiMu(JsonObject object){
        switch (object.get("type").getAsString()){
            case "dm":
                return new Deskmate(object.get("body").getAsJsonObject());
            case "sg":
                return new SameGroup(object.get("body").getAsJsonObject());
            case "g":
                return new DesignatedGroup(object.get("body").getAsJsonObject());
        }
        return null;
    }
    public static void FinishAddHeiMu(){
        heiMus.sort(new Comparator<HeiMu>() {
            @Override
            public int compare(HeiMu o1, HeiMu o2) {
                return o1.getId()-o2.getId();
            }
        });
    }
    public static void setPeopleHeiMu(){
        for(Person person: People.people){
            for(HeiMu heiMu:heiMus){
                if(heiMu.isPersonHeiMu(person)){
                    person.addHeiMu(heiMu);
                }
            }
        }
    }
    public static void setAllHeiMuPerson(ArrayList<Person> people){
        for(HeiMu heiMu:heiMus){
            heiMu.setPerson(people);
        }
    }
    public static void setPeopleWithHeiMu(Random random){
        ArrayList<Group> group = new ArrayList<>();
        group.addAll(Model.groups);
        for(Person person:People.people){
            if(!person.hasHeiMu())continue;
            ArrayList<Group> groups = new ArrayList<>();
            groups.addAll(group);
            for(HeiMu heiMu:person.getHeiMus()){
                groups= heiMu.getGroups(person,groups);
            }
            Group g = Util.getRandomElement(groups,random);
            group.remove(g);
            ArrayList<Seat> seats = g.getUnUsedSeats();
            for(HeiMu heiMU:person.getHeiMus()){
                seats = heiMU.getSeats(person,seats);
            }
            Seat s = Util.getRandomElement(seats,random);
            s.setPerson(person,true);
        }
    }
    public static class Deskmate extends HeiMu{
        private String personA,personB;
        private Person pA,pB;
        //dm
        public Deskmate(JsonObject object) {
            super(1);
            Base64.Decoder decoder = Base64.getDecoder();
            personA = new String(decoder.decode(decoder.decode(object.get("A").getAsString())));
            personB = new String(decoder.decode(decoder.decode(object.get("B").getAsString())));
        }

        @Override
        public String toString() {
            return "同桌" +
                    "A='" + personA+
                    ", B='" + personB;
        }

        @Override
        protected void setPerson(ArrayList<Person> people) {
            for(Person person:people){
                if(person.getName().equalsIgnoreCase(personA)){
                    pA=person;
                }
                if(person.getName().equalsIgnoreCase(personB)){
                    pB=person;
                }
            }
        }

        @Override
        protected ArrayList<Group> getGroups(Person person,ArrayList<Group> groups) {
            if(person==pA){
                if(pB.hasSeat()){
                    groups = new ArrayList<>();
                    groups.add(pB.getSeat().getParentGroup());
                }
            }else if(person==pB){
                if(pA.hasSeat()){
                    groups = new ArrayList<>();
                    groups.add(pA.getSeat().getParentGroup());
                }
            }
            return groups;
        }

        @Override
        protected ArrayList<Seat> getSeats(Person person,ArrayList<Seat> seats) {
            Iterator<Seat> iterator = seats.iterator();
            while (iterator.hasNext()) {
                if(!iterator.next().hasDeskMate()){
                    iterator.remove();
                }
            }
            if(person==pA){
                if(pB.hasSeat()){
                    seats = new ArrayList<>();
                    seats.add(pB.getSeat().getDeskMate());
                }
            }else if(person==pB){
                if(pA.hasSeat()){
                    seats = new ArrayList<>();
                    seats.add(pA.getSeat().getDeskMate());
                }
            }
            return seats;
        }

        @Override
        protected boolean isPersonHeiMu(Person person) {
            return person.getName().equalsIgnoreCase(personA)||person.getName().equalsIgnoreCase(personB);
        }
    }

    public static class SameGroup extends HeiMu{
        private String personA,personB;
        private Person pA,pB;
        //sg
        public SameGroup(JsonObject object) {
            super(2);
            Base64.Decoder decoder = Base64.getDecoder();
            personA = new String(decoder.decode(decoder.decode(object.get("A").getAsString())));
            personB = new String(decoder.decode(decoder.decode(object.get("B").getAsString())));
        }

        @Override
        public String toString() {
            return "同组" +
                    "A='" + personA+
                    ", B='" + personB;
        }

        @Override
        protected void setPerson(ArrayList<Person> people) {
            for(Person person:people){
                if(person.getName().equalsIgnoreCase(personA)){
                    pA=person;
                }
                if(person.getName().equalsIgnoreCase(personB)){
                    pB=person;
                }
            }
        }

        @Override
        protected ArrayList<Group> getGroups(Person person,ArrayList<Group> groups) {
            if(person==pA){
                if(pB.hasSeat()){
                    groups = new ArrayList<>();
                    groups.add(pB.getSeat().getParentGroup());
                }
            }else if(person==pB){
                if(pA.hasSeat()){
                    groups = new ArrayList<>();
                    groups.add(pA.getSeat().getParentGroup());
                }
            }
            return groups;
        }

        @Override
        protected ArrayList<Seat> getSeats(Person person,ArrayList<Seat> seats) {
            if(person==pA){
                if(pB.hasSeat()&& pB.getSeat().getDeskMate()!=null){
                    seats.remove(pB.getSeat().getDeskMate());
                }
            }else if(person==pB){
                if(pA.hasSeat()&& pA.getSeat().getDeskMate()!=null){
                    seats.remove(pA.getSeat().getDeskMate());
                }
            }
            return seats;
        }

        @Override
        protected boolean isPersonHeiMu(Person person) {
            return person.getName().equalsIgnoreCase(personA)||person.getName().equalsIgnoreCase(personB);
        }
    }

    public static class DesignatedGroup extends HeiMu{
        private String person;
        private Person p;
        private List<String> groupIDs;
        //g
        public DesignatedGroup(JsonObject object) {
            super(0);
            Base64.Decoder decoder = Base64.getDecoder();
            person = new String(decoder.decode(decoder.decode(object.get("p").getAsString())));
            groupIDs = Arrays.asList(object.get("gs").getAsString().split(","));
        }

        @Override
        public String toString() {
            return "组" +
                    "p='" + person+
                    ", gs='" + groupIDs;
        }

        @Override
        protected void setPerson(ArrayList<Person> people) {
            for(Person person:people){
                if(person.getName().equalsIgnoreCase(this.person)){
                    p=person;
                }
            }
        }

        @Override
        protected ArrayList<Group> getGroups(Person person,ArrayList<Group> groups) {
            ArrayList<Group> groups1 = new ArrayList<>();
            for(Group group:groups){
                if(groupIDs.contains(group.getId())){
                    groups1.add(group);
                }
            }
            return groups1;
        }

        @Override
        protected ArrayList<Seat> getSeats(Person person,ArrayList<Seat> seats) {
            return seats;
        }

        @Override
        protected boolean isPersonHeiMu(Person person) {
            return person.getName().equalsIgnoreCase(this.person);
        }
    }
}
