package com.bacy.seat.util.heimu;

import com.bacy.seat.group.SeatGroup;
import com.bacy.seat.people.Person;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Random;

public abstract class AbstractHeiMu {
    /*0-100*/
    protected int probability;
    public AbstractHeiMu(int probability){
        this.probability=probability;
    }
    public AbstractHeiMu(){}

    public void readData(JsonObject object){
        if(object.has("probability")){
            this.probability=object.get("probability").getAsInt();
        }else{
            this.probability=100;
        }
    }

    private boolean useful = true;
    public void doRandom(Random random){
        this.useful=random.nextInt(99)<probability;
    }

    public boolean isUseful() {
        return useful;
    }

    //读取完名单检查...
    public abstract boolean isAvailable();
    public abstract ArrayList<SeatGroup> getMatchGroups(boolean mark);
    public abstract void putToPerson();
}
