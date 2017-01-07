package com.example.han.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Vector;
import java.util.logging.Handler;

/**
 * Created by Han on 2016-12-21.
 */

public class StateDB {

    public String userBrrowCheck = "";
    String url = "http://192.168.1.170/state.php";

    public Boolean getData(final String id) {
        class GettingPHP extends AsyncTask<String, Integer, String> {
            protected String doInBackground(String... params) {
                String total = "";
                StringBuilder jsonHtml = new StringBuilder();
                try {
                    HttpPost request = new HttpPost(params[0]);
                    //전달할 인자들
                    Vector<NameValuePair> nameValue = new Vector<NameValuePair>();
                    //nameValue.add(new BasicNameValuePair("um", Integer.toString(a+1)));
                    nameValue.add(new BasicNameValuePair("id", id));
                    //웹 접속 - utf-8 방식으로
                    HttpEntity enty = new UrlEncodedFormEntity(nameValue, HTTP.UTF_8);
                    request.setEntity(enty);

                    HttpClient client = new DefaultHttpClient();
                    HttpResponse res = client.execute(request);
                    //웹 서버에서 값받기
                    HttpEntity entityResponse = res.getEntity();
                    InputStream im = entityResponse.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(im, HTTP.UTF_8));


                    String tmp = "";
                    //버퍼에있는거 전부 더해주기
                    //readLine -> 파일내용을 줄 단위로 읽기
                    while ((tmp = reader.readLine()) != null) {
                        if (tmp != null) {
                            total += tmp;
                        }
                    }
                    im.close();
                    //결과창뿌려주기 - ui 변경시 에러
                    //result.setText(total);
                    Log.i("Total",total);

                    //return total;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //

                Log.i("JsonHtmlState", total);
                //return jsonHtml.toString();
                return total;

            }

            protected void onPostExecute(String str) {

                try {

                    JSONObject jObject = new JSONObject(str);

                    JSONArray results = jObject.getJSONArray("result");

                    for (int i = 0; i < results.length(); ++i) {
                        JSONObject temp = results.getJSONObject(i);

                            userBrrowCheck = temp.get("state").toString();

                        }


                        Log.i("test : ", userBrrowCheck);


                    //SharedPreferences preferences = getSharedPreferences(LocationActivity.LoginValues.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                    //SharedPreferences.Editor editor = preferences.edit();

                    //Boolean logined = preferences.getBoolean(LocationActivity.LoginValues.LOGGEDIN_SHARED_PREF, false);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }
        GettingPHP g = new GettingPHP();
        g.execute(url);


        if(userBrrowCheck.equalsIgnoreCase("0"))
        return true;

        else return false;
    }
}