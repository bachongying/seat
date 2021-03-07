package com.bacy.seat;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Iterator;

public class Update {
    public static void main(String[] args) {
        String data = args[0];
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(data, JsonObject.class);
        JsonArray array = json.get("data").getAsJsonArray();
        Iterator<JsonElement> iterator = array.iterator();

        JFrame frame = new JFrame("Updater");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setSize(300,120);
        int windowWidth = frame.getWidth();
        int windowHeight = frame.getHeight();
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        frame.setLocation(screenWidth/2-windowWidth/2, screenHeight/2-windowHeight/2);

        JLabel label = new JLabel("准备中...");
        JProgressBar bar = new JProgressBar(0,1);
        label.setBounds(10,10,300,20);
        bar.setBounds(10,40,250,20);
        frame.add(label);
        frame.add(bar);
        frame.setVisible(true);
        try{
            Thread.sleep(Long.valueOf(args[1]));
        }catch (Exception e){
            e.printStackTrace();
        }

        Thread thread1 = new Thread(){
            @Override
            public void run() {
                try{
                    Thread.sleep(100);
                    frame.repaint();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        thread1.start();

        int var = 1;
        int max = array.size();
        while (iterator.hasNext()){
            JsonObject object = iterator.next().getAsJsonObject();
            String url = object.get("url").getAsString();
            String path = object.get("path").getAsString();
            File file = new File(path);
            file.getParentFile().mkdirs();
            System.out.println(url+"   "+path);
            DownLoadThread thread = new DownLoadThread(url,path,bar);
            thread.start();
            while (thread.isAlive()){
                label.setText(thread.getDownloadState()+"("+var+"/"+max+")");
            }
            var++;
        }
        label.setText("请重启软件,软件将自动退出");
        try {
            Thread.sleep(2000);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.exit(0);
    }
    public static String format(double value,String format) {
        DecimalFormat df = new DecimalFormat(format);
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }
    public static String getSize(int size){
        if(size>1024*1024*1024){
            return format(size/1024.0/1024.0/1024.0,"0.00")+"GB";
        }else if(size>1024*1024){
            return format(size/1024.0/1024.0,"0.00")+"MB";
        }else if(size>1024){
            return format(size/1024.0,"0.00")+"KB";
        }
        return size+"B";
    }
    public static class DownLoadThread extends Thread{
        private String url;
        private String path;
        private JProgressBar bar;
        private String state = "";
        public DownLoadThread(String url,String path,JProgressBar bar){
            super();
            this.url=url;
            this.path=path;
            this.bar=bar;
            this.bar.setValue(0);
        }
        @Override
        public void run() {
            try {
                URL u = new URL(url);
                HttpURLConnection connect = (HttpURLConnection) u.openConnection();
                connect.setRequestMethod("GET"); // 参数必须大写
                connect.setConnectTimeout(20000);
                state = "连接中...";
                connect.connect();
                state = "获取数据...";
                InputStream inputStream = connect.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                int len = connect.getContentLength();
                FileOutputStream outputStream = new FileOutputStream(path);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream,10240);
                bar.setMaximum(len);

                byte[] data = new byte[1024];
                int downloadedFileSize = 0;
                int x = 0;
                String max = getSize(len);

                state = "下载中...("+getSize(downloadedFileSize) + "/"+max +"  "+format((double) downloadedFileSize/(double) len*100,"0.0")+"%)";

                while ((x = bufferedInputStream.read(data, 0, 1024)) >= 0) {
                    downloadedFileSize += x;
                    bar.setValue(downloadedFileSize);
                    state = "下载中...("+getSize(downloadedFileSize) + "/"+max +"  "+format((double) downloadedFileSize/(double) len*100,"0.0")+"%)";
                    bufferedOutputStream.write(data, 0, x);
                }
                bufferedInputStream.close();
                bufferedOutputStream.close();
                state = "下载完成";
                Thread.sleep(1000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public String getDownloadState() {
            return state;
        }
    }
}
