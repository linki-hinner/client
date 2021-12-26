package com.lin;

import com.sun.deploy.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpClient {
    public static void downloadFile(String url, String uuid, String DirectoryPath) throws Exception {
        File dir = new File(DirectoryPath);
        dir.mkdirs();
        HttpURLConnection connection = (HttpURLConnection) new URL(url + "?" + "uuid=" + uuid).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(60000);
        connection.connect();
        System.out.println("code: " + connection.getResponseCode());
        if (connection.getResponseCode() == 200) {
            Pattern compile = Pattern.compile(".*filename=\"(.*)\"");
            Matcher matcher = compile.matcher(connection.getHeaderField("Content-Disposition"));
            if (matcher.find()) {
                String fileFullName = URLDecoder.decode(matcher.group(1), "utf-8");
                Files.copy(connection.getInputStream(), Paths.get(DirectoryPath + File.separator + fileFullName));
            }
        }
        System.out.println("文件已完成下载");
        connection.disconnect();
    }

    public static void getFileInfo(String url, String uuid) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(url + "?" + "uuid=" + uuid).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(60000);
        connection.connect();
        System.out.println("code: " + connection.getResponseCode());
        if (connection.getResponseCode() == 200) {
            System.out.println("文件信息如下");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String temp = null;
            while ((temp = br.readLine()) != null) {
                System.out.println(temp);
            }
            System.out.println("=============");
        }
        connection.disconnect();
    }

    public static String upload(String httpUrl, String filePath) throws Exception {
        String Boundary = UUID.randomUUID().toString(); // 文件边界

        HttpURLConnection connection = (HttpURLConnection) new URL(httpUrl).openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Charset", "utf-8");
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + Boundary);

        File file = new File(filePath);
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeUTF("--" + Boundary + "\r\n" +
                "Content-Disposition: form-data; name=\"file\"; filename=\"" +
                file.getName() + "\"\r\n" +
                "Content-Type: application/octet-stream; charset=utf-8" + "\r\n\r\n");
        if (file.isFile()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            int read;
            byte[] bytes = new byte[1024 * 8];
            while ((read = fileInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.writeUTF("\r\n--" + Boundary + "--\r\n");
            out.flush();
            fileInputStream.close();
        }
        System.out.println("code: " + connection.getResponseCode());
        String uuid = null;
        if (connection.getResponseCode() == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            uuid = br.readLine();
            System.out.println("uuid:" + uuid);
            System.out.println("=============");
        }
        connection.disconnect();
        return uuid;
    }
}