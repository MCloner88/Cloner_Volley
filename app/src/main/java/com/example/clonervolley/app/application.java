package com.example.clonervolley.app;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;
import java.util.Locale;

public class application extends Application {

    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context=this;

        //getLanguage();
    }




    public static Context getContext(){
        return context;
    }


    private void getLanguage()
    {
        String language = Locale.getDefault().getDisplayLanguage();

        String font;

        if(language.equalsIgnoreCase("English"))
        {
            font = "font/pop.ttf";
        }else {
            font = "font/ir.ttf";
        }

       // setFont(font);
    }

    private void setFont(String font)
    {
        Typeface typeface = Typeface.createFromAsset(getAssets() , font);

        try {
            Field field = Typeface.class.getDeclaredField("MONOSPACE") ;
            field.setAccessible(true);
            field.set(null, typeface);
        }catch (Exception ignored){

        }
    }
}
