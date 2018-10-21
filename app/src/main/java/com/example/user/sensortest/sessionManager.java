package com.example.user.sensortest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.android.gms.cast.framework.SessionManager;

import java.util.HashMap;

public class sessionManager
{

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PREVATE_MODE=0;

    private static final String PREF_NAME="LOGIN";
    private static final String LOGIN="IS LOGIN";
    public static final String NAME="NAME";
    public static final String EMAIL="EMAIL";

    public sessionManager(Context context)
    {
        this.context=context;
        sharedPreferences=context.getSharedPreferences(PREF_NAME,PREVATE_MODE);
        editor=sharedPreferences.edit();
    }

    public void createSession(String name, String email)
    {
        editor.putBoolean(LOGIN,true);
        editor.putString(NAME,name);
        editor.putString(EMAIL,email);
        editor.apply();
    }


    public boolean isLoggin()
    {
        return sharedPreferences.getBoolean(LOGIN,false);
    }

    public void checkLogin()
    {
        if(!this.isLoggin())
        {
            Intent i=new Intent(context,userlogin.class);
            context.startActivity(i);
            ((MainActivity)context).finish();
        }
    }

    public HashMap<String,String> getUserDetail()
    {
        HashMap<String ,String>user=new HashMap<>();
        user.put(NAME,sharedPreferences.getString(NAME,null));
        user.put(EMAIL,sharedPreferences.getString(EMAIL,null));

        return user;
    }

    public void logout()
    {
        editor.clear();
        editor.commit();
        Intent i=new Intent(context,userlogin.class);
        context.startActivity(i);
        ((MainActivity)context).finish();
    }
}
