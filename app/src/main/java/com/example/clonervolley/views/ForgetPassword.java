package com.example.clonervolley.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layout_pinview , layout_email , layout_password;
    EditText editText_email , editText_password;
    PinView pinview;
    Button btn_continue , btn_checkCode , btn_goback , btn_kos;
    TextView Textview_password_error , Textview_email_error;

    String email;
    int randomNUM;
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        init();
       // onclick();

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editText_email.getText().toString().trim();
                if(!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){
                    Textview_email_error.setVisibility(View.VISIBLE);
                }else {
                    CheckEmail();
                }
            }
        });

        btn_checkCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = pinview.getText().toString().trim();
              //  Toast.makeText(ForgetPassword.this, "ttt", Toast.LENGTH_SHORT).show();
                if(code.equals(randomNUM+"")){ //code ok
                    layout_pinview.setVisibility(View.GONE);
                    btn_checkCode.setVisibility(View.GONE);
                    layout_password.setVisibility(View.VISIBLE);
                    btn_goback.setVisibility(View.VISIBLE);
                }else {
                    pinview.setLineColor(getResources().getColor(R.color.colorRed));
                }
            }
        });

        btn_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ForgetPassword.this, "TEst", Toast.LENGTH_SHORT).show();
                String mypassword = editText_password.getText().toString().trim();
                if(mypassword.length() < 6){
                    Textview_password_error.setVisibility(View.VISIBLE);
                }else {
                    changePass(mypassword);
                }
            }
        });

    }

    private void init() {

            layout_pinview    =findViewById(R.id.layout_pinview);
            layout_email      =findViewById(R.id.layout_email);
            layout_password   =findViewById(R.id.layout_password);
            editText_email    =findViewById(R.id.editText_email);
            editText_password =findViewById(R.id.editText_password);
            pinview           =findViewById(R.id.pinview);
            btn_continue      =findViewById(R.id.btn_continue);
            btn_checkCode     =findViewById(R.id.btn_checkCode);
            btn_goback        =findViewById(R.id.btn_goback);

            requestQueue = Volley.newRequestQueue(this);
    }
    private void onclick() {

//        btn_continue.setOnClickListener(this);
//        btn_checkCode.setOnClickListener(this);
//        btn_goback.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
//        if( v == btn_continue){
//            email = editText_email.getText().toString().trim();
//            if(!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){
//                Textview_email_error.setVisibility(View.VISIBLE);
//        }else {
//                CheckEmail();
//            }
//        if( v == btn_checkCode){
//            String code = pinview.getText().toString().trim();
//            Toast.makeText(this, "ttt", Toast.LENGTH_SHORT).show();
//            if(code.equals(randomNUM+"")){ //code ok
//                 layout_pinview.setVisibility(View.GONE);
//                 btn_checkCode.setVisibility(View.GONE);
//                 layout_password.setVisibility(View.VISIBLE);
//                 btn_goback.setVisibility(View.VISIBLE);
//            }else {
//                pinview.setLineColor(getResources().getColor(R.color.colorRed));
//            }
//        }
//        if( v == btn_goback){
//            String password = editText_password.getText().toString().trim();
//            if(password.length() < 6){
//                Textview_password_error.setVisibility(View.VISIBLE);
//            }else {
//                changePass(password);
//            }
//        }

        //}
    }

    private void changePass(String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.BASE_URL + "change_pass.php", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if(message.equals("ok")){
                        app.failedT(getString(R.string.toast_password_changed));
                        finish();
                    }
                    if (message.equals("FAILED")){
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
                Map<String , String> params = new HashMap<>();
                params.put("email",email);
                params.put("password",password);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void CheckEmail(){
        double d =Math.random();
        randomNUM = (int) (d * 100000) ;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.BASE_URL + "forget_pass.php", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message =jsonObject .getString ("message");
                    if(message.equals("email_notExist")){
                        app.failedT(getString(R.string.toast_email_notExist));
                    }if(message.equals("email_ok")) {
                        layout_email.setVisibility(View.GONE);
                        btn_continue.setVisibility(View.GONE);
                        layout_pinview.setVisibility(View.VISIBLE);
                        btn_checkCode.setVisibility(View.VISIBLE);
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
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<>();
                params.put("email", email);
                params.put("code", randomNUM+ "");

                return params;

            }
        };
        requestQueue.add(stringRequest);
    }
}