package com.madmagic.oqrpc.api;

import com.madmagic.oqrpc.source.Config;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiReceiver extends NanoHTTPD {

    public ApiReceiver() throws IOException {
        super(8080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("listening on port 8080 at /api/pc");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String s = "{}";
        try {
            Map<String, List<String>> types = session.getParameters();
            if (!types.isEmpty()) {
                s = ResponseHandler.handle(types.get("type").get(0));
                System.out.println(types.get("type").get(0));
                final HashMap<String, String> map = new HashMap<>();
                session.parseBody(map);
                JSONObject pO = new JSONObject(map.get("postData"));

                if (pO.has("address")) Config.setAddress(pO.getString("address"));
                if (pO.has("apkVersion")) Config.setApk(pO.getString("apkVersion"));
            }
        } catch (Exception ignored) {}
        return newFixedLengthResponse(s);
    }
}
