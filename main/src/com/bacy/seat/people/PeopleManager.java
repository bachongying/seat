package com.bacy.seat.people;

import com.bacy.seat.util.Util;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.Random;

public class PeopleManager {
    private static ArrayList<Person> people;

    public static ArrayList<Person> getPeople() {
        return people;
    }

    public static Person getPerson(String name){
        for(Person person:people){
            if(person.getName().equalsIgnoreCase(name)){
                return person;
            }
        }
        return null;
    }

    public static void initPeople() throws Exception{
        people=new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook("名单.xlsx");
        XSSFSheet sheet = workbook.getSheetAt(0);//读取表格数据
        Random random = new Random(System.currentTimeMillis());
        for(int a = 0;a<=sheet.getLastRowNum();a++){
            XSSFRow row = sheet.getRow(a);
            if(row==null)continue;
            Person person;
            String sex = Util.getValue(row.getCell(1));
            if(sex.equalsIgnoreCase("男")){
                person = new Person(Util.getValue(row.getCell(0)),true,a+1);
            }else if(sex.equalsIgnoreCase("女")){
                person = new Person(Util.getValue(row.getCell(0)),false,a+1);
            }else{
                person = new Person(Util.getValue(row.getCell(0)),random.nextBoolean(),a+1);
            }
            //Util.log("Read "+person);
            people.add(person);
        }
        Util.log("读入"+people.size()+"条数据");
        workbook.close();
    }
}
