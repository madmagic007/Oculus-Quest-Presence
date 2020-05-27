package com.madmagic.oqrpc;

import android.util.Log;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ApiSender {

    private static OkHttpClient c = new OkHttpClient();
    private static MediaType jT = MediaType.parse("application/json; charset=utf-8");

    public static JSONObject send(String message, MainService s) {
        JSONObject ro = new JSONObject();
        try {
            JSONObject post = new JSONObject()
                    .put("apkVersion", "2.1")
                    .put("message", message);
            try {
                post.put("addressA", MainService.getIp(s));
            } catch (Exception ignored) {
            }

            String ip = Config.getIp();
            if (ip.isEmpty()) return ro;

            Request r = new Request.Builder()
                    .url("http://" + ip + ":8080")
                    .post(RequestBody.create(post.toString(4), jT))
                    .build();

            ro = new JSONObject(c.newCall(r).execute().body().string());
        } catch (Exception ignored) {}
        return ro;
    }
}
