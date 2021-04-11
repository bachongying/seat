package com.bacy.seat.util.heimu.heimus;

import com.bacy.seat.group.SeatGroup;
import com.bacy.seat.group.SeatGroupManager;
import com.bacy.seat.people.Person;
import com.bacy.seat.util.heimu.AbstractSinglePerson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetGroup extends AbstractSinglePerson {
    //need data   probability:0-100,person:name,groups:groupIDs split with ','
    //etc. {"className":"SetGroup","probability":100,"person":"张三","groups":"1,2,3"}
    private List<String> groups;
    public SetGroup(int probability, Person person, List<String> groups) {
        super(probability, person);
        this.groups = groups;
    }
    public SetGroup(){
        super();
    }

    @Override
    public ArrayList<SeatGroup> getMatchGroups(boolean mark) {
        ArrayList<SeatGroup> groups = new ArrayList<>();
        for(SeatGroup group: SeatGroupManager.getGroups()){
            if(this.groups.contains(group.getId())){
                groups.add(group);
            }
        }
        return groups;
    }

    @Override
    public void readData(JsonObject object) {
        super.readData(object);
        this.groups= Arrays.asList(object.get("groups").getAsString().split(","));
    }

    @Override
    public String toString() {
        return "SetGroup{" +
                "probability=" + probability +
                ", person='" + person +
                ", groups=" + groups +
                '}';
    }
}
