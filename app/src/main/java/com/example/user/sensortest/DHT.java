package com.example.user.sensortest;

public class DHT implements  java.io.Serializable{
    //attribute 採用封裝性 資訊隱藏
    private String jump;
    private String level;
    private String longitude;  //經度
    private String latitude; //緯度
    private String uploadtime;

    //setter and gette setTemper() getTemper() 去頭set or get 留下後面名稱回歸匈牙利命名法 temper稱呼為property(屬性)
    public String getjump() {
        return  "震度: "+jump;
    }

    public void setjump(String jump) {
        this.jump=jump;
    }

    public String getlevel() {
        return "等級: "+level;
    }

    public void setlevel(String level) {
        this.level = level;
    }
    public String  getlongitude() {
        return  "經度: "+longitude;
    }

    public void setlongitude(String longitude) {
        this.longitude = longitude;
    }
    public String getlatitude() {
        return "緯度: "+latitude;
    }

    public void setlatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getuploadtime() {
        return "上傳時間: "+uploadtime;
    }

    public void setuploadtime(String uploadtime) {
        this.uploadtime = uploadtime;
    }
}
