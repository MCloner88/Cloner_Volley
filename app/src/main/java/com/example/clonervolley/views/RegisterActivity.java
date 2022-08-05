package com.example.clonervolley.views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    EditText editText_email,editText_password;
    Button btn_signup;
    TextView Textview_Signin,Textview_email_error,Textview_password_error;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        onclick();
    }



    private void init(){

        editText_email          =findViewById(R.id.editText_email);
        editText_password       =findViewById(R.id.editText_password);
        btn_signup              =findViewById(R.id.btn_signup);
        Textview_Signin         =findViewById(R.id.Textview_Signin);
        Textview_email_error    =findViewById(R.id.Textview_email_error);
        Textview_password_error = findViewById(R.id.Textview_password_error);

        requestQueue = Volley.newRequestQueue(this);
    }

    private void onclick() {
        btn_signup.setOnClickListener(this);
        Textview_Signin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if( v == btn_signup){

            String email=editText_email.getText().toString().trim();
            String password=editText_password.getText().toString().trim();

            if(!email.matches("[a-zA-Z0-9._-]+@[a-z]+.[a-z]+")){
                Textview_email_error.setVisibility(View.VISIBLE);

            }else if(password.length() < 6){
                Textview_email_error.setVisibility(View.GONE);
                Textview_password_error.setVisibility(View.VISIBLE);
            }else{
                signUp(email,password);
            }
        }


        if( v == Textview_Signin){
            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            finish();
        }
    }

    private void signUp(String email , String password)
    {
         double d =Math.random();
        int randomNUM = (int) (d * 100000) ;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.BASE_URL + "check_email.php", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message =jsonObject .getString ("message");
                    if(message.equals("email_exist")){
                        app.failedT(getString(R.string.toast_email_exist));
                    }if(message.equals("email_ok")) {
                        Intent intent = new Intent(RegisterActivity.this,EmailValidationActivity.class);
                        intent.putExtra("email",email);
                        intent.putExtra("password",password);
                        intent.putExtra("code",randomNUM + "");

                        startActivity(intent);
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