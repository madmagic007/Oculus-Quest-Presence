package com.madmagic.oqrpc;

import android.util.Log;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ApiCaller {

    private static OkHttpClient c = new OkHttpClient();
    private static MediaType jT = MediaType.parse("application/json; charset=utf-8");
    private static JSONObject rO;

    public static JSONObject call(String type) {
        rO = new JSONObject();
        try {
            JSONObject post = new JSONObject()
                    .put("address", MainService.getIp())
                    .put("apkVersion", "2.0");

            Request r = new Request.Builder()
                    .url("http://" + ConfigCreator.getIp() + ":8080?type=" + type)
                    .post(RequestBody.create(post.toString(4), jT))
                    .build();
            String rS = c.newCall(r).execute().body().string();
            rO = new JSONObject(rS);
        } catch (Exception e) {
            Log.d("OQRPC", "error caller " + e.getMessage());
        }
        return rO;
    }
}
