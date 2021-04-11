package com.bacy.seat.util;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.text.SimpleDateFormat;
import java.util.*;

public class Util {
    public static XSSFWorkbook workbook = null;
    public static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    public static void log(Object o){
        Date date = new Date();
        System.out.println("["+df.format(date)+"] "+ o.toString());
    }
    public static String getValue(XSSFCell xssfCell){
        if(xssfCell==null)return "";
        if(xssfCell.getCellType()== CellType.BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        }else if(xssfCell.getCellType()==CellType.NUMERIC) {
            return String.valueOf((int)xssfCell.getNumericCellValue());
        }else {
            return String.valueOf(xssfCell.getStringCellValue());
        }
    }
    public static <T> ArrayList<T> ShuffleList(ArrayList<T> list){
        ArrayList<T> list1 = new ArrayList<>();
        list1.addAll(list);
        Collections.shuffle(list1);
        return list1;
    }
    public static <T> T getRandomElement(List<T> list, Random random){
        if(list.size()==0){
            return null;
        }else if(list.size()==1){
            return list.get(0);
        }else{
            return list.get(random.nextInt(list.size()));
        }
    }
    public static <T> ArrayList<T> getIntersection(List<T> list1,List<T> list2){
        ArrayList<T> list = new ArrayList<>();
        for(T t:list1){
            if(list2.contains(t)){
                list.add(t);
            }
        }
        return list;
    }
    public static String DecodeBase64(String s){
        return new String(Base64.getDecoder().decode(s.getBytes()));
    }
    public static String EncodeBase64(String s){
        return new String(Base64.getEncoder().encode(s.getBytes()));
    }
    public static int[] getIntBetweenDouble(double value){
        int[] returnVar = new int[2];
        if(value>(int)value){
            returnVar[0] = (int) value;
            returnVar[1] = (int) value + 1;
        }else{
            returnVar[0] = (int) value - 1;
            returnVar[1] = (int) value + 1;
        }
        return returnVar;
    }
}
