package com.lin;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
    public static void main(String[] args) {
        try {
            String uuid = HttpClient.upload("http://localhost:8080/uploadFile", "C:\\Users\\lin\\Desktop\\照片\\身份证_反.jpg");
            HttpClient.downloadFile("http://localhost:8080/downloadFile", uuid,"C:\\Users\\lin\\Desktop\\照片\\download");
            HttpClient.getFileInfo("http://localhost:8080/fileInfo", uuid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
