package com.madmagic.oqrpc;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import fi.iki.elonen.NanoHTTPD;

public class ApiReceiver extends NanoHTTPD {

    private MainService s;

    public ApiReceiver(MainService s) throws IOException {
        super(8080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        Log.d("OQRPC", "listening at: " + MainService.getIp(s) + ":8080");
        this.s = s;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String r = "";

        try {
            final HashMap<String, String> map = new HashMap<>();
            session.parseBody(map);
            if (!map.containsKey("postData")) {
                return newFixedLengthResponse(DeviceInfo.getInfo(s));
            }

            JSONObject pO = new JSONObject(map.get("postData"));
            if (pO.has("pcAddress"))
                Config.write(new JSONObject().put("address", pO.getString("pcAddress")));

            switch (pO.getString("message")) {
                case "game":
                     r = new JSONObject()
                        .put("game", DeviceInfo.getTopmost(s))
                        .toString(4);
                     break;
                case "startup":
                    s.callStart();
                    break;
                case "validate":
                    r = new JSONObject()
                            .put("valid", "You just found a secret :)")
                            .toString(4);
                    break;
            }

        } catch (Exception ignored) {}
        return newFixedLengthResponse(r);
    }
}