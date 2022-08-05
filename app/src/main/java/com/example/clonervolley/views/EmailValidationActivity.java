package com.example.clonervolley.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.example.clonervolley.R;
import com.example.clonervolley.app.app;
import com.example.clonervolley.app.spref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EmailValidationActivity extends AppCompatActivity implements View.OnClickListener {


    String email , password , code;

    ImageView image_back;
    Button btn_regester;
    PinView pinview;


    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_validation);


        getExtras();
        init();
        onclick();

    }


    private void getExtras() {

        email    = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        code     = getIntent().getStringExtra("code");
    }

    private void init(){

        image_back   =findViewById(R.id.image_back);
        pinview      =findViewById(R.id.pinview);
        btn_regester =findViewById(R.id.btn_regester);

        requestQueue = Volley.newRequestQueue(this);
    }

    private void onclick() {
        image_back.setOnClickListener(this);
        btn_regester.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if( v == image_back){
            finish();
        }
        if( v == btn_regester){
            String pinviewcode = pinview.getText().toString().trim();
            if(pinviewcode.equals(code)){
                register();
            }else {
                pinview.setLineColor(getResources().getColor(R.color.colorRed)) ;

            }
        }
    }


    private void register(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.BASE_URL + "register.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if(message.equals("ok")){// ok register

                        spref.getSharedPreferences().edit().putString(spref.EMAIL , email);
                        Intent intent = new Intent(EmailValidationActivity.this , MainActivity.class);
                        startActivity(intent);
                        finish();

                    }else {//failed register
                        app.failedT(getString(R.string.toast_internet_Error));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                app.failedT(getString(R.string.toast_internet_Error));
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>() ;
                params.put("email",email);
                params.put("password",password);

                return params;

            }
        };
        requestQueue.add(stringRequest);
    }
}