package com.madmagic.oqrpc.api;

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
        try {
            final HashMap<String, String> map = new HashMap<>();
            session.parseBody(map);
            final String json = map.get("postData");
            ResponseHandler.handle(new JSONObject(json));
        } catch (Exception ignored) {
        }
        return newFixedLengthResponse("");
    }
}
