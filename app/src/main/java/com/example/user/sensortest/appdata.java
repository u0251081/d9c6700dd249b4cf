package com.example.user.sensortest;

import java.util.ArrayList;

public class appdata {
    //類別成員 <Generic>
    public static ArrayList<findvalue> getValue() {
        //建構集合物件
        ArrayList<findvalue> data = new ArrayList<>();


        //逐一參考員工物件
        findvalue e1 = new findvalue();

        e1.setId("0001");
        e1.setName("台北市");
        e1.setAddress("台北市");
        data.add(e1);
        findvalue e2 = new findvalue();
        e2.setId("0002");
        e2.setName("台北市");
        e2.setAddress("台中市");
        data.add(e2);
        findvalue e3 = new findvalue();
        e3.setId("台北市");
        e3.setName("台北市");
        e3.setAddress("台北市");
        data.add(e3);
        findvalue e4 = new findvalue();
        e4.setId("0004");
        e4.setName("高雄市");
        e4.setAddress("高雄市");
        data.add(e4);
        findvalue e5 = new findvalue();
        e5.setId("0001");
        e5.setName("張三豐");
        e5.setAddress("台北市");
        data.add(e5);
        findvalue e6 = new findvalue();
        e6.setId("0002");
        e6.setName("張無際");
        e6.setAddress("台中市");
        data.add(e6);
        findvalue e7 = new findvalue();
        e7.setId("0003");
        e7.setName("張泰山");
        e7.setAddress("台北市");
        data.add(e7);
        findvalue e8 = new findvalue();
        e8.setId("0004");
        e8.setName("張翠珊");
        e8.setAddress("高雄市");
        data.add(e8);
        findvalue e9 = new findvalue();
        e9.setId("0001");
        e9.setName("張三豐");
        e9.setAddress("台北市");
        data.add(e9);
        findvalue e10 = new findvalue();
        e10.setId("0002");
        e10.setName("張無際");
        e10.setAddress("台中市");
        data.add(e10);
        findvalue e11 = new findvalue();
        e11.setId("0003");
        e11.setName("張泰山");
        e11.setAddress("台北市");
        data.add(e11);
        findvalue e12 = new findvalue();
        e12.setId("0004");
        e12.setName("張翠珊");
        e12.setAddress("高雄市");
        data.add(e12);

        return data;
    }
}
