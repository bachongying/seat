package com.bacy.seat.util.heimu;

import com.bacy.seat.group.SeatGroup;
import com.bacy.seat.group.SeatGroupManager;
import com.bacy.seat.people.PeopleManager;
import com.bacy.seat.people.Person;
import com.bacy.seat.util.Util;
import com.bacy.seat.util.heimu.heimus.DeskMate;
import com.bacy.seat.util.heimu.heimus.DifferentGroup;
import com.bacy.seat.util.heimu.heimus.SameGroup;
import com.bacy.seat.util.heimu.heimus.SetGroup;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class HeiMuManager {
    public static ArrayList<AbstractHeiMu> heiMus;
    public static Class[] classes = {DeskMate.class,DifferentGroup.class,SameGroup.class,SetGroup.class};

    /*example json at jars/downloadinfo.json
    {"data":[base64code,base64code]}
    base64code:{"className":"DifferentGroup",}
    */

    public static void initHeiMu() throws Exception{
        Gson gson = new Gson();
        heiMus = new ArrayList<>();
        JsonObject var = gson.fromJson(new FileReader("./jars/downloadinfo.json"),JsonObject.class);
        Iterator<JsonElement> iterator = var.get("data").getAsJsonArray().iterator();
        while (iterator.hasNext()){
            JsonElement element = iterator.next();
            JsonObject object = gson.fromJson(Util.DecodeBase64(element.getAsString()),JsonObject.class);
            try{
                Class<? extends AbstractHeiMu> cls = (Class<? extends AbstractHeiMu>)Class.forName("com.bacy.seat.util.heimu.heimus."+object.get("className").getAsString());
                AbstractHeiMu heiMu = cls.newInstance();
                heiMu.readData(object);
                if(heiMu.isAvailable()){
                    heiMus.add(heiMu);
                    heiMu.putToPerson();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public static void removeHeiMu(AbstractHeiMu heiMu){
        heiMus.remove(heiMu);
    }
    public static void addHeiMu(AbstractHeiMu heiMu){
        heiMus.add(heiMu);
    }
    public static void saveHeiMu() throws Exception{
        File file = new File("./jars/downloadinfo.json");
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(new FileReader(file),JsonObject.class);
        JsonArray array = new JsonArray();
        for(AbstractHeiMu heiMu:heiMus){
            String var = heiMu.getJson().toString();
            var = Util.EncodeBase64(var);
            array.add(var);
        }
        jsonObject.add("data",array);

        PrintWriter printWriter = new PrintWriter(file);
        printWriter.println(gson.toJson(jsonObject));
        printWriter.flush();
        printWriter.close();
    }
    public static void arrangeHeiMu(boolean mark){
        ArrayList<Person> people = new ArrayList<>();
        people.addAll(PeopleManager.getPeople());
        people.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o2.getHeiMus().size()-o1.getHeiMus().size();
            }
        });
        Random random = new Random();
        for(AbstractHeiMu heiMu:heiMus){
            heiMu.doRandom(random);
        }
        for(Person person:people){
            if(person.getHeiMus().size()==0)continue;
            ArrayList<AbstractHeiMu> heiMus = person.getHeiMus();

            //获取对于这个人来说可用的黑幕组
            ArrayList<SeatGroup> accessableGroup;
            if(mark){
                accessableGroup = person.getMarkGroup().getAccessableGroup();
            }else {
                int count = 1;
                for(AbstractHeiMu heiMu:person.getHeiMus()){
                    if(heiMu instanceof AbstractTwoPeopleHeiMu){
                        AbstractTwoPeopleHeiMu heiMu1 = (AbstractTwoPeopleHeiMu) heiMu;
                        if(heiMu1.isUseful()&&(heiMu1.personA.getGroup()!=null||heiMu1.personB.getGroup()!=null)){
                            count++;
                        }
                    }
                }
                ArrayList<SeatGroup> seatGroups = new ArrayList<>();
                for(SeatGroup group: SeatGroupManager.getGroups()){
                    if(group.getEmptySeatSize()>count){
                        seatGroups.add(group);
                    }
                }
                accessableGroup=seatGroups;
            }
            for(AbstractHeiMu heiMu:heiMus){
                if(!heiMu.isUseful()){
                    Util.log("not use         "+heiMu.toString());
                    continue;
                }
                ArrayList<SeatGroup> temp = Util.getIntersection(heiMu.getMatchGroups(mark),accessableGroup);
                if(temp.size()>0){
                    accessableGroup=temp;
                    Util.log("arrange success "+heiMu);
                }else{
                    Util.log("arrange failed  "+heiMu);
                }
            }
            if(accessableGroup.size()>0){
                SeatGroup group = Util.getRandomElement(accessableGroup,random);
                person.setGroup(group);
                group.addPerson(person);
                if(mark){
                    person.getMarkGroup().removeAccessableGroup(group);
                    person.getMarkGroup().removePerson(person);
                }
            }
        }
    }
}
