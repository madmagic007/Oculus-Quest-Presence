package me.madmagic.oqrpc.api;

import com.madmagic.oqrpc.source.*;
import fi.iki.elonen.NanoHTTPD;
import me.madmagic.oqrpc.source.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class ApiSocket extends NanoHTTPD {

    public ApiSocket() throws IOException {
        super(16255);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("listening on port 16255");
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
                                .put("questAddress", Config.getAddress())
                                .put("pcAddress", Main.getIp())
                                .put("computerName",
                                        System.getenv().containsValue("COMPUTERNAME") ? System.getenv().get("COMPUTERNAME") : "unknown")
                                .toString(4));
            }

            JSONObject pO = new JSONObject(map.get("postData"));
            if (pO.has("questAddress")) Config.setAddress(pO.getString("questAddress"));
            if (pO.has("apkVersion")) Config.setApkVersion(pO.getString("apkVersion"));

            switch (pO.getString("message")) {
                case "online":
                    SystemTrayHandler.notif("Quest online", "RPC service on your Quest has started");
                    Discord.init(Discord.appId);
                    Timing.startRequester();
                    break;
                case "offline":
                    SystemTrayHandler.notif("Quest offline", "RPC service on your Quest has stopped");
                    Discord.terminate();
                    Timing.terminate();
                    break;
                case "connect":
                r = new JSONObject()
                        .put("connected", "Successfully connected")
                        .toString(4);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFixedLengthResponse(r);
    }
}
