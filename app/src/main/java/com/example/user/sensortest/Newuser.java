package com.example.user.sensortest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.sensortest.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Newuser extends Activity {

    Button connect1; //返回
    Button connect2; //確定
    private EditText editphone, editemail, editaccount, editpassword, editpersonid;
    private ProgressBar loading;
    String outputstr;
    private String URL_REGIST = "http://163.18.62.49/Android_login_register/register.php";

    // public static final String KEY_USERNAME="name";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ACCOUNT = "account";
    public static final String KEY_PERSON_ID = "personid";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newuser);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //loading=findViewById(R.id.loading);
        // editname=findViewById(R.id.name);
        editphone = findViewById(R.id.phone);
        editemail = findViewById(R.id.email);
        editaccount = findViewById(R.id.account2);
        editpassword = findViewById(R.id.password2);
        editpersonid = findViewById(R.id.personid2);


        //點擊按鈕後回登入畫面
        connect1 = (Button) findViewById(R.id.btnback);
        connect1.setOnClickListener(new Button.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Newuser.this, userlogin.class);
                startActivity(intent);
                Newuser.this.finish();
            }
        });


        /*進入此activity時EditText不先跳出鍵盤*/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    //按下確定按鈕後
    public void updatedata(View v) {

        if (editaccount.getText().toString().matches("") || editpassword.getText().toString().matches("") || editphone.getText().toString().matches("") || editemail.getText().toString().matches("") || editpersonid.getText().toString().matches("")) {
            Toast.makeText(Newuser.this, "請輸入完整的使用者資料歐!", Toast.LENGTH_LONG).show();
        } else {
            ifidsame();
        }

    }

    //判斷帳號是否重複
    private void ifidsame() {
        final String account = editaccount.getText().toString().trim();
        final String password = editpassword.getText().toString().trim();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://163.18.62.49/Android_login_register/newuser.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.contains("ok")) {
                    Toast.makeText(Newuser.this, "帳號名稱 " + account + " 有人使用囉!", Toast.LENGTH_LONG).show();
                } else {
                    //上傳使用者資料
                    registerUser();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Toast.makeText(userlogin.this,"登入失敗:"+error.toString(),Toast.LENGTH_LONG).show();

                    }
                })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_ACCOUNT, account);
                params.put(KEY_PASSWORD, password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //上傳使用者資料
    private void registerUser() {
        //  final String name=editname.getText().toString().trim();
        final String phone = editphone.getText().toString().trim();
        final String email = editemail.getText().toString().trim();
        final String account = editaccount.getText().toString().trim();
        final String password = editpassword.getText().toString().trim();
        final String personid = editpersonid.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Newuser.this, "註冊成功", Toast.LENGTH_LONG).show();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Newuser.this, "註冊失敗:\n" + error.toString(), Toast.LENGTH_LONG).show();
                    }
                })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // params.put(KEY_USERNAME,name);
                params.put(KEY_PHONE, phone);
                params.put(KEY_EMAIL, email);
                params.put(KEY_ACCOUNT, account);
                params.put(KEY_PASSWORD, password);
                params.put(KEY_PERSON_ID, personid);
                return params;
            }
        };


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    //回登入畫面
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵

            Intent intent = new Intent();
            intent.setClass(Newuser.this, userlogin.class);
            startActivity(intent);
            Newuser.this.finish();
        }

        return true;
    }


}