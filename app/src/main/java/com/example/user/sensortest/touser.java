package com.example.user.sensortest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.sensortest.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class touser extends Activity {

    TextView tx;
    String phone;
    public static final String KEY_ACCOUNT = "account";
    String activity_num="";
     String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);

        tx = findViewById(R.id.persontext);

        //取得使用者名稱
        Intent intent = getIntent();
        account= constant.n;
        String ioturl = intent.getStringExtra("url");
        activity_num=intent.getStringExtra("activity_num");


        //取得使用者資料
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://163.18.62.49:80/Android_login_register/user.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //如果不是error
                if (!response.contains("Error")) {
                    //Toast.makeText(touser.this, response, Toast.LENGTH_LONG).show();
                    String[] array = response.split(",");
                    phone=array[2];
                    tx.setText(" 使用者名稱: " + array[0] + "\n\n 電話: " + array[2] + "\n\n Email: " + array[3] + "\n\n 身份證ID: " + array[4]);
                } else {
                    Toast.makeText(touser.this, response, Toast.LENGTH_LONG).show();
                    tx.setText(response);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(touser.this, "失敗:\n" + error.toString(), Toast.LENGTH_LONG).show();
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


    //回主畫面或偵測畫面
    public void backmain(View view) {


        if(activity_num.equals("1")||activity_num=="1")
        {
            Intent intent = new Intent();
            intent.setClass(touser.this, MainActivity.class);
            constant.n=account;
            constant.p=phone;
            startActivity(intent);
           touser.this.finish();
        }
        else
        {
            Intent intent = new Intent();
            intent.setClass(touser.this, toStart.class);
            constant.n=account;
            constant.p=phone;
            startActivity(intent);
            touser.this.finish();
        }

    }

    //關於坑洞終結者 提示框
   /* public void about(View view) {
        new AlertDialog.Builder(touser.this)
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

    }*/


}

