package com.example.user.sensortest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class userlogin extends Activity {
    private EditText et;
    private CheckBox cb;

    EditText account, password;
    Button btnlogin;
    ProgressBar loding;
    private static String urllogin = "http://163.18.62.49:80/Android_login_register/login.php";

    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ACCOUNT = "account";
    sessionManager sessionManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userlogintoapp);

        loding = findViewById(R.id.loding);
        account = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);

        sessionManager = new sessionManager(this);


        /*帳密登入*/
        btnlogin = (Button) findViewById(R.id.button1);
        btnlogin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                final String mAccount = account.getText().toString().trim();
                final String mPass = password.getText().toString().trim();

                if (!mAccount.isEmpty() && !mPass.isEmpty()) {
                    Login(mAccount, mPass);
                } else if (!mAccount.isEmpty() && mPass.isEmpty()) {
                    password.setError("請輸入密碼");
                } else if (!mPass.isEmpty() && mAccount.isEmpty()) {
                    account.setError("請輸入帳號");
                } else {
                    account.setError("請輸入帳號");
                    password.setError("請輸入密碼");
                }
            }
        });
        /*註冊新帳號*/
        Button b2 = (Button) findViewById(R.id.button2);
        b2.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                /* new 一個Intent 物件，並指定要啟動的class */
                Intent intent = new Intent();
                intent.setClass(userlogin.this, Newuser.class);
                /* 呼叫一個新的Activity */
                startActivity(intent);
                /* 關閉原本的Activity */
                userlogin.this.finish();
            }
        });
        /*密碼顯示隱藏*/
        et = (EditText) findViewById(R.id.editText2);
        cb = (CheckBox) findViewById(R.id.checkBox);
        /* 設定CheckBox 的OnCheckedChangeListener */
        cb.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (cb.isChecked()) {
                    /* 設定EditText 的內容為可見的 */
                    et.setTransformationMethod(
                            HideReturnsTransformationMethod.getInstance());
                } else {
                    /* 設定EditText 的內容為隱藏的 */
                    et.setTransformationMethod(
                            PasswordTransformationMethod.getInstance());
                }
            }
        });

        /*進入此activity時EditText不先跳出鍵盤*/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void Login(final String account, final String password) {
        loding.setVisibility(View.VISIBLE);
        btnlogin.setVisibility(View.GONE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, urllogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("")) {
                    Toast.makeText(userlogin.this, "錯誤的帳號或密碼", Toast.LENGTH_LONG).show();
                    loding.setVisibility(View.GONE);
                    btnlogin.setVisibility(View.VISIBLE);

                     /*   SharedPreferences setting =
                                getSharedPreferences("atm", MODE_PRIVATE);
                        setting.edit()
                                .putString("PREF_USERID", account)
                                .commit();*/

                } else {
                    String[] array = response.split(" ");
                    String name = array[0].trim();
                    String email = array[1].trim();
                    sessionManager.createSession(name, email);
                    Toast.makeText(userlogin.this, name + " 登入成功", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent();
                    intent.setClass(userlogin.this, MainActivity.class);
                    /* 呼叫一個新的Activity */
                    startActivity(intent);
                    /* 關閉原本的Activity */
                    userlogin.this.finish();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loding.setVisibility(View.GONE);
                        btnlogin.setVisibility(View.VISIBLE);
                        Toast.makeText(userlogin.this, "登入失敗:" + error.toString(), Toast.LENGTH_LONG).show();

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


}