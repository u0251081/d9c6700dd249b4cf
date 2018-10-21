package com.example.user.sensortest;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.sensortest.DHT;  //從類別DHT取方法
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.example.user.sensortest.DHT;

import static android.content.ContentValues.TAG;

public class tolist extends Activity {
    //private Context mContext;
    //private Activity mActivity;
    private CoordinatorLayout mCLayout;
    //Attribute 請求佇列區 物件Constructor完之後引發
    private TextView t,counttext,nulltext;
    String account;
    Button btn;
    String activity_num="";
    String ioturl;
    String search_thing;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview2);
        t = (TextView) findViewById(R.id.listData);
        nulltext= (TextView) findViewById(R.id.nulltext);

       /* Intent intent=this.getIntent();
        //取出序列化物件
        ArrayList<findvalue> data=
                (ArrayList<findvalue>)intent.getSerializableExtra("data");

        //Toast.makeText(this,data.size()+"",Toast.LENGTH_LONG).show();
        //TODO 如何將Data轉換成ListView裡面View進行呈現
        //1.建構自訂配接器(Context,List資料)
        viewAdapter adapter=new viewAdapter(this,data);
        //配上去
        listData.setAdapter(adapter); //ListAdapter介面 進行介面多型化
*/



        //取出Intent

        //從Mainactivity取得url
        Intent intent = getIntent();
        activity_num=intent.getStringExtra("activity_num");
       ioturl = intent.getStringExtra("url");

        counttext=findViewById(R.id.counttext);
        account=constant.n;


        RequestQueue requestQueue = Volley.newRequestQueue(tolist.this);
        //下載ioT平台上資料
        JsonArrayRequest jsonarrayRequest = new JsonArrayRequest
                (Request.Method.GET, ioturl, (String) null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {

                                    int count=0;

                                    for (int i = response.length()-1; i > 0; i--) {
                                        JSONObject road = response.getJSONObject(i);
                                        String lat = road.getString("lat"); //緯度
                                        String lng = road.getString("lon"); //經度
                                        JSONArray values = road.getJSONArray("value"); //偵測數值
                                        String item0 = values.getString(0);
                                        String item1 = values.getString(1);
                                        String item2 = values.getString(2);
                                        String item3 = values.getString(3);
                                        String item4 = values.getString(4);
                                        String item5 = values.getString(5);
                                        //將字串符合JSON Object格式 Parser成JSONObject 反序列化
                                        //使用Gson jar
                                        //Gson gson=new Gson();
                                        //反序列化 String convert to Custom Class Instance
                                        //String[] strings = gson.fromJson(item, String[].class);

                                        if(account.equals(item4)||account==item4)
                                        {
                                            String name=item4;
                                        if(item0.equals("00")&&item1.equals("00"))
                                        {
                                            t.append("上傳方式: 地圖標記\n"+"緯度: " + lat + "\n 經度: " + lng + "\n 地址: " + item2 + "\n 上傳時間: " + item3 + "\n 使用者名稱: " + name + "\n 電話: " + item5);
                                            t.append("\n\n");
                                        }
                                        else
                                        {
                                            t.append("上傳方式: 手機偵測\n"+"震度: " + item0 + "  等級: " + item1 + "\n 緯度: " + lat + "\n 經度: " + lng + "\n 地址: " + item2 + "\n 上傳時間: " + item3 + "\n 使用者名稱: " + name + "\n 電話: " + item5);
                                            t.append("\n\n");
                                        }

                                            count++;
                                        }
                                       counttext.setText("您目前已上傳 "+count+" 筆資料至SkyTrain，\n感謝您的路見不平，拔\"手機\"相助 !");


                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast t = Toast.makeText(tolist.this, "e :" + e.getMessage(), Toast.LENGTH_LONG);
                                    t.show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast t = Toast.makeText(tolist.this, "error :" + error.toString(), Toast.LENGTH_LONG);
                                t.show();
                            }
                        }
                )

        {
            //Overriding 覆寫父親既定的方法
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //操作Headers加入金鑰
                //建構Map Collection
                Map<String, String> headers = new HashMap<>();
                //加入Key與value
                //String key=AppUtility.getResourceKeyValue("CK");
                //Log.i("Key",key);
                headers.put("CK", "PKQO1V8D10R44L63U7");
                return headers;
            }

        };
        requestQueue.add(jsonarrayRequest);




        /*進入此activity時EditText不先跳出鍵盤*/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    EditText search;
    //如果按下震度等級搜尋按鈕
    public void pressok(View view)
    {
        t.setText("");

        //取得 edittext內的值
 search=(EditText)findViewById(R.id.search);
      search_thing = search.getText().toString().trim();

        if (search_thing == null || search_thing.equals(""))
        {
            Toast.makeText(tolist.this, "請輸入想查詢的地址或震度的等級", Toast.LENGTH_LONG).show();
            RequestQueue requestQueue = Volley.newRequestQueue(tolist.this);
            //下載ioT平台上資料
            JsonArrayRequest jsonarrayRequest = new JsonArrayRequest
                    (Request.Method.GET, ioturl, (String) null,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {

                                        int count=0;

                                        for (int i = response.length()-1; i > 0; i--) {
                                            JSONObject road = response.getJSONObject(i);
                                            String lat = road.getString("lat"); //緯度
                                            String lng = road.getString("lon"); //經度
                                            JSONArray values = road.getJSONArray("value"); //偵測數值
                                            String item0 = values.getString(0);
                                            String item1 = values.getString(1);
                                            String item2 = values.getString(2);
                                            String item3 = values.getString(3);
                                            String item4 = values.getString(4);
                                            String item5 = values.getString(5);
                                            //將字串符合JSON Object格式 Parser成JSONObject 反序列化
                                            //使用Gson jar
                                            //Gson gson=new Gson();
                                            //反序列化 String convert to Custom Class Instance
                                            //String[] strings = gson.fromJson(item, String[].class);

                                            if(account.equals(item4)||account==item4)
                                            {
                                                String name=item4;
                                                if(item0.equals("00")&&item1.equals("00"))
                                                {
                                                    t.append("上傳方式: 地圖標記\n"+"緯度: " + lat + "\n 經度: " + lng + "\n 地址: " + item2 + "\n 上傳時間: " + item3 + "\n 使用者名稱: " + name + "\n 電話: " + item5);
                                                    t.append("\n\n");
                                                }
                                                else
                                                {
                                                    t.append("上傳方式: 手機偵測\n"+"震度: " + item0 + "  等級: " + item1 + "\n 緯度: " + lat + "\n 經度: " + lng + "\n 地址: " + item2 + "\n 上傳時間: " + item3 + "\n 使用者名稱: " + name + "\n 電話: " + item5);
                                                    t.append("\n\n");
                                                }

                                                count++;
                                            }
                                            counttext.setText("您目前已上傳 "+count+" 筆資料至SkyTrain，\n感謝您的路見不平，拔\"手機\"相助 !");


                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast t = Toast.makeText(tolist.this, "e :" + e.getMessage(), Toast.LENGTH_LONG);
                                        t.show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast t = Toast.makeText(tolist.this, "error :" + error.toString(), Toast.LENGTH_LONG);
                                    t.show();
                                }
                            }
                    )

            {
                //Overriding 覆寫父親既定的方法
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //操作Headers加入金鑰
                    //建構Map Collection
                    Map<String, String> headers = new HashMap<>();
                    //加入Key與value
                    //String key=AppUtility.getResourceKeyValue("CK");
                    //Log.i("Key",key);
                    headers.put("CK", "PKQO1V8D10R44L63U7");
                    return headers;
                }

            };
            requestQueue.add(jsonarrayRequest);
        }

        else {
            RequestQueue requestQueue = Volley.newRequestQueue(tolist.this);
            //下載ioT平台上資料
            JsonArrayRequest jsonarrayRequest = new JsonArrayRequest
                    (Request.Method.GET, ioturl, (String) null,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {

                                        int count = 0;

                                        for (int i = response.length() - 1; i > 0; i--) {
                                            JSONObject road = response.getJSONObject(i);
                                            String lat = road.getString("lat"); //緯度
                                            String lng = road.getString("lon"); //經度
                                            JSONArray values = road.getJSONArray("value"); //偵測數值
                                            String item0 = values.getString(0);//震度
                                            String item1 = values.getString(1);//等級
                                            String item2 = values.getString(2);//地址
                                            String item3 = values.getString(3);//上傳時間
                                            String item4 = values.getString(4);//使用者名稱
                                            String item5 = values.getString(5);//電話
                                            //將字串符合JSON Object格式 Parser成JSONObject 反序列化
                                            //使用Gson jar
                                            //Gson gson=new Gson();
                                            //反序列化 String convert to Custom Class Instance
                                            //String[] strings = gson.fromJson(item, String[].class);

                                            //只顯示該使用者資料
                                            if (account.equals(item4) || account == item4)
                                            {
                                                String name = item4;
                                                //判斷是否為地圖標記(無震度等級)
                                                if (item0.equals("00") && item1.equals("00"))
                                                {
                                                    String shake_level = item1;
                                                    if (search_thing.contains(shake_level))
                                                    {

                                                    }
                                                }
                                                //手機偵測(有震度等級)
                                                else
                                                {
                                                    String shake_level=item1;

                                                    //如果震度等級資料和搜尋輸入的一致
                                                    if(search_thing.contains(shake_level))
                                                    {
                                                        t.append("上傳方式: 手機偵測\n"+"震度: "+item0+"  等級: "+shake_level + "\n緯度: " + lat + "\n 經度: " + lng + "\n 地址: " + item2 + "\n 上傳時間: " + item3 + "\n 使用者名稱: " + name + "\n 電話: " + item5);
                                                        t.append("\n\n");
                                                    }
                                                }


                                                count++;
                                            }
                                            counttext.setText("您目前已上傳 " + count + " 筆資料至SkyTrain，\n感謝您的路見不平，拔\"手機\"相助 !");

                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast t = Toast.makeText(tolist.this, "e :" + e.getMessage(), Toast.LENGTH_LONG);
                                        t.show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast t = Toast.makeText(tolist.this, "error :" + error.toString(), Toast.LENGTH_LONG);
                                    t.show();
                                }
                            }
                    )

            {
                //Overriding 覆寫父親既定的方法
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //操作Headers加入金鑰
                    //建構Map Collection
                    Map<String, String> headers = new HashMap<>();
                    //加入Key與value
                    //String key=AppUtility.getResourceKeyValue("CK");
                    //Log.i("Key",key);
                    headers.put("CK", "PKQO1V8D10R44L63U7");
                    return headers;
                }

            };
            requestQueue.add(jsonarrayRequest);
        }
    }

