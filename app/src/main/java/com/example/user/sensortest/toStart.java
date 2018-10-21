package com.example.user.sensortest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.*;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.app.Activity;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.toolbox.Volley;
import com.example.user.sensortest.DHT;

import java.net.Authenticator;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.util.Calendar;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.function.ToLongBiFunction;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;


public class toStart extends AppCompatActivity {
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
    TextView network;
    TextView number;
    public static final int DHTREQUEST = 1;
    public static final int DHTRESPONSE = 2;
    String num_to_string = "";
    String msg = null;
    String requestUrl = "https://iot.cht.com.tw/iot/v1/device/10482012315/rawdata";  //上傳網址
    String requestMethod = "POST";
    private TextView mTextView01, longitude_txt, latitude_txt;
    private boolean getService = false;     //是否已開啟定位服務
    private Location location;
    private String bestProvider = LocationManager.GPS_PROVIDER;
    double lat; //緯度
    double lng; //經度
    private LocationManager locationManager;
    String latLng;
    String provider;
    String returnAddress; //地址
    double yy;
    String accountname;
    sessionManager sessionManager;
    String mName;
    String mPone;
    final String KEY_ACCOUNT = "account";
    String passname;
    TextView gr;
    TextView re1;
    TextView re2;
    TextView re3;
    TextView re4;
    TextView re5;
    String imagechange;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigation_view;
    private ActionBar actionBar;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private int count=0;
    Handler mHandler;

    //偵測跳動等級畫面延遲3秒
    final Runnable runnable = new Runnable() {

        public void run() {
            // TODO Auto-generated method stub
            // 需要背景作的事
            if (count < 3)
            {
                switch (imagechange)
                {
                    case("re1"):
                        gr.setVisibility(View.GONE);//先設定layout為平坦畫面
                        re1.setVisibility(View.VISIBLE);
                        re2.setVisibility(View.GONE);
                        re3.setVisibility(View.GONE);
                        re4.setVisibility(View.GONE);
                        re5.setVisibility(View.GONE);
                        break;

                    case("re2"):
                        gr.setVisibility(View.GONE);//先設定layout為平坦畫面
                        re1.setVisibility(View.GONE);
                        re2.setVisibility(View.VISIBLE);
                        re3.setVisibility(View.GONE);
                        re4.setVisibility(View.GONE);
                        re5.setVisibility(View.GONE);
                        break;

                    case("re3"):
                        gr.setVisibility(View.GONE);//先設定layout為平坦畫面
                        re1.setVisibility(View.GONE);
                        re2.setVisibility(View.GONE);
                        re3.setVisibility(View.VISIBLE);
                        re4.setVisibility(View.GONE);
                        re5.setVisibility(View.GONE);
                        break;

                    case("re4"):
                        gr.setVisibility(View.GONE);//先設定layout為平坦畫面
                        re1.setVisibility(View.GONE);
                        re2.setVisibility(View.GONE);
                        re3.setVisibility(View.GONE);
                        re4.setVisibility(View.VISIBLE);
                        re5.setVisibility(View.GONE);
                    break;
                    case("re5"):
                        gr.setVisibility(View.GONE);//先設定layout為平坦畫面
                        re1.setVisibility(View.GONE);
                        re2.setVisibility(View.GONE);
                        re3.setVisibility(View.GONE);
                        re4.setVisibility(View.GONE);
                        re5.setVisibility(View.VISIBLE);
                        break;


                }
                count++;
                mHandler.postDelayed(runnable, 1000);
            }
            if (count ==3) {
                gr.setVisibility(View.VISIBLE);//先設定layout為平坦畫面
                re1.setVisibility(View.GONE);
                re2.setVisibility(View.GONE);
                re3.setVisibility(View.GONE);
                re4.setVisibility(View.GONE);
                re5.setVisibility(View.GONE);
            }
        }
    };
    String two="2";
    @Override
    @SuppressLint("RestrictedApi")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mHandler = new Handler();
        //mHandler.post(runnable);

        gr = findViewById(R.id.nocurrentrecord);
        gr.setVisibility(View.VISIBLE);//先設定layout為平坦畫面

        re1 = findViewById(R.id.current_record);
        re1.setVisibility(View.GONE);

        re2 = findViewById(R.id.current_record2);
        re2.setVisibility(View.GONE);

        re3 = findViewById(R.id.current_record3);
        re3.setVisibility(View.GONE);

        re4 = findViewById(R.id.current_record4);
        re4.setVisibility(View.GONE);

