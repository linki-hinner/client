package com.lin;

public class test {
    public static void main(String[] args) {
        String s = HttpClient.doGet1("http://localhost:8080/downloadFile?uuid=e2ec94bb-35d2-4dec-8a39-a359541733e8");
        System.out.println(s);
    }
}