String abc;
    //如果按下地址搜尋按鈕
    public void pressok2(View view)
    {
        t.setText("");

        //取得 edittext內的值
         search=(EditText)findViewById(R.id.search);
        search_thing = search.getText().toString().trim();


        //如果輸入框未有資料
        if (search_thing == null || search_thing.equals(""))
        {
            Toast.makeText(tolist.this, "請輸入想查詢的地址或震度的等級", Toast.LENGTH_LONG).show();
            RequestQueue requestQueue = Volley.newRequestQueue(tolist.this);
            //下載ioT平台上資料
            JsonArrayRequest jsonarrayRequest = new JsonArrayRequest
                    (Request.Method.GET, ioturl, (String) null,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {

                                        int count=0;

                                        for (int i = response.length()-1; i > 0; i--) {
                                            JSONObject road = response.getJSONObject(i);
                                            String lat = road.getString("lat"); //緯度
                                            String lng = road.getString("lon"); //經度
                                            JSONArray values = road.getJSONArray("value"); //偵測數值
                                            String item0 = values.getString(0);
                                            String item1 = values.getString(1);
                                            String item2 = values.getString(2);
                                            String item3 = values.getString(3);
                                            String item4 = values.getString(4);
                                            String item5 = values.getString(5);
                                            //將字串符合JSON Object格式 Parser成JSONObject 反序列化
                                            //使用Gson jar
                                            //Gson gson=new Gson();
                                            //反序列化 String convert to Custom Class Instance
                                            //String[] strings = gson.fromJson(item, String[].class);

                                            if(account.equals(item4)||account==item4)
                                            {
                                                String name=item4;
                                                if(item0.equals("00")&&item1.equals("00"))
                                                {
                                                    t.append("上傳方式: 地圖標記\n"+"緯度: " + lat + "\n 經度: " + lng + "\n 地址: " + item2 + "\n 上傳時間: " + item3 + "\n 使用者名稱: " + name + "\n 電話: " + item5);
                                                    t.append("\n\n");
                                                }
                                                else
                                                {
                                                    t.append("上傳方式: 手機偵測\n"+"震度: " + item0 + "  等級: " + item1 + "\n 緯度: " + lat + "\n 經度: " + lng + "\n 地址: " + item2 + "\n 上傳時間: " + item3 + "\n 使用者名稱: " + name + "\n 電話: " + item5);
                                                    t.append("\n\n");
                                                }

                                                count++;
                                            }
                                          counttext.setText("您目前已上傳 "+count+" 筆資料至SkyTrain，\n感謝您的路見不平，拔\"手機\"相助 !");


                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast t = Toast.makeText(tolist.this, "e :" + e.getMessage(), Toast.LENGTH_LONG);
                                        t.show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast t = Toast.makeText(tolist.this, "error :" + error.toString(), Toast.LENGTH_LONG);
                                    t.show();
                                }
                            }
                    )

            {
                //Overriding 覆寫父親既定的方法
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //操作Headers加入金鑰
                    //建構Map Collection
                    Map<String, String> headers = new HashMap<>();
                    //加入Key與value
                    //String key=AppUtility.getResourceKeyValue("CK");
                    //Log.i("Key",key);
                    headers.put("CK", "PKQO1V8D10R44L63U7");
                    return headers;
                }

            };
            requestQueue.add(jsonarrayRequest);
        }

        else {
            RequestQueue requestQueue = Volley.newRequestQueue(tolist.this);
            //下載ioT平台上資料
            JsonArrayRequest jsonarrayRequest = new JsonArrayRequest
                    (Request.Method.GET, ioturl, (String) null,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {

                                        int count = 0;

                                        for (int i = response.length() - 1; i > 0; i--) {
                                            JSONObject road = response.getJSONObject(i);
                                            String lat = road.getString("lat"); //緯度
                                            String lng = road.getString("lon"); //經度
                                            JSONArray values = road.getJSONArray("value"); //偵測數值
                                            String item0 = values.getString(0);//震度
                                            String item1 = values.getString(1);//等級
                                            String item2 = values.getString(2);//地址
                                            String item3 = values.getString(3);//上傳時間
                                            String item4 = values.getString(4);//使用者名稱
                                            String item5 = values.getString(5);//電話
                                            //將字串符合JSON Object格式 Parser成JSONObject 反序列化
                                            //使用Gson jar
                                            //Gson gson=new Gson();
                                            //反序列化 String convert to Custom Class Instance
                                            //String[] strings = gson.fromJson(item, String[].class);

                                            //只顯示該使用者資料
                                            if (account.equals(item4) || account == item4)
                                            {
                                                String name = item4;
                                                //判斷是否為地圖標記
                                                if (item0.equals("00") && item1.equals("00"))
                                                {
                                                    String shake_address=item2;
                                                    if (contains(search_thing, item2)==true)
                                                    {
                                                        t.append("上傳方式: 地圖標記"+"\n緯度: " + lat + "\n 經度: " + lng + "\n 地址: " + shake_address+ "\n 上傳時間: " + item3 + "\n 使用者名稱: " + name + "\n 電話: " + item5);
                                                        t.append("\n\n");
                                                    }
                                                }
                                                //手機偵測
                                                else
                                                {
                                                    String shake_address=item2;
                                                    //如果地址資料和搜尋輸入的一致
                                                    if(contains(search_thing, item2)==true)
                                                    {
                                                        t.append("上傳方式: 手機偵測\n"+"震度: "+item0+"  等級: "+item1 + "\n緯度: " + lat + "\n 經度: " + lng + "\n 地址: " + shake_address + "\n 上傳時間: " + item3 + "\n 使用者名稱: " + name + "\n 電話: " + item5);
                                                        t.append("\n\n");
                                                    }
                                                }


                                                count++;
                                            }
                                            counttext.setText("您目前已上傳 " + count + " 筆資料至SkyTrain，\n感謝您的路見不平，拔\"手機\"相助 !");

                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast t = Toast.makeText(tolist.this, "e :" + e.getMessage(), Toast.LENGTH_LONG);
                                        t.show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast t = Toast.makeText(tolist.this, "error :" + error.toString(), Toast.LENGTH_LONG);
                                    t.show();
                                }
                            }
                    )

            {
                //Overriding 覆寫父親既定的方法
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //操作Headers加入金鑰
                    //建構Map Collection
                    Map<String, String> headers = new HashMap<>();
                    //加入Key與value
                    //String key=AppUtility.getResourceKeyValue("CK");
                    //Log.i("Key",key);
                    headers.put("CK", "PKQO1V8D10R44L63U7");
                    return headers;
                }

            };
            requestQueue.add(jsonarrayRequest);
        }


    }

    //回偵測畫面
    public void changeProcess4(View view) {


        if(activity_num.equals("1")||activity_num=="1")
        {
            Intent intent = new Intent();
            intent.setClass(tolist.this, MainActivity.class);
            startActivity(intent);
            tolist.this.finish();
        }
        else
        {
            Intent intent = new Intent();
            intent.setClass(tolist.this, toStart.class);
            startActivity(intent);
            tolist.this.finish();
        }

    }

    //比較字串                                                   輸入值            上傳 資料
    private boolean contains(String result, String key)
    {
        int searchLoc = key.indexOf(result);

        //如果上傳資料有包含有輸入值
        if(searchLoc!=-1)
        {
            return true;
        }

        return false;
    }

    private static Boolean isExit = false;

    private static Boolean hasTask = false;


    //判斷是否要退出 app
    Timer timerExit = new Timer();

    TimerTask task = new TimerTask() {

        @Override

        public void run() {

            isExit = false;

            hasTask = true;

        }

    };


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        // 判斷是否按下Back
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 是否要退出
            if (isExit == false) {

                isExit = true; //記錄下一次要退出

                Toast.makeText(this, "再按一次離開 SkyTrain"

                        , Toast.LENGTH_SHORT).show();

                // 如果超過兩秒則恢復預設值
                if (!hasTask) {

                    timerExit.schedule(task, 2000);

                }

            } else {

                finish(); // 離開程式

                System.exit(0);

            }

        }
        return false;
    }




    protected void onPause() {
        super.onPause();
    }

/*    public static String String2Json(String s)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.toCharArray()[i];
            switch (c)
            {
                case '.':
                    sb.append("."); break;
                case '-':
                    sb.append("-"); break;
                case '/':
                    sb.append("\\/"); break;
                case '\b':
                    sb.append("\\b"); break;
                case '\f':
                    sb.append("\\f"); break;
                case '\n':
                    sb.append("\\n"); break;
                case '\r':
                    sb.append("\\r"); break;
                case '\t':
                    sb.append("\\t"); break;
                default:
                    if ((c >= 0 && c <= 31)||c ==127)//在ASCⅡ码中，第0～31号及第127号(共33个)是控制字符或通讯专用字符
                    {

                    }
                    else
                    {
                        sb.append(c);
                    }
                    break;
            }
        }
        return sb.toString();
    }
*/


}



