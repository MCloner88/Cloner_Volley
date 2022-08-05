package com.example.clonervolley.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.clonervolley.R;
import com.example.clonervolley.adapters.images_adapter;
import com.example.clonervolley.app.app;
import com.example.clonervolley.app.spref;
import com.example.clonervolley.interfaces.MultiAction_interface;
import com.example.clonervolley.models.images_model;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener , MultiAction_interface {

    SwipeRefreshLayout SwipeRefreshLayout ;
    RecyclerView recyclerview;

    RelativeLayout layout;
    TextView Textview_delete , Textview_cancel;

    List<images_model> list ;
    images_adapter     adapter;

    AlertDialog alertDialog;


    ImageView image_more;
    final int CAMERA_PERM = 110;
    final int CAMERA_CAP = 111;
    final int GALLERY_PERM = 112;
    final int GALLERY_PICK = 113;


    RequestQueue requestQueue;

    String ids ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        getImages();
        onclick();



    }

    private void init() {
        image_more          = findViewById(R.id.image_more);
        SwipeRefreshLayout  = findViewById(R.id.SwipeRefreshLayout);
        recyclerview        = findViewById(R.id.recyclerview);

        layout              = findViewById(R.id.layout);
        Textview_cancel     = findViewById(R.id.Textview_cancel);
        Textview_delete     = findViewById(R.id.Textview_delete);

        requestQueue = Volley.newRequestQueue(this);


        list=new ArrayList<>();
        adapter = new images_adapter(list , this,this,this);

        recyclerview.setAdapter(adapter);
        recyclerview.setLayoutManager(new GridLayoutManager(this,3));

        SwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getImages();
            }
        });


    }

    private void getImages(){
        list.clear();

        SwipeRefreshLayout.setRefreshing(true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.BASE_URL + "get_images.php", new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    int len = jsonArray.length();
                    for(int i=0 ; i<len ; i++ ){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        images_model model = new images_model();

                        model.setId(jsonObject.getString("id"));
                        model.setEmail(jsonObject.getString("email"));
                        model.setImage(jsonObject.getString("image"));
                        model.setUploaded_at(jsonObject.getString("uploaded_at"));

                        list.add(model);
                    }

                    adapter.notifyDataSetChanged();

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
                params.put("email",spref.getSharedPreferences().getString(spref.EMAIL,""));

                return params ;
            }
        };
        SwipeRefreshLayout.setRefreshing(false);
        requestQueue.add(stringRequest) ;

        
    }

    private void onclick() {
        image_more.setOnClickListener(this);
        Textview_cancel.setOnClickListener(this);
        Textview_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if( v == image_more){
            openBottomsheet();
        }
        if( v == Textview_delete){
            deleteImage();
        }
        if( v == Textview_cancel){

            layout.setVisibility(View.GONE);
            images_adapter.count = 0;
            images_adapter.multiAction = false;
            adapter.notifyDataSetChanged();
        }
    }

    private void deleteImage() {
        ids = "-1";
        for(int i=0 ; i<list.size() ; i++){
            if(list.get(i).getMultiAction()){
                ids += ","+list.get(i).getId();
            }
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.BASE_URL + "delete_images.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message =jsonObject.getString("message");
                    if(message.equals("delete_failed")){
                        app.failedT(getString(R.string.toast_internet_Error));
                    }
                    if(message.equals("delete_ok")){
                        images_adapter.count = 0;
                        images_adapter.multiAction = false ;
                        layout.setVisibility(View.GONE);
                        getImages();
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
               Map<String ,String> params = new HashMap<>();
               params.put("ids",ids);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void openBottomsheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottem_sheet , findViewById(R.id.layout_BottomSheet));

        LinearLayout layout_fromGallery , layout_fromCam , layout_exit , layout_deleteAc ;

        layout_fromGallery  = view.findViewById(R.id.layout_fromGallery);
        layout_fromCam      = view.findViewById(R.id.layout_fromCam);
        layout_exit         = view.findViewById(R.id.layout_exit);
        layout_deleteAc     = view.findViewById(R.id.layout_deleteAc);

        layout_fromGallery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
                    int res = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                    if(res == PackageManager.PERMISSION_GRANTED){
                        fromGallery();
                    }else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},GALLERY_PERM);
                    }
                }else {
                    fromGallery();
                }
                bottomSheetDialog.dismiss();
            }
        });

        layout_fromCam.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1){
                    int res = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ;
                    if(res == PackageManager.PERMISSION_GRANTED){
                        fromCamera();
                    }else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE},CAMERA_PERM);
                    }
                }
                else {
                    fromCamera();
                }
                bottomSheetDialog.dismiss();
            }
        });

        layout_exit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                spref.getSharedPreferences().edit().putString(spref.EMAIL,"").apply();
                spref.getSharedPreferences().edit().putBoolean(spref.REMMEBER_ME, false);
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });

        layout_deleteAc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                showAlert();
                bottomSheetDialog.dismiss();

            }
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.alert_delete_acc, null);
        
        builder.setView(view);

        TextView Textview_delete_Ac = view.findViewById(R.id.Textview_delete_Ac);
        TextView Textview_cancel = view.findViewById(R.id.Textview_cancel);

        Textview_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        Textview_delete_Ac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, app.BASE_URL + "delete_acc.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            String message =jsonObject.getString("message");

                            if(message.equals("Delete_Ok")){
                                spref.getSharedPreferences().edit().putString(spref.EMAIL,"").apply();
                                spref.getSharedPreferences().edit().putBoolean(spref.EMAIL,false).apply();

                                app.successT(getString(R.string.toast_acc_deleted));
                                startActivity(new Intent(MainActivity.this,LoginActivity.class));
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
                        params.put("email",spref.getSharedPreferences().getString(spref.EMAIL,""));
                        return params;
                    }
                };

                requestQueue.add(stringRequest);
                alertDialog.dismiss();
            }

        });

        alertDialog = builder.create() ;
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void fromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent , CAMERA_CAP);

    }

    private void fromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent , GALLERY_PICK);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                fromCamera();
            }
        }
        if(requestCode == GALLERY_PERM){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                fromGallery();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if (requestCode == CAMERA_CAP){
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                Upload(bitmap);
            }
            if(requestCode == GALLERY_PICK){
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver() , data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Upload(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void Upload(Bitmap bitmap){
        int size =(int) ((bitmap.getHeight()) * (812.0 / bitmap.getWidth()));
        Bitmap decoded = Bitmap.createScaledBitmap(bitmap,812 , size , true);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        decoded.compress(Bitmap.CompressFormat.JPEG, 80 , byteArrayOutputStream);

        byte []bytes = byteArrayOutputStream.toByteArray();
        String image = Base64.encodeToString(bytes , Base64.DEFAULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, app.BASE_URL + "upload.php", new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");

                    if(message.equals("OK")){//uoload ok
                        app.successT(getString(R.string.toast_image_uploaded));
                        getImages();
                    }
                    if(message.equals("FAILED")){// uplaod FAILED
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
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> params = new HashMap<>();
                params.put("email", spref.getSharedPreferences().getString(spref.EMAIL,""));
                params.put("image",image);

                return params;
            }
        } ;

        requestQueue.add(stringRequest);
    }




    //multiAction Method

    @Override
    public void start() {
        layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void selected(int count, int pos) {
        list.get(pos).setMultiAction(true);
    }

    @Override
    public void deSelected(int count, int pos) {
        list.get(pos).setMultiAction(false);

        if(count == 0){
            layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        if(layout.getVisibility() == View.VISIBLE){
            layout.setVisibility(View.GONE);
            images_adapter.count = 0;
            images_adapter.multiAction = false;

            adapter.notifyDataSetChanged();

        }else {
            super.onBackPressed();
        }

    }
}