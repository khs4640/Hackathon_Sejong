package com.example.han.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class LocationActivity  extends Activity {

    private ImageButton btnLocation1;
    private ImageButton btnLocation2;
    private ImageButton btnLocation3;
    private ImageButton btnLocation4;
    private ImageButton btnLocation5;
    private ImageButton btnLocation6;
    private ImageButton btnLogout;
    private ImageView iconUmb;
    Boolean chkState;
    StateDB umbrellaState = new StateDB();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        btnLocation1 = (ImageButton) findViewById(R.id.btnLocation1);
        btnLocation1.setOnClickListener(btnClickListener);
        btnLocation1.setOnTouchListener(btnTouchListener);
        btnLocation2 = (ImageButton) findViewById(R.id.btnLocation2);
        btnLocation2.setOnClickListener(btnClickListener);
        btnLocation2.setOnTouchListener(btnTouchListener);
        btnLocation3 = (ImageButton) findViewById(R.id.btnLocation3);
        btnLocation3.setOnClickListener(btnClickListener);
        btnLocation3.setOnTouchListener(btnTouchListener);
        btnLocation4 = (ImageButton) findViewById(R.id.btnLocation4);
        btnLocation4.setOnClickListener(btnClickListener);
        btnLocation4.setOnTouchListener(btnTouchListener);
        btnLocation5 = (ImageButton) findViewById(R.id.btnLocation5);
        btnLocation5.setOnClickListener(btnClickListener);
        btnLocation5.setOnTouchListener(btnTouchListener);
        btnLocation6 = (ImageButton) findViewById(R.id.btnLocation6);
        btnLocation6.setOnClickListener(btnClickListener);
        btnLocation6.setOnTouchListener(btnTouchListener);
        btnLogout = (ImageButton) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(btnClickListener);
        btnLogout.setOnTouchListener(btnTouchListener);


        iconUmb = (ImageView) findViewById(R.id.imgUmb);
        iconUmb.setVisibility(View.INVISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences(LoginValues.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString(LoginValues.SHARED_PREF_ID,"Not Available");

        TextView textView1 = (TextView) findViewById(R.id.textView_Location1);
        textView1.setVisibility(View.GONE);
        textView1.setText("현재 유저 : " + id);


    }

    protected void onStart() {
        super.onStart();
    }

    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences(LoginValues.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String id = sharedPreferences.getString(LoginValues.SHARED_PREF_ID,"Not Available");
        chkState = umbrellaState.getData(id);
        Log.i("chkSt",chkState.toString());
        if(!chkState) iconUmb.setVisibility(View.VISIBLE);
        else iconUmb.setVisibility(View.INVISIBLE);

    }

    protected void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed() {
        new AlertDialog.Builder(LocationActivity.this)
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

    Button.OnClickListener btnClickListener = new View.OnClickListener() {
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnLocation1:

                    intentLocation(1);
                    break;

                case R.id.btnLocation2:
                    intentLocation(2);
                    break;

                case R.id.btnLocation3:
                    intentLocation(3);
                    break;

                case R.id.btnLocation4:
                    intentLocation(4);
                    break;

                case R.id.btnLocation5:
                    intentLocation(5);
                    break;

                case R.id.btnLocation6:
                    intentLocation(6);
                    break;

                case R.id.btnLogout:
                    //TODO
                    SharedPreferences preferences = getSharedPreferences(LoginValues.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putBoolean(LoginValues.LOGGEDIN_SHARED_PREF, false);
                    editor.putString(LoginValues.SHARED_PREF_ID, "");
                    editor.commit();

                    Intent i = new Intent(LocationActivity.this, StartActivity.class);
                    startActivity(i);
                    break;
            }
        }
    };

    private void intentLocation(int num) {
        Intent i = new Intent(LocationActivity.this, LentActivity.class);
        i.putExtra("locNum", num);
        startActivity(i);
    }
    Button.OnTouchListener btnTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                if(v.getId() == R.id.btnLocation1)
                    btnLocation1.setImageResource(R.drawable.btn_loc1_02);
                if(v.getId() == R.id.btnLocation2)
                    btnLocation2.setImageResource(R.drawable.btn_loc2_02);
                if(v.getId() == R.id.btnLocation3)
                    btnLocation3.setImageResource(R.drawable.btn_loc3_02);
                if(v.getId() == R.id.btnLocation4)
                    btnLocation4.setImageResource(R.drawable.btn_loc4_02);
                if(v.getId() == R.id.btnLocation5)
                    btnLocation5.setImageResource(R.drawable.btn_loc5_02);
                if(v.getId() == R.id.btnLocation6)
                    btnLocation6.setImageResource(R.drawable.btn_loc6_02);
                if(v.getId() == R.id.btnLogout)
                    btnLogout.setImageResource(R.drawable.icon_logout_02);
            }
            if(event.getAction() == MotionEvent.ACTION_UP) {
                if(v.getId() == R.id.btnLocation1)
                    btnLocation1.setImageResource(R.drawable.btn_loc1_01);
                if(v.getId() == R.id.btnLocation2)
                    btnLocation2.setImageResource(R.drawable.btn_loc2_01);
                if(v.getId() == R.id.btnLocation3)
                    btnLocation3.setImageResource(R.drawable.btn_loc3_01);
                if(v.getId() == R.id.btnLocation4)
                    btnLocation4.setImageResource(R.drawable.btn_loc4_01);
                if(v.getId() == R.id.btnLocation5)
                    btnLocation5.setImageResource(R.drawable.btn_loc5_01);
                if(v.getId() == R.id.btnLocation6)
                    btnLocation6.setImageResource(R.drawable.btn_loc6_01);
                if(v.getId() == R.id.btnLogout)
                    btnLogout.setImageResource(R.drawable.icon_logout_01);
            }
            return false;
        }
    };

    public static class LoginValues {
        public static final String SHARED_PREF_NAME = "myloginapp";
        //This would be used to store the email of current logged in user
        public static final String SHARED_PREF_ID = "id";
        public static final String LOGGEDIN_SHARED_PREF = "loggedin";
        public static final String BRROW_UMBRELLA = "false";
    }
}