        re5 = findViewById(R.id.current_record5);
        re5.setVisibility(View.GONE);


        SensorActivity();   //註冊感測器(啟動);
        btnshowdata = (Button) findViewById(R.id.roadlist);
        tx = (TextView) findViewById(R.id.empty);  //經緯度 地址 Text
        network = (TextView) findViewById(R.id.empty4); //網路連接狀態 Text
        number = (TextView) findViewById(R.id.tvMsg); //震度 Text


        checkNetworkConnection(); //檢查網路是否連線

       /* findvalue e1=new findvalue();
        e1.setId("日期");
        e1.setName("震度值");
        e1.setAddress("顛簸等級");
        e1.setlocation("經緯度");
        data.add(e1);
        */


        //取得使用者名稱

       final  String account =constant.n;


      /*  if( intent.getStringExtra("mName")!=null)
        {
           account = intent.getStringExtra("mName");
        }
        else
        {
            account=constant.n;
        }*/

        //final String finalaccount=account;



        //取得使用者電話
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://163.18.62.49:80/Android_login_register/user.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //如果不是error
                if (!response.contains("Error")) {
                    String[] array = response.split(",");

                        mPone = array[2];

                } else {
                    //Toast.makeText(touser.this,response, Toast.LENGTH_LONG).show();
                    //tx.setText(response);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(touser.this,"失敗:\n"+error.toString(),Toast.LENGTH_LONG).show();
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // params.put(KEY_USERNAME,name);
                params.put(KEY_ACCOUNT, account);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


mName=constant.n;
constant.p=mPone;

        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
//drawerLayout.bringToFront();
        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);


        navigation_view = (NavigationView) findViewById(R.id.navigation_view);
        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                // 點選時收起選單
                drawerLayout.closeDrawer(GravityCompat.START);

                // 取得選項id
                int id = item.getItemId();

                // 前往使用者資料畫面
                if (id == R.id.nav_account) {
                    // 按下「首頁」要做的事
                    //Toast.makeText(MainActivity.this, "使用者資料", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(toStart.this, touser.class);
                    constant.n=mName;
                    constant.p=mPone;
                    intent.putExtra("activity_num",two);
                    toStart.this.startActivity(intent);
                    toStart.this.finish();
                    return true;
                }
                // 前往已上傳資料清單
                else if (id == R.id.nav_list) {
                    // 按下「使用說明」要做的事
                    //Toast.makeText(MainActivity.this, "已上傳資料清單", Toast.LENGTH_SHORT).show();
                    //採用雙向切換(來源Activity 可以傳遞資訊到Target /Target處理好退掉 也可以回傳資訊到這一個Activity)
                    //1.建構Intent
                    Intent intent = new Intent(toStart.this, tolist.class);
                    //2.帶URL資料到目標Activity
                    //2.1使用機動問出資源管理物件
                    Resources resources = toStart.this.getResources();
                    String hostedString = resources.getString(R.string.hosted);
                    String dhtservice = resources.getString(R.string.dhtservice);
                    String url = String.format("%s%s", hostedString, dhtservice);
                    Log.i("網址", url);
                    //帶資訊 OverLoading Method
                    intent.putExtra("url", url);
                    intent.putExtra("activity_num",two);
                    //有去有回 狀態(state) 回來要有Callback
                    //如何去 帶旗標
                    toStart.this.startActivityForResult(intent, MainActivity.DHTREQUEST);//第二參數當作Flag 使用Callback程序判斷
                    toStart.this.finish();

                    return true;
                }
                //前往地圖標記
                else if (id == R.id.nav_map) {
                    // 按下「使用說明」要做的事
                    //Toast.makeText(MainActivity.this, "地圖標記", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(toStart.this, mapsActivity.class);
                    constant.n = mName;
                    intent.putExtra("activity_num",two);
                    toStart.this.startActivity(intent);
                    toStart.this.finish();
                    return true;
                }
                //關於SkyTrain訊息框跳出
                else if (id == R.id.nav_about) {
                    // 按下「使用說明」要做的事
                    //Toast.makeText(MainActivity.this, "關於SkyTrain", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(toStart.this)
                            .setTitle("關於功能")
                            .setMessage("此APP藉由偵測手機震動來判斷路面坑洞\n如果震動數值幅度大於14.5則紀錄為坑洞\n我們把震動幅度分為五個等級\n" +
                                    "讓施工單位或使用者能了解各地區域的路面坑洞嚴重程度\n此APP利用手機內建的三軸感測器進行測量\n***偵測時請盡量直立手機以確保其精準度")

                            .setPositiveButton("了解",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {

                                        }
                                    })
                            .setNegativeButton("",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            // TODO Auto-generated method stub

                                        }
                                    }).show();
                    return true;
                }
                //坑洞震度等級說明訊息框跳出
                /*else if (id == R.id.nav_level) {
                    // 按下「使用說明」要做的事
                    Toast.makeText(toStart.this, "坑洞震度等級說明", Toast.LENGTH_SHORT).show();
                    return true;
                }*/


                return false;
            }
        });



        //取得定位服務
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //創建一個criteria物件
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//設置不需要獲取海拔方向資料
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
//設置允許產生資費
        criteria.setCostAllowed(true);
