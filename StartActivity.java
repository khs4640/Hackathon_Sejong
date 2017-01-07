package com.example.han.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StartActivity extends Activity {

    private ImageButton btnLogin;

    //String name="",pass="";
    EditText id,password;
    TextView tv;
    byte[] data;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    InputStream inputStream;
    SharedPreferences app_preferences ;
    List<NameValuePair> nameValuePairs;
    CheckBox check;
    ProgressDialog dialog = null;
    private boolean loggedIn = false;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnLogin = (ImageButton) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(btnClickListener);
        btnLogin.setOnTouchListener(btnTouchListener);

        id = (EditText) findViewById(R.id.editID);
        password = (EditText) findViewById(R.id.editPW);

    }

    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getSharedPreferences(LocationActivity.LoginValues.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //Fetching the boolean value form sharedpreferences
        loggedIn = sharedPreferences.getBoolean(LocationActivity.LoginValues.LOGGEDIN_SHARED_PREF, false);

        //If we will get true
        if(loggedIn){
            //We will start the Profile Activity
            Intent intent = new Intent(StartActivity.this, LocationActivity.class);
            startActivity(intent);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed() {
        new AlertDialog.Builder(StartActivity.this)
                .setTitle("종료하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    void login(){
        try{

            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://192.168.1.170/"); // make sure the url is correct.
            //add your data
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("id",id.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("password",password.getText().toString().trim()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            String idStr = id.getText().toString();
            //String passStr = password.getText().toString();
            //Log.i("id",idStr);
            //Log.i("pass",passStr);
            //Execute HTTP Post Request
            response=httpclient.execute(httppost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            runOnUiThread(new Runnable() {
                public void run() {
                    //tv.setText("Response from PHP : " + response);
                    dialog.dismiss();
                }
            });

            if(response.equalsIgnoreCase("User Found")){
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(StartActivity.this,"Login Success", Toast.LENGTH_SHORT).show();
                    }
                });
                SharedPreferences sharedPreferences = StartActivity.this.getSharedPreferences(LocationActivity.LoginValues.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                //Creating editor to store values to shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();

                //Adding values to editor
                editor.putBoolean(LocationActivity.LoginValues.LOGGEDIN_SHARED_PREF, true);
                //editor.putString(LoginValues.SHARED_PREF_PASS , email);
                editor.putString(LocationActivity.LoginValues.SHARED_PREF_ID,idStr );
                //Saving values to editor
                editor.commit();
                startActivity(new Intent(StartActivity.this, LocationActivity.class));
            }else{
                showAlert();
            }

        }catch(Exception e){
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }
    public void showAlert(){
        StartActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);
                builder.setTitle("Login Error.");
                builder.setMessage("User not Found.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    Button.OnClickListener btnClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.btnLogin:
                    dialog = ProgressDialog.show(StartActivity.this, "",
                            "Validating user...", true);
                    new Thread(new Runnable() {
                        public void run() {
                            login();
                        }
                    }).start();

                    //Intent i = new Intent(StartActivity.this, LocationActivity.class);
                    //startActivity(i);
                    break;
            }
        }
    };
    Button.OnTouchListener btnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                if(v.getId() == R.id.btnLogin)
                    btnLogin.setImageResource(R.drawable.btn_login_01);
            }
            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(v.getId() == R.id.btnLogin)
                    btnLogin.setImageResource(R.drawable.btn_login_02);
            }
            return false;
        }
    };
}