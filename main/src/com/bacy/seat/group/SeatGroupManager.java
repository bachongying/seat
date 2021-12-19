package com.bacy.seat.group;

import com.bacy.seat.people.PeopleManager;
import com.bacy.seat.util.Util;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class SeatGroupManager {
    private static ArrayList<SeatGroup> groups;
    public static void initSeatGroup() throws Exception{
        Map<String,SeatGroup> model = new HashMap<>();
        groups = new ArrayList<>();
        XSSFWorkbook workbook = Util.workbook = new XSSFWorkbook("座位模板.xlsx");
        XSSFSheet sheet = workbook.getSheetAt(0);//读取表格数据
        int count = 0;
        for(int a = 0;a<=sheet.getLastRowNum();a++){
            XSSFRow row = sheet.getRow(a);
            if(row==null)continue;
            for(int b = 0;b<=row.getLastCellNum();b++){
                XSSFCell cell = row.getCell(b);
                if(cell==null)continue;
                String groupID = Util.getValue(cell);
                if(model.containsKey(groupID)){
                    SeatGroup group = model.get(groupID);
                    Seat seat = new Seat(cell, group);
                    //Util.log("Read "+seat+" GroupID="+groupID);
                    group.addSeat(seat);
                    count++;
                }else {
                    SeatGroup group = new SeatGroup(groupID);
                    model.put(groupID,group);
                    groups.add(group);
                    Seat seat = new Seat(cell, group);
                    group.addSeat(seat);
                    count++;
                }
            }
        }
        Util.log("读入"+groups.size()+"个组,共"+count+"个座位");

        if(count!= PeopleManager.getPeople().size()){
            throw new Exception("座位数("+count+")与人数("+PeopleManager.getPeople().size()+")不符");
        }
    }

    public static ArrayList<SeatGroup> getGroups() {
        return groups;
    }
}
