package com.madmagic.oqrpc;

import android.os.BatteryManager;
import android.util.Log;


import com.rvalerio.fgchecker.AppChecker;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class ApiReceiver extends NanoHTTPD {

    public ApiReceiver() throws IOException {
        super(8080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    @Override
    public Response serve(IHTTPSession session) {
        String r = "{}";
        try {
            Map<String, List<String>> types = session.getParameters();
            if (types.isEmpty()) {
                r = GetInfo.getInfo();
            } else {
                String type = types.get("type").get(0);

                if (type.equals("validate")) {
                    r = new JSONObject().put("valid", "you just found a secret :)")
                            .toString(4);
                }

                if (type.equals("game")) {
                    r = new JSONObject()
                            .put("game", new AppChecker().getForegroundApp(MainService.s.getBaseContext()))
                            .toString(4);
                }

                if (type.equals("startup")) {
                    MainService.callStart();
                }

                final HashMap<String, String> map = new HashMap<>();
                session.parseBody(map);
                JSONObject pO = new JSONObject(map.get("postData"));

                if (pO.has("address")) ConfigCreator.write(new JSONObject().put("address", pO.getString("address")));
            }

        } catch (Exception e) {
            Log.d("OQRPC" ,"error " + e.getMessage());
        }
        return newFixedLengthResponse(r);
    }
}