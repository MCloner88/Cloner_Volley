package com.example.clonervolley.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.clonervolley.R;
import com.example.clonervolley.app.app;

public class ShowImage_activity extends AppCompatActivity {

    ImageView imageView , imageview_back ;

    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        link = getIntent().getStringExtra("link");


        imageview_back = findViewById(R.id.imageview_back);
        imageView       = findViewById(R.id.imageView);


        Glide.with(this).load(app.BASE_URL + link).into(imageView);

        imageview_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();
    }
}