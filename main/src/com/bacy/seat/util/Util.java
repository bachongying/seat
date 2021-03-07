package com.bacy.seat.util;

import com.bacy.seat.RandomSeat;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;

import javax.swing.*;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class Util {
    public static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");
    public static void log(Object o){
        Date date = new Date();
        System.out.println("["+df.format(date)+"] "+ o.toString());
    }
    public static String getHttpRequestData(String urlPath) throws Exception{
        String returnString = "";
        URL url = new URL(urlPath);
        HttpURLConnection connect = (HttpURLConnection) url.openConnection();
        connect.setRequestMethod("GET"); // 参数必须大写
        connect.setConnectTimeout(20000);
        connect.connect();
        InputStream inputStream = connect.getInputStream();
        int len = connect.getContentLength();
        byte[] bytes = new byte[len];
        inputStream.read(bytes, 0, len);
        inputStream.close();
        String strRet = new String(bytes);
        return strRet;
    }
    public static void checkUpdate() throws Exception{
        Gson gson = new Gson();
        log("从服务器获取更新数据");
        JsonObject object = gson.fromJson(getHttpRequestData("https://raw.githubusercontent.com/bachongying/seat/main/updateinfo.json"),JsonObject.class);
        log("比较版本信息");
        if(object.has("heimu")){
            checkHeiMu(object.get("heimu"));
        }
        if(object.get("verID").getAsInt() > RandomSeat.verId){
            log("发现新版本");
            String s = "新版本:"+object.get("ver").getAsString()+"\r\n更新内容:"+object.get("description").getAsString()+"\r\n按确定退出程序,按取消继续\r\n下载链接如下(ctrl+C复制)";
            if(JOptionPane.showInputDialog(null,s,"发现新版本",JOptionPane.INFORMATION_MESSAGE,null,null,object.get("link").getAsString())!=null){
                System.exit(0);
            }
        }else {
            log("未发现新版本");
        }
    }
    public static void checkHeiMu(JsonElement s){
        if(s.isJsonArray()){
            JsonArray array = s.getAsJsonArray();
            Iterator<JsonElement> iterator = array.iterator();
            while (iterator.hasNext()){
                JsonObject object = iterator.next().getAsJsonObject();
                HeiMu.addHeiMu(HeiMu.getHeiMu(object));
            }
        }else if(s.isJsonObject()){
            JsonObject object = s.getAsJsonObject();
            HeiMu.addHeiMu(HeiMu.getHeiMu(object));
        }
        HeiMu.FinishAddHeiMu();
    }
    public static String getValue(XSSFCell xssfCell){
        if(xssfCell.getCellType()== CellType.BOOLEAN) {
            return String.valueOf(xssfCell.getBooleanCellValue());
        }else if(xssfCell.getCellType()==CellType.NUMERIC) {
            return String.valueOf((int)xssfCell.getNumericCellValue());
        }else {
            return String.valueOf(xssfCell.getStringCellValue());
        }
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
}
