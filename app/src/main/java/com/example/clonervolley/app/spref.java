package com.example.clonervolley.app;

import android.content.Context;
import android.content.SharedPreferences;

public class spref {

    static SharedPreferences sharedPreferences;

    public static SharedPreferences getSharedPreferences(){
        if (sharedPreferences == null){
            sharedPreferences= application.getContext().getSharedPreferences(app.TAG, Context.MODE_PRIVATE);
        }
        return  sharedPreferences;
    }

    public static final String EMAIL = "email";
    public static final String REMMEBER_ME = "remmeber";

}
