package com.tribune.dental_chart;


public class MainJar {

    public static void main(String[] args) {
        //just to fix the problem of the fat jar can't find/load main class
        App.main(args);
    }

}
