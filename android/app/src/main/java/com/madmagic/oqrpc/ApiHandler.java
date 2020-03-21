package com.madmagic.oqrpc;

import android.util.Log;

import org.json.JSONObject;

public class ApiHandler {

    public static JSONObject received(JSONObject o) {
        try {
            String message = o.getString("message");

            if (message.equals("game")) {
                return new JSONObject().put("game", ActivityGetter.getName()).put("message", "game");
            }

            if (message.equals("startup")) {
                return new JSONObject().put("message", "started");
            }

            if (o.has("address")) {
                ConfigCreator.write(new JSONObject().put("address", o.getString("address")));
                return new JSONObject().put("message", "valid");
            }

        } catch (Exception e) {
            Log.d("OQRPC", "error receiever " + e.getMessage());
        }
        return new JSONObject();
    }
}