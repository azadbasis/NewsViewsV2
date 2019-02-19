package com.newsviewsv2.utils;

public class App {
    private static final App ourInstance = new App();
    public static Query query=null;
    public static String userId=null;

    public static App getInstance() {
        return ourInstance;
    }

    private App() {
    }
}
