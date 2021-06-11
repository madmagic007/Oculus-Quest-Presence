package com.madmagic.oqrpc;

import android.util.Log;
import com.madmagic.oqrpc.main.MainService;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class ApiSocket extends NanoHTTPD {

    private final MainService s;

    public ApiSocket(MainService s) throws IOException {
        super(16255);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        Log.d("OQRPC", "listening at: " + MainService.getIp(s) + ":16255");
        this.s = s;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String r = "{}";

        try {
            final HashMap<String, String> map = new HashMap<>();
            session.parseBody(map);
            if (!map.containsKey("postData")) {
                return newFixedLengthResponse(DeviceInfo.getInfo(s));
            }

            JSONObject pO = new JSONObject(map.get("postData"));
            if (pO.has("pcAddress"))
                Config.setAddress(pO.getString("pcAddress"));
            if (pO.has("sleepWake"))
                Config.setSleepWake(pO.getBoolean("sleepWake"));

            switch (pO.getString("message")) {

                case "game":
                    String[] topmostData = DeviceInfo.getTopmost(s.getBaseContext());
                    String packageName = topmostData[0];
                    JSONObject response = new JSONObject();

                    boolean detailed = false;
                    if (Module.isEnabled(packageName)) {
                        int port = Module.getPort(packageName);
                        response = ApiSender.moduleSocket(port);
                        detailed = !response.toString().equals("{}");

                        String appId = Module.getAppKey(packageName);
                        if (!appId.isEmpty()) response.put("appId", appId);
                    }
                    r = response.put("message", "gameResponse")
                            .put("name", topmostData[1])
                            .put("packageName", packageName)
                            .put("detailed", detailed)
                            .toString(4);
                    break;

                case "startup":
                    new Thread(s::callStart).start();
                    r = new JSONObject().put("started", true).toString(4);
                    break;

                case "validate":
                    r = new JSONObject()
                            .put("valid", "You just found a secret :)")
                            .toString(4);
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse(r);
    }
}