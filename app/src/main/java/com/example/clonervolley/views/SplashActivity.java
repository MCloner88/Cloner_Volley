package com.example.clonervolley.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.clonervolley.R;
import com.example.clonervolley.app.app;
import com.example.clonervolley.app.spref;

public class SplashActivity extends AppCompatActivity {

    ImageView image_splash;
    LinearLayout layout_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
        onclick();
    }

    private void init() {
        image_splash = findViewById(R.id.image_splash);
        layout_refresh = findViewById(R.id.layout_refresh);

        layout_refresh.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(app.isConected()){// Net Connect
                    if(spref.getSharedPreferences().getBoolean(spref.REMMEBER_ME,false)){
                        if(spref.getSharedPreferences().getString(spref.EMAIL,"").equals("")){
                            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this,image_splash,"splash");
                            Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                            startActivity(intent , activityOptionsCompat.toBundle());
                            finish();
                        }else {
                            startActivity(new Intent(SplashActivity.this,MainActivity.class));
                            finish();
                        }
                    }else {
                        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this,image_splash,"splash");
                        Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                        startActivity(intent , activityOptionsCompat.toBundle());
                        finish();
                    }
                }else {
                    layout_refresh.setVisibility(View.VISIBLE);
                }
            }
        },3000);
    }

    private void onclick() {
        layout_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });
    }
}