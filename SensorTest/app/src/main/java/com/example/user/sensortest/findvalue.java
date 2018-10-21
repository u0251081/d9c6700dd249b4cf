package com.example.user.sensortest;
//Javabean (空參數建構 setter and getter)
public class findvalue implements  java.io.Serializable {

    private String id;  //日期
    private String name; //震度
    private String address; //等級
    private String location; //經緯度位置

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getlocation() {
        return location;
    }

    public void setlocation(String location) {
        this.location = location;
    }
}
