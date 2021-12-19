package com.bacy.seat.util.heimu.heimus;

import com.bacy.seat.group.SeatGroup;
import com.bacy.seat.group.SeatGroupManager;
import com.bacy.seat.people.Person;
import com.bacy.seat.util.Util;
import com.bacy.seat.util.heimu.AbstractTwoPeopleHeiMu;

import java.util.ArrayList;

public class SameGroup extends AbstractTwoPeopleHeiMu {
    //need data   probability:0-100,personA:name,personB:name
    //etc. {"className":"SameGroup","probability":100,"personA":"张三","personB":"李四"}
    public SameGroup(){
        super();
    }

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
        return "{\"className\":\"SameGroup\",\"probability\":0-100,\"personA\":\"人1\",\"personB\":\"人2\"}";
    }

    @Override
    public String toString() {
        return "SameGroup{" +
                "probability=" + probability +
                ", personA='" + personA +
                ", personB=" + personB +
                '}';
    }
}
