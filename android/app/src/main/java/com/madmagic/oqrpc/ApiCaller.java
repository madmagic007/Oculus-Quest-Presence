package com.madmagic.oqrpc;

import android.util.Log;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ApiCaller {

    private OkHttpClient c = new OkHttpClient();
    private MediaType jT = MediaType.parse("application/json; charset=utf-8");

    public ApiCaller(JSONObject o) {

        Thread thread = new Thread(() -> {
            try {
                String ip = ConfigCreator.getIp();
                Log.d("OQRPC", "calling " + ip + " with message " + o.toString(4));
                Request r = new Request.Builder()
                        .url("http://" + ip + ":8080/api/pc")
                        .post(RequestBody.create(o.toString(4), jT))
                        .build();
                c.newCall(r).execute().body().string();
            } catch (Exception e) {
                Log.d("OQRPC", "error caller " + e.getMessage());
            }
        });
        thread.start();
    }
}
