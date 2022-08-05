package com.example.clonervolley.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clonervolley.R;

public class app {
    public static final String TAG = "Volley";
    public static final String BASE_URL = "http://alifeyzabadi.ir/Cloner_Volley/";

    public static void l(String message) {

        Log.e(TAG, message);
    }

    public static void t(String message) {
        Toast.makeText(application.getContext(), message, Toast.LENGTH_SHORT).show();
    }


    public static void successT(String message) {
        Toast toast = new Toast(application.getContext());
        View view = LayoutInflater.from(application.getContext()).inflate(R.layout.success_t, null);

        ImageView imageView = view.findViewById(R.id.img);
        TextView textView = view.findViewById(R.id.txt);

        imageView.setImageResource(R.drawable.ic_baseline_check_24);
        textView.setText(message);

        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 50);

        toast.show();
    }


    public static void failedT(String message) {
        Toast toast = new Toast(application.getContext());
        View view = LayoutInflater.from(application.getContext()).inflate(R.layout.failed_t, null);

        ImageView imageView = view.findViewById(R.id.img);
        TextView textView = view.findViewById(R.id.txt);

        imageView.setImageResource(R.drawable.ic_baseline_close_24);
        textView.setText(message);

        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 50);

        toast.show();
    }

    public  static Boolean isConected (){

        ConnectivityManager connectivityManager = (ConnectivityManager) application.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting() ;

    }

}
