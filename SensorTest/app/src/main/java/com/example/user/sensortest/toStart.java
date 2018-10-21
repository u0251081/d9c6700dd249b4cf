package com.example.user.sensortest;


import java.lang.*;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.sensortest.DHT;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import static android.content.ContentValues.TAG;


public class toStart extends Activity {

    private SensorManager sensorMgr;
    private float x;//初始X值
    private float y;//初始Y值
    private float z;//初始Z值
    public double num;
    public double num2;
    public double num3;
    private Button connect = null;
    ArrayList<String> data = new ArrayList<>();
    Button btnshowdata;
    TextView tx;
    private TextView current_record;
    private int k = 0;//防多次紀錄
    public static final int DHTREQUEST = 1;
    public static final int DHTRESPONSE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);
        SensorActivity();
        setlayout();

        btnshowdata = (Button) findViewById(R.id.roadlist);
        tx = (TextView) findViewById(R.id.empty);

       /* findvalue e1=new findvalue();
        e1.setId("日期");
        e1.setName("震度值");
        e1.setAddress("顛簸等級");
        e1.setlocation("經緯度");
        data.add(e1);
        */
        RequestQueue queue = Volley.newRequestQueue(this);

        connect = (Button) findViewById(R.id.stop);
        //點擊按鈕後暫停紀錄
        connect.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(toStart.this, MainActivity.class);
                startActivity(intent);
                toStart.this.finish();
            }
        });


    }

    public void SensorActivity() {
        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
    }


    SensorEventListener listener = new SensorEventListener()
    {
        @Override

        //如果 sensor 值有改變  被觸發的事件會傳遞過來

        public void onSensorChanged(SensorEvent event)
        {
            //取得 sensor 的資料
            Sensor sensor = event.sensor;

            // StringBuilder sensorInfo = new StringBuilder();
           /* sensorInfo.append("Sensor name: "+sensor.getName()+"\n");
                          sensorInfo.append("Sensor type: "+sensor.getType()+"\n");
                          sensorInfo.append("Sensor power: "+sensor.getPower()+"mA\n");
                          sensorInfo.append("values: "+"\n");  */

            //取得加速度感應器的y軸的值

            float[] values = event.values;
            // x = values[0];
            y = values[1];
            //z = values[2];

            num = (double) y;
            BigDecimal bd = new BigDecimal(num);
            bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP); //取到小數後第一位
           double yy = bd.doubleValue();  //偵測的數值  (四捨五入到小數後第一位)


        /*  num2 = (double) x;
          BigDecimal bd2 = new BigDecimal(num2);
          bd2 = bd2.setScale(1, BigDecimal.ROUND_HALF_UP); //取到小數後第一位
          double xx = bd2.doubleValue();  //偵測的數值  (四捨五入到小數後第一位)
*/
                   /* num3 = (double) z;
                    BigDecimal bd3 = new BigDecimal(num3);
                    bd3 = bd3.setScale(1, BigDecimal.ROUND_HALF_UP); //取到小數後第一位
                    double zz = bd.doubleValue();  //偵測的數值  (四捨五入到小數後第一位)
*/

            if(yy< 15.0)

            {
                current_record.setText("平坦");
                setlayout();
            } else

            {

                String num_to_string = "";

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH點mm");
                Date curDate = new Date(System.currentTimeMillis()); // 獲取當前時間
                String currenttime = formatter.format(curDate);

                if (yy >= 15.0 && yy < 16.0) {
               /* findvalue e1=new findvalue();
               e1.setId("0001");
                e1.setName(num_to_string);
                e1.setAddress("等級 1");
                e1.setlocation(" ");
                e1.setId("18:00");
                data.add(e1);*/
                    num_to_string = "{" + "\"jump\":" + Double.toString(yy) + ",\"level\":" + "1," + "\"longitude\":" + "22.754485," + "\"latitude\":" + "-120.332831," + "\"uploadtime\":" + currenttime;
                    current_record.setText("等級 1");
                    setlaout2();

                } else if (yy >= 16.0 && yy < 17.0) {
             /*   findvalue e1=new findvalue();
                e1.setId("0002");
                e1.setName(num_to_string);
                e1.setAddress("等級 2");
                e1.setlocation(" ");
                e1.setId("14:00");
                data.add(e1);  */
                    num_to_string = "{" + "\"jump\":" + Double.toString(yy) + ",\"level\":" + "2," + "\"longitude\":" + "22.754485," + "\"latitude\":" + "-120.332831," + "\"uploadtime\":" + currenttime;
                    current_record.setText("等級 2");
                    setlaout2();
                } else if (yy >= 17.0 && yy < 18.0) {
             /*   findvalue e1=new findvalue();
                e1.setId("0003");
                e1.setName(num_to_string);
                e1.setAddress("等級 3");
                e1.setlocation(" ");
                e1.setId("5:00");
                data.add(e1);  */
                    num_to_string = "{" + "\"jump\":" + Double.toString(yy) + ",\"level\":" + "3," + "\"longitude\":" + "22.754485," + "\"latitude\":" + "-120.332831," + "\"uploadtime\":" + currenttime;
                    current_record.setText("等級 3");
                    setlaout2();
                } else if (yy >= 18.0 && yy < 19.0) {
               /* findvalue e1=new findvalue();
                e1.setId("0004");
                e1.setName(num_to_string);
                e1.setAddress("等級 4");
                e1.setlocation(" ");
                e1.setId("12:00");
                data.add(e1);  */
                    num_to_string = "{" + "\"jump\":" + Double.toString(yy) + ",\"level\":" + "4," + "\"longitude\":" + "22.754485," + "\"latitude\":" + "-120.332831," + "\"uploadtime\":" + currenttime;
                    current_record.setText("等級 4");
                    setlaout2();
                } else if (yy >= 19.0) {
              /*  findvalue e1=new findvalue();
                e1.setId("0005");
                e1.setName(num_to_string);
                e1.setAddress("等級 5");
                e1.setlocation(" ");
                e1.setId("18:00");
                data.add(e1);  */
                    num_to_string = "{" + "\"jump\":" + Double.toString(yy) + ",\"level\":" + "5," + "\"longitude\":" + "22.754485," + "\"latitude\":" + "-120.332831," + "\"uploadtime\":" + currenttime;
                    current_record.setText("等級 5");
                    setlaout2();
                }
                tx.setText(num_to_string);

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                JSONObject json = new JSONObject();
                try {
                    json.put("value",num_to_string);
                    json.put("id","road_test9");
                    json.put("deviceId","7606988296");
                    json.put("time","2018-08-30T09:00:30.562Z");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "https://iot.cht.com.tw/iot/v1/device/7606988296/sensor/road_test9/rawdata", json,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                tx.setText("String Response : "+ response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

                requestQueue.add(jsonObjectRequest);


            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            //當 sensor 的精準度改變時會呼叫此方法
        }


    };


    protected void onResume()
    {
        super.onResume();
        sensorMgr.registerListener(listener, //監聽sensor的值是否改變
                sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), //偵測要監聽的是哪個感應器 (此次專案是監聽加速度感應器)
                SensorManager.SENSOR_DELAY_NORMAL); //抓取值的頻率
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(toStart.this)
                    .setTitle("離開坑洞終結者")
                    .setMessage("祝您行車平安~~")

                    .setPositiveButton("掰掰",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub

                                }
                            }).show();
        }
        return true;
    }




    //偵測畫面1(路面尚未顛簸)
    private  void setlayout()
    {

        current_record=(TextView)findViewById(R.id.current_record);
        current_record.setBackgroundResource(R.drawable.green);

        connect = (Button) findViewById(R.id.stop);

        //點擊按鈕後更換layout

        connect.setOnClickListener(new Button.OnClickListener()
        {

            public void onClick(View v)
            {

                Intent intent = new Intent();
                intent.setClass(toStart.this, MainActivity.class);
                startActivity(intent);
                toStart.this.finish();
            }

        });
    }


    //偵測畫面2(路面顛簸)
    private void setlaout2()
    {

        current_record=(TextView)findViewById(R.id.current_record);
        current_record.setBackgroundResource(R.drawable.red);


        connect = (Button) findViewById(R.id.stop);
                connect.setOnClickListener(new Button.OnClickListener()
                {

                    public void onClick(View v)
                    {

                Intent intent = new Intent();
                intent.setClass(toStart.this, MainActivity.class);
                startActivity(intent);
                toStart.this.finish();
            }

        });
    }

    //切換至偵測清單畫面
    public void changeProcess(View view)
    {
       /* ArrayList<findvalue> data2= data;
        //Toast.makeText(this,"變更畫面!!",Toast.LENGTH_LONG).show();
        //1.建構Intent 指定來源 設定目標的Meta data
        Intent intent=new Intent(toStart.this,tolist.class);
        //帶資料(序列化)
        intent.putExtra("data",data2);
        //帶入android環境 進行切換
        this.startActivity(intent);
        toStart.this.finish();

*/
       /* Intent intent=new Intent(toStart.this,tolist.class);
        this.startActivity(intent);
        toStart.this.finish();*/

        showDHTData(btnshowdata);

    }
    //切換至使用者基本資料畫面
    public void changeProcess2(View view)
    {
        Intent intent=new Intent(toStart.this,touser.class);
        this.startActivity(intent);
        toStart.this.finish();
    }

    //切換至地圖畫面
    public void changeProcess3(View view)
    {
        Intent intent=new Intent(toStart.this,tomap.class);
        this.startActivity(intent);
        toStart.this.finish();
    }

    //按鈕簡易事件程序自動委派(onClick Method結構)
    public void showDHTData(View view)
    {
        //採用雙向切換(來源Activity 可以傳遞資訊到Target /Target處理好退掉 也可以回傳資訊到這一個Activity)
        //1.建構Intent
        Intent intent=new Intent(toStart.this,tolist.class);
        //2.帶URL資料到目標Activity
        //2.1使用機動問出資源管理物件
        Resources resources=this.getResources();
        String hostedString=resources.getString(R.string.hosted);
        String dhtservice=resources.getString(R.string.dhtservice);
        String url=String.format("%s%s",hostedString,dhtservice);
        Log.i("網址",url);
        //帶資訊 OverLoading Method
        intent.putExtra("url",url);
        //有去有回 狀態(state) 回來要有Callback
        //如何去 帶旗標
        this.startActivityForResult(intent,toStart.DHTREQUEST); //第二參數當作Flag 使用Callback程序判斷
toStart.this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        //要自己處理
        if(requestCode==toStart.DHTREQUEST && resultCode==toStart.DHTRESPONSE)
        {
            //透過Intent取出回傳的狀態

        }
    }

  /*  public static ArrayList<findvalue> getValue(ArrayList<String> a)
    {

        ArrayList<String>b=a;

        //建構集合物件
        //逐一參考員工物件
       // findvalue e1=new findvalue();

      /*  e1.setId("0001");
        e1.setName(b.get(0));
        e1.setAddress("台北市");
        data.add(e1);
        findvalue e2=new findvalue();
        e2.setId("0002");
        e2.setName("台北市");
        e2.setAddress("台中市");
        data.add(e2);
        findvalue e3=new findvalue();
        e3.setId("台北市");
        e3.setName("台北市");
        e3.setAddress("台北市");
        data.add(e3);
        findvalue e4=new findvalue();
        e4.setId("0004");
        e4.setName("高雄市");
        e4.setAddress("高雄市");
        data.add(e4);
        findvalue e5=new findvalue();
        e5.setId("0001");
        e5.setName("張三豐");
        e5.setAddress("台北市");
        data.add(e5);
        findvalue e6=new findvalue();
        e6.setId("0002");
        e6.setName("張無際");
        e6.setAddress("台中市");
        data.add(e6);
        findvalue e7=new findvalue();
        e7.setId("0003");
        e7.setName("張泰山");
        e7.setAddress("台北市");
        data.add(e7);
        findvalue e8=new findvalue();
        e8.setId("0004");
        e8.setName("張翠珊");
        e8.setAddress("高雄市");
        data.add(e8);
        findvalue e9=new findvalue();
        e9.setId("0001");
        e9.setName("張三豐");
        e9.setAddress("台北市");
        data.add(e9);
        findvalue e10=new findvalue();
        e10.setId("0002");
        e10.setName("張無際");
        e10.setAddress("台中市");
        data.add(e10);
        findvalue e11=new findvalue();
        e11.setId("0003");
        e11.setName("張泰山");
        e11.setAddress("台北市");
        data.add(e11);
        findvalue e12=new findvalue();
        e12.setId("0004");
        e12.setName("張翠珊");
        e12.setAddress("高雄市");
        data.add(e12);

        return data;
    }
*/

}

