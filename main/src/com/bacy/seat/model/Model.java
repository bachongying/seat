package com.bacy.seat.model;

import com.bacy.seat.people.People;
import com.bacy.seat.util.Util;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Model {
    public static ArrayList<Group> groups;
    public static XSSFWorkbook workbook;
    public static int seatSize;
    public static void loadModel() throws Exception{
        seatSize=0;
        groups=new ArrayList<>();
        Map<String,Group> model = new HashMap<>();
        workbook = new XSSFWorkbook("座位模板.xlsx");
        XSSFSheet sheet = workbook.getSheetAt(0);//读取表格数据
        for(int a = 0;a<=sheet.getLastRowNum();a++){
            XSSFRow row = sheet.getRow(a);
            if(row==null)continue;
            for(int b = 0;b<=row.getLastCellNum();b++){
                XSSFCell cell = row.getCell(b);
                if(cell==null)continue;
                String groupID = Util.getValue(cell);
                Util.log("读入("+cell.getRowIndex()+","+cell.getColumnIndex()+"),value="+groupID);
                seatSize++;
                if(model.containsKey(groupID)){
                    Group group = model.get(groupID);
                    Seat seat = new Seat(group,cell);
                    group.addSeat(seat);
                }else {
                    Group group = new Group(groupID);
                    model.put(groupID,group);
                    groups.add(group);
                    Seat seat = new Seat(group,cell);
                    group.addSeat(seat);
                }
            }
        }
        Util.log("加载完成,共 "+model.size()+" 组");
        Util.log("正在确认每个座位的同桌");
        for(Group group:groups){
            Util.log("正在确认"+group.getId()+"组");
            group.checkDeskMate();
        }
        Util.log("比对两表");
        if(seatSize!= People.people.size()){
            Util.log("不匹配");
            throw new Exception("两表人数不匹配");
        }
    }
}
