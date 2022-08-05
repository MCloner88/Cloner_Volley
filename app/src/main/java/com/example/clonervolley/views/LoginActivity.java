package com.example.clonervolley.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.clonervolley.R;
import com.example.clonervolley.app.app;
import com.example.clonervolley.app.spref;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editText_email , editText_password;
    Button btn_sign;
    CheckBox CheckBox_Remmebr;
    TextView Textview_forget_password , Textview_SignUp;

    TextView Textview_email_error , Textview_password_error;

    RequestQueue requestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        onclick();
    }

    private void init(){

            editText_email           =findViewById(R.id.editText_email);
            editText_password        =findViewById(R.id.editText_password);
            btn_sign                 =findViewById(R.id.btn_sign);
            CheckBox_Remmebr         =findViewById(R.id.CheckBox_Remmebr);
            Textview_forget_password =findViewById(R.id.Textview_forget_password);
            Textview_SignUp          =findViewById(R.id.Textview_SignUp);

            Textview_email_error     =findViewById(R.id.Textview_email_error);
            Textview_password_error  =findViewById(R.id.Textview_password_error);

            requestQueue = Volley.newRequestQueue(this);

//           String email = spref.getSharedPreferences().getString(spref.EMAIL,"");
//           editText_email.setText(email);

//        app.successT("SEC");
//        app.failedT("FAL");

    }

    private void onclick(){

            btn_sign.setOnClickListener(this);
            Textview_forget_password.setOnClickListener(this);
            Textview_SignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if(v == btn_sign){
            if(CheckBox_Remmebr.isChecked()){
                spref.getSharedPreferences().edit().putBoolean(spref.REMMEBER_ME,true).apply();
            }else {
                spref.getSharedPreferences().edit().putBoolean(spref.REMMEBER_ME,CheckBox_Remmebr.isChecked()).apply();
            }

            String email=editText_email.getText().toString().trim();
            String password=editText_password.getText().toString().trim();

            if(!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){
                Textview_email_error.setVisibility(View.VISIBLE);

            }else if(password.length() < 6){
                Textview_email_error.setVisibility(View.GONE);
                Textview_password_error.setVisibility(View.VISIBLE);
            }else{
                login(email,password);
            }
            spref.getSharedPreferences().edit().putString(spref.EMAIL,email).apply();
        }

        if(v == Textview_forget_password){
            startActivity(new Intent(this,ForgetPassword.class));
        }

        if(v == Textview_SignUp){
            startActivity(new Intent(this,RegisterActivity.class));
        }

    }

    private void login(String email , String password)
    {
        StringRequest stringRequest=  new StringRequest(Request.Method.POST, app.BASE_URL + "login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message =jsonObject.getString("message");

                    if(message.equals("login ok")){//login ok
                        spref.getSharedPreferences().edit().putString(spref.EMAIL , email);
                        Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                        startActivity(intent);
                        finish();

                    }else { //failed_login
                        app.failedT(getString(R.string.toast_Login_Failed));
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
}