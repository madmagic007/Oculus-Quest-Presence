package com.madmagic.oqrpc;

import android.util.Log;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ApiSender {
    private static final OkHttpClient c = new OkHttpClient();
    private static final MediaType jT = MediaType.parse("application/json; charset=utf-8");

    public static JSONObject send(String message, String ownAddress) {
        JSONObject ro = new JSONObject();
        try {
            JSONObject post = new JSONObject()
                    .put("apkVersion", "2.5")
                    .put("message", message);

            if (!ownAddress.isEmpty())
                post.put("addressA", ownAddress);

            String ip = Config.getAddress();
            if (ip.isEmpty()) return ro;

            Request r = new Request.Builder()
                    .url("http://" + ip + ":16255")
                    .post(RequestBody.create(post.toString(4), jT))
                    .build();

            ro = new JSONObject(c.newCall(r).execute().body().string());
        } catch (Exception ignored) {}
        return ro;
    }

    public static JSONObject moduleSocket(int port)    {
        try {
            Socket socket = new Socket("127.0.0.1", port);
            DataInputStream stream = new DataInputStream(socket.getInputStream());

            int messageLength = stream.readInt();
            byte[] message = new byte[messageLength];
            stream.readFully(message);

            socket.close();
            return new JSONObject(new String(message, StandardCharsets.UTF_8));
        }   catch(Exception e) {
            Log.d("OQRPC", Log.getStackTraceString(e));
        }
        return new JSONObject();
    }
}