//要求低耗電
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        provider = locationManager.getBestProvider(criteria, true);

        Log.i(TAG, "we choose " + provider);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
//第一次獲得設備的位置
        updateLocation(location);
//重要函數，監聽資料測試  (五秒五公尺監聽一次位置)
        locationManager.requestLocationUpdates(provider, 1000, 10,
                locationListener);


        //點擊按鈕後暫停紀錄
        connect = (Button) findViewById(R.id.stop);
        connect.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent();
                constant.n=mName;
                intent.setClass(toStart.this, MainActivity.class);
                startActivity(intent);
                toStart.this.finish();
            }
        });

    }


    //創建一個座標事件監聽器
    private final LocationListener locationListener = new LocationListener() {

        public void onLocationChanged(Location location) {

            updateLocation(location);

        }

        public void onProviderDisabled(String provider) {

            updateLocation(null);

            Log.i(TAG, "Provider now is disabled..");

        }

        public void onProviderEnabled(String provider) {

            Log.i(TAG, "Provider now is enabled..");

        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    };

    //更新目前座標
    private void updateLocation(Location location) {

        tx.setText("定位中  請稍後.....");
        if (location != null) {

            lat = location.getLatitude();
            lng = location.getLongitude();

            //顯示目前地址
            Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> lstAddress = null;
            try {
                lstAddress = gc.getFromLocation(lat, lng, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            returnAddress = lstAddress.get(0).getAddressLine(0);

            latLng = "目前所在位置\n\n" + returnAddress;
            tx.setText(latLng);

        } else {

            latLng = "無法定位您的位置";
            tx.setText(latLng);
        }

        Log.i(TAG, "The location has changed..");

        Log.i(TAG, "Your Location:" + latLng);
    }


    //感測器
    public SensorEventListener listener = new SensorEventListener() {
        @Override

        //如果 sensor 值有改變  被觸發的事件會傳遞過來

        public void onSensorChanged(SensorEvent event) {

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
            yy = bd.doubleValue();  //偵測的數值  (四捨五入到小數後第一位)


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

            if (yy < 11)

            {
                /*
                gr.setVisibility(View.VISIBLE);
                re1.setVisibility(View.GONE);
                re2.setVisibility(View.GONE);
                re3.setVisibility(View.GONE);
                re4.setVisibility(View.GONE);
                re5.setVisibility(View.GONE);
                */

            } else

            {
                num_to_string = ""; // json格式上傳資料

                // 獲取當前時間
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH點mm");
                Date curDate = new Date(System.currentTimeMillis());
                String currenttime = formatter.format(curDate);

                if (yy >= 11 && yy < 13.5) {

                    num_to_string =
                            "[ {\"id\":\"road_test19\",\"lat\":" + Double.toString(lat) + ",\"lon\":" + Double.toString(lng) + ",\"save\":true,\"value\":[\"" + Double.toString(yy) + "\",\"1\",\"" + returnAddress + "\",\"" + currenttime + "\",\"" + mName + "\",\"" + mPone + "\"] } ]";

                   /* re1.setVisibility(View.VISIBLE);
                    gr.setVisibility(View.GONE);
                    re2.setVisibility(View.GONE);
                    re3.setVisibility(View.GONE);
                    re4.setVisibility(View.GONE);
                    re5.setVisibility(View.GONE);*/
                    imagechange="re1";
                    httpRequest(requestUrl, requestMethod, num_to_string);
                } else if (yy >= 13.5 && yy < 15.5) {

                    num_to_string =
                            "[ {\"id\":\"road_test19\",\"lat\":" + Double.toString(lat) + ",\"lon\":" + Double.toString(lng) + ",\"save\":true,\"value\":[\"" + Double.toString(yy) + "\",\"2\",\"" + returnAddress + "\",\"" + currenttime + "\",\"" + mName + "\",\"" + mPone + "\"] } ]";
                    /*re2.setVisibility(View.VISIBLE);
                    gr.setVisibility(View.GONE);
                    re1.setVisibility(View.GONE);
                    re3.setVisibility(View.GONE);
                    re4.setVisibility(View.GONE);
                    re5.setVisibility(View.GONE);*/
                    imagechange="re2";
                    httpRequest(requestUrl, requestMethod, num_to_string);
                } else if (yy >= 15.5 && yy < 17.5) {

                    num_to_string =
                            "[ {\"id\":\"road_test19\",\"lat\":" + Double.toString(lat) + ",\"lon\":" + Double.toString(lng) + ",\"save\":true,\"value\":[\"" + Double.toString(yy) + "\",\"3\",\"" + returnAddress + "\",\"" + currenttime + "\",\"" + mName + "\",\"" + mPone + "\"] } ]";
                   /* re3.setVisibility(View.VISIBLE);
                    gr.setVisibility(View.GONE);
                    re1.setVisibility(View.GONE);
                    re2.setVisibility(View.GONE);
                    re4.setVisibility(View.GONE);
                    re5.setVisibility(View.GONE);*/
                    imagechange="re3";
                    httpRequest(requestUrl, requestMethod, num_to_string);
                } else if (yy >= 17.5 && yy < 18.5) {

                    num_to_string =
                            "[ {\"id\":\"road_test19\",\"lat\":" + Double.toString(lat) + ",\"lon\":" + Double.toString(lng) + ",\"save\":true,\"value\":[\"" + Double.toString(yy) + "\",\"4\",\"" + returnAddress + "\",\"" + currenttime + "\",\"" + mName + "\",\"" + mPone + "\"] } ]";
                   /* re4.setVisibility(View.VISIBLE);
                    gr.setVisibility(View.GONE);
                    re1.setVisibility(View.GONE);
                    re2.setVisibility(View.GONE);
                    re3.setVisibility(View.GONE);
                    re5.setVisibility(View.GONE);*/
                    imagechange="re4";
                    httpRequest(requestUrl, requestMethod, num_to_string);
                } else if (yy >= 18.5) {

                    num_to_string =
                            "[ {\"id\":\"road_test19\",\"lat\":" + Double.toString(lat) + ",\"lon\":" + Double.toString(lng) + ",\"save\":true,\"value\":[\"" + Double.toString(yy) + "\",\"5\",\"" + returnAddress + "\",\"" + currenttime + "\",\"" + mName + "\",\"" + mPone + "\"] } ]";
                   /* re5.setVisibility(View.VISIBLE);
                    gr.setVisibility(View.GONE);
                    re1.setVisibility(View.GONE);
                    re2.setVisibility(View.GONE);
                    re3.setVisibility(View.GONE);
                    re4.setVisibility(View.GONE);*/
                    imagechange="re5";
                    httpRequest(requestUrl, requestMethod, num_to_string);
                }
                //httpRequest(requestUrl, requestMethod, num_to_string);


            }
            number.setText(Double.toString(yy));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            //當 sensor 的精準度改變時會呼叫此方法

        }
    };


    //上傳感測資料至ioT平台
    public void httpRequest(String requestUrl, String requestMethod, String outputStr) {
        String result = null;
        StringBuffer buffer = new StringBuffer();
        String ou = outputStr;
        HttpsURLConnection httpUrlConn = null;
        try {

            count=0;
            if (mHandler != null) {
                mHandler.removeCallbacks(runnable);
            }
            mHandler.post(runnable);

            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
        /*TrustManager[] tm = {new MyX509TrustManager()};
        SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
        sslContext.init(null, tm, new java.security.SecureRandom());
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        SSLSocketFactory ssf = sslContext.getSocketFactory();*/

            URL ioturl = new URL(requestUrl);
            httpUrlConn = (HttpsURLConnection) ioturl.openConnection();
            //httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setConnectTimeout(50000);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);
            httpUrlConn.setRequestProperty("CK", "PKEJNLQMANM2M7KWNP");//设置请求头
            httpUrlConn.setRequestProperty("Content-Type", "application/json");//设置参数类型是json格式


            //httpUrlConn.setRequestProperty("Content-Length", "86");

            httpUrlConn.connect();

            // 当有数据需要提交时

            OutputStream outputStream = new BufferedOutputStream(httpUrlConn.getOutputStream());
            // 注意编码格式，防止中文乱码
            outputStream.write(ou.getBytes("utf-8"));
            outputStream.flush();
            outputStream.close();


            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            // inputStream = null;

            result = buffer.toString();
            //Toast re = Toast.makeText(toStart.this, "上傳成功 " + result, Toast.LENGTH_LONG);
           // re.show();

            /*
            TimerTask task = new TimerTask()
            {
                @Override
                public void run()
                {
                    TextView gr=(TextView)findViewById(R.id.norecord);
                    TextView re1=(TextView)findViewById(R.id.current_record);
                    TextView re2=(TextView)findViewById(R.id.current_record2);
                    TextView re3=(TextView)findViewById(R.id.current_record3);
                    TextView re4=(TextView)findViewById(R.id.current_record4);
                    TextView re5=(TextView)findViewById(R.id.current_record5);
                    re1.setVisibility(View.VISIBLE);
                    gr.setVisibility(View.GONE);
                    re2.setVisibility(View.GONE);
                    re3.setVisibility(View.GONE);
                    re4.setVisibility(View.GONE);
                    re5.setVisibility(View.GONE);
                }
            };
            */




            httpUrlConn.disconnect();
            // jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            ce.printStackTrace();
            Toast re = Toast.makeText(toStart.this, "上傳失敗: " + ce.toString(), Toast.LENGTH_LONG);
            re.show();
            // log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            e.printStackTrace();
            Toast re = Toast.makeText(toStart.this, "上傳失敗: " + e.toString(), Toast.LENGTH_LONG);
            re.show();
            // log.error("https request error:{}", e);
        }
    }



    //註冊感測器(啟動)
    public void SensorActivity() {
        sensorMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }


    //確定網路是否連線
    public boolean checkNetworkConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean isConnected = false;
        if (networkInfo != null && (isConnected = networkInfo.isConnected())) {
            // show "Connected" & type of network "WIFI or MOBILE"
            network.setText("Network Connected");
            // change background color to green
            network.setBackgroundColor(0xFF7CCC26);


        } else {
            // show "Not Connected"
            network.setText("No Connected");
            // change background color to red
            network.setBackgroundColor(0xFFFF0000);
        }

        return isConnected;
    }


    //註冊感測器監聽
    @Override
    protected void onResume() {
        super.onResume();
        sensorMgr.registerListener(listener,
                sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    //關閉Activity後解除註冊感測器監聽
    @Override
    protected void onPause() {
        super.onPause();
        sensorMgr.unregisterListener(listener);

        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
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


    //切換至偵測清單畫面
    public void changeProcess(View view) {
        showDHTData(btnshowdata);
    }

    //切換至使用者基本資料畫面
    public void changeProcess2(View view) {
        Intent intent = new Intent(toStart.this, touser.class);
        constant.n = mName;
        intent.putExtra("activity_num",two);
        this.startActivity(intent);
        toStart.this.finish();
    }

    //切換至地圖畫面
    public void changeProcess3(View view) {

        Intent intent = new Intent(toStart.this, mapsActivity.class);
        constant.n = mName;
        intent.putExtra("activity_num",two);
        this.startActivity(intent);
        toStart.this.finish();
    }

    //按鈕簡易事件程序自動委派(onClick Method結構)
    public void showDHTData(View view) {
        //採用雙向切換(來源Activity 可以傳遞資訊到Target /Target處理好退掉 也可以回傳資訊到這一個Activity)
        //1.建構Intent
        Intent intent = new Intent(toStart.this, tolist.class);
        //2.帶URL資料到目標Activity
        //2.1使用機動問出資源管理物件
        Resources resources = this.getResources();
        String hostedString = resources.getString(R.string.hosted);
        String dhtservice = resources.getString(R.string.dhtservice);
        String url = String.format("%s%s", hostedString, dhtservice);
        Log.i("網址", url);
        //帶資訊 OverLoading Method
        intent.putExtra("url", url);
        constant.n=mName;
        intent.putExtra("activity_num",two);
        //有去有回 狀態(state) 回來要有Callback
        //如何去 帶旗標
        this.startActivityForResult(intent, toStart.DHTREQUEST); //第二參數當作Flag 使用Callback程序判斷
        toStart.this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //要自己處理
        if (requestCode == toStart.DHTREQUEST && resultCode == toStart.DHTRESPONSE) {
            //透過Intent取出回傳的狀態

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}




