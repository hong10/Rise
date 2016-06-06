package com.hong.studyandroid;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hong.domain.Result;
import com.hong.domain.User;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/6/3.
 */
public class MockLoginByVolleyActivity extends Activity {

    private static final String TAG = MockLoginByVolleyActivity.class.getSimpleName();
    private EditText etUsername, etPassword;
    private Button btLogin;
    private Gson gson;
    private String username, password;
    private String url = "http://10.88.0.228:8080/WebRise/toAndroid/MockLoginServlet?";
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mockloginbyvolley);

        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        btLogin = (Button) findViewById(R.id.bt_login);


        gson = new Gson();
        requestQueue = Volley.newRequestQueue(this);

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = etUsername.getText().toString().trim();
                password = etPassword.getText().toString().trim();


                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplication(), "username or password is null", Toast.LENGTH_LONG).show();
                } else {
                    User user = new User(username, password);
                    String loginJson = gson.toJson(user);
                    Log.i(TAG, loginJson);
                    //url + username=hong&password=123
                    String requestUrl = url + "username=" + username + "&password=" + password;
                    JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Result result = gson.fromJson(jsonObject.toString(), Result.class);
                            Log.i(TAG + "-result", result.toString());
                            Toast.makeText(getApplicationContext(), result.getMsg(), Toast.LENGTH_LONG).show();

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getApplicationContext(), "login fail", Toast.LENGTH_LONG).show();
                        }
                    });

                    requestQueue.add(jsObjRequest);
                }


            }
        });

    }
}
