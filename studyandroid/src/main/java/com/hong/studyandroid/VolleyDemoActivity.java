package com.hong.studyandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Administrator on 2016/6/2.
 */
public class VolleyDemoActivity extends Activity {

    private Button btSendRequest, btCancelRequest;
    private TextView tvWebContent;
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private static final String TAG = "baiduTag";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_volleydemo);

        btSendRequest = (Button) findViewById(R.id.bt_send_request);
        btCancelRequest = (Button) findViewById(R.id.bt_cancel_request);
        tvWebContent = (TextView) findViewById(R.id.tv_web_content);
        requestQueue = Volley.newRequestQueue(this);

        final String url = "http://www.baidu.com";
        btSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        tvWebContent.setText(response.substring(0, 500));

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tvWebContent.setText("This is not work!!");
                    }
                });

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                stringRequest.setTag(TAG);
                requestQueue.add(stringRequest);

            }
        });

        btCancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (requestQueue != null) {
                    requestQueue.cancelAll(TAG);
                    tvWebContent.setText("cancel");
                }

            }
        });


    }
}
