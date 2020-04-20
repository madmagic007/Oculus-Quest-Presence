package com.madmagic.oqrpc;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import fi.iki.elonen.NanoHTTPD;

public class ApiReceiver extends NanoHTTPD {

    public ApiReceiver() throws IOException {
        super(8080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String r = "";
        try {

            final HashMap<String, String> map = new HashMap<>();
            session.parseBody(map);
            final String json = map.get("postData");

            JSONObject body = new JSONObject(json);

            r = ApiHandler.received(body).toString(4);
        } catch (Exception e) {
            Log.d("OQRPC" ,"error" + e.getMessage());
        }
        return newFixedLengthResponse(r);
    }
}