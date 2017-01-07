package com.example.han.myapplication;

/**
 * Created by Han on 2016-12-20.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LentActivity extends Activity {

    private ImageView imgLoc;
    private int locNum;
    String url1 = "http://192.168.1.170/building1.php";
    String url2 = "http://192.168.1.170/building2.php";
    String url3 = "http://192.168.1.170/building3.php";
    String url4 = "http://192.168.1.170/building4.php";
    String url5 = "http://192.168.1.170/building5.php";
    String url6 = "http://192.168.1.170/building6.php";
    String updateURL = "http://192.168.1.170/building.php";
    public String[] umbrellaCheck = new String[9];

    int img[] = {R.drawable.icon_enable, R.drawable.icon_enable, R.drawable.icon_enable,
            R.drawable.icon_enable, R.drawable.icon_enable, R.drawable.icon_enable,
            R.drawable.icon_enable, R.drawable.icon_enable, R.drawable.icon_enable};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lent);
        String umbrella = "";

        Intent i = new Intent(this.getIntent());
        locNum = i.getIntExtra("locNum", 0);

        getData();
    }

    public void getData() {
        class GettingPHP extends AsyncTask<String, Integer, String> {
            protected String doInBackground(String... params) {
                StringBuilder jsonHtml = new StringBuilder();
                try {
                    URL phpUrl = new URL(params[0]);
                    HttpURLConnection conn = (HttpURLConnection) phpUrl.openConnection();

                    if (conn != null) {
                        conn.setConnectTimeout(10000);
                        conn.setUseCaches(false);

                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                            while (true) {
                                String line = br.readLine();
                                if (line == null)
                                    break;
                                jsonHtml.append(line + "\n");
                            }
                            br.close();
                        }
                        conn.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.i("JsonHtml", jsonHtml.toString());


                return jsonHtml.toString();
            }
            protected void onPostExecute(String str) {
                try {

                    JSONObject jObject = new JSONObject(str);

                    JSONArray results = jObject.getJSONArray("result");

                    for ( int i = 0; i < results.length(); ++i ) {
                        JSONObject temp = results.getJSONObject(i);
                        int k = 0;
                        for(int j = 0 ; j < 9 ; j++) {
                            k = j + 1;
                            umbrellaCheck[j] = temp.get("um"+k).toString();

                        }
                    }
                    for(int j = 0 ; j < 9 ; j++)
                        Log.i("test : ", umbrellaCheck[j]);


                    setContentView(R.layout.activity_lent);
                    final int img[] = new int[9];
                    for(int i = 0; i<9; i++) {
                        if (umbrellaCheck[i].equalsIgnoreCase("1")) {

                            img[i] = R.drawable.icon_enable;
                        } else img[i] = R.drawable.icon_unable;

                        Log.i("img"+i+":",Integer.toString(img[i]));
                    }

                    imgLoc = (ImageView)findViewById(R.id.imgLoc);
                    switch(locNum) {
                        case 1:
                            imgLoc.setImageResource(R.drawable.img_loc1);
                            break;
                        case 2:
                            imgLoc.setImageResource(R.drawable.img_loc2);
                            break;
                        case 3:
                            imgLoc.setImageResource(R.drawable.img_loc3);
                            break;
                        case 4:
                            imgLoc.setImageResource(R.drawable.img_loc4);
                            break;
                        case 5:
                            imgLoc.setImageResource(R.drawable.img_loc5);
                            break;
                        case 6:
                            imgLoc.setImageResource(R.drawable.img_loc6 );
                            break;
                    }

                    MyAdapter adapter = new MyAdapter (getApplicationContext(), R.layout.item, img);


                    final GridView gv = (GridView)findViewById(R.id.gridView);
                    gv.setAdapter(adapter);
                    final UpdateDB db = new UpdateDB();
                    final StateDB umbrellaState = new StateDB();

                    final SharedPreferences preferences = getSharedPreferences(LocationActivity.LoginValues.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                    //SharedPreferences.Editor editor = preferences.edit();

                    final Boolean logined = preferences.getBoolean(LocationActivity.LoginValues.LOGGEDIN_SHARED_PREF,false);
                    final String userID = preferences.getString(LocationActivity.LoginValues.SHARED_PREF_ID,"");

                    gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            //if(!preferences.getBoolean(LocationActivity.LoginValues.BRROW_UMBRELLA, false))
                            Boolean chkState = umbrellaState.getData(userID);
                            if (chkState && img[position] == R.drawable.icon_enable){
                                //if(chkState){
                                    new AlertDialog.Builder(LentActivity.this)
                                            .setTitle("대여 하시겠습니까?")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    img[position] = R.drawable.icon_unable;
                                                    MyAdapter adapter = new MyAdapter(getApplicationContext(), R.layout.item, img);
                                                    gv.setAdapter(adapter);
                                                    //ActivityCompat.finishAffinity(LentActivity.this);
                                                    //SharedPreferences.Editor editor = preferences.edit();
                                                    //editor.putBoolean(LocationActivity.LoginValues.BRROW_UMBRELLA,true);

                                                    //editor.commit();


                                                    db.update(position,userID);
                                                }
                                            })
                                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            }).show();

                                //}
                            }

                            else if(!chkState && img[position] == R.drawable.icon_unable){

                                //if (img[position] == R.drawable.icon_unable)
                               // if(!chkState){
                                    new AlertDialog.Builder(LentActivity.this)
                                            .setTitle("반납 하시겠습니까?")
                                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    img[position] = R.drawable.icon_enable;
                                                    MyAdapter adapter = new MyAdapter(getApplicationContext(), R.layout.item, img);
                                                    gv.setAdapter(adapter);
                                                    //ActivityCompat.finishAffinity(LentActivity.this);
                                                    //SharedPreferences.Editor editor = preferences.edit();
                                                    //editor.putBoolean(LocationActivity.LoginValues.BRROW_UMBRELLA,false);
                                                    //editor.commit();
                                                    db.update(position,userID,1);
                                                }
                                            })
                                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            }).show();
                               // }



                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }
        GettingPHP g = new GettingPHP();
        switch(locNum) {
            case 1:
                g.execute(url1);
                break;
            case 2:
                g.execute(url2);
                break;
            case 3:
                g.execute(url3);
                break;
            case 4:
                g.execute(url4);
                break;
            case 5:
                g.execute(url5);
                break;
            case 6:
                g.execute(url6);
                break;
        }

    }
}

class MyAdapter extends BaseAdapter {
    Context context;
    int layout;
    int img[];
    LayoutInflater inf;

    public MyAdapter(Context context, int layout, int[] img) {
        this.context = context;
        this.layout = layout;
        this.img = img;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return img.length;
    }

    public Object getItem(int position) {
        return img[position];
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
            convertView = inf.inflate(layout, null);
        ImageView iv = (ImageView)convertView.findViewById(R.id.imageView);
        iv.setImageResource(img[position]);

        return convertView;
    }
}
