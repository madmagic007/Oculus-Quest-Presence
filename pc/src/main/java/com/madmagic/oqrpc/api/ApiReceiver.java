package com.madmagic.oqrpc.api;

import com.madmagic.oqrpc.source.*;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class ApiReceiver extends NanoHTTPD {

    public ApiReceiver() throws IOException {
        super(8080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("listening on port 8080 at /api/pc");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String r = "{}";
        try {
            final HashMap<String, String> map = new HashMap<>();
            session.parseBody(map);
            if (!map.containsKey("postData")) {
                return newFixedLengthResponse(
                        new JSONObject()
                                .put("questIp", Config.getAddress())
                                .put("thisIp", Main.getIp())
                                .put("computerName",
                                        System.getenv().containsValue("COMPUTERNAME") ? System.getenv().get("COMPUTERNAME") : "unknown")
                                .toString(4));
            }

            JSONObject pO = new JSONObject(map.get("postData"));
            if (pO.has("addressA")) Config.setAddress(pO.getString("addressA"));
            if (pO.has("apkVersion")) Config.setApk(pO.getString("apkVersion"));

            switch (pO.getString("message")) {
                case "online":
                    SystemTrayHandler.notif("Quest online", "RPC service on your Quest has started");
                    Discord.init();
                    Timing.startRequester();
                    break;
                case "offline" :
                    SystemTrayHandler.notif("Quest offline", "RPC service on your Quest has stopped");
                    Discord.terminate();
                    Timing.terminate();
                    break;
                case "connect":
                    r = new JSONObject()
                            .put("connected", "You found another secret :)")
                            .toString(4);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse(r);
    }
}
