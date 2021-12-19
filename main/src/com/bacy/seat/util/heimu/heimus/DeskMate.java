package com.bacy.seat.util.heimu.heimus;

import com.bacy.seat.group.SeatGroup;
import com.bacy.seat.group.SeatGroupManager;
import com.bacy.seat.people.Person;
import com.bacy.seat.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DeskMate extends SameGroup{
    //need data   probability:0-100,personA:name,personB:name
    //etc. {"className":"DeskMate","probability":100,"personA":"张三","personB":"李四"}
    @Override
    public ArrayList<SeatGroup> getMatchGroups(boolean mark) {
        ArrayList<SeatGroup> temp = new ArrayList<>();
        if(personA.getGroup()!=null){
            temp.add(personA.getGroup());
        }else if(personB.getGroup()!=null){
            temp.add(personB.getGroup());
        }else{
            if(mark){
                temp = Util.getIntersection(personA.getMarkGroup().getAccessableGroup(),personB.getMarkGroup().getAccessableGroup());
            }else{
                temp.addAll(SeatGroupManager.getGroups());
            }
        }
        return temp;
    }

    @Override
    public String getDefaultJson() {
        return "{\"className\":\"DeskMate\",\"probability\":0-100,\"personA\":\"人1\",\"personB\":\"人2\"}";
    }

    @Override
    public String toString() {
        return "DeskMate{" +
                "probability=" + probability +
                ", personA='" + personA +
                ", personB=" + personB +
                '}';
    }
}
