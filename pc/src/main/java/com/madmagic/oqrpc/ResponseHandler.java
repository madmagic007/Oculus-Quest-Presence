package com.madmagic.oqrpc;

import org.json.JSONObject;

public class ResponseHandler {

    public static void handle(JSONObject response) {
        String message = response.getString("message");

        if (message.equals("valid")) InitFrame.error.setText("device found and running apk. Ready to go!");
        if (message.equals("started")) {
            System.out.println("device online");
            Timing.terminate();
            SystemTrayHandler.notif();
            Discord.init();
            Timing.startRequester();
            Timing.startEnder();
        }
        if (message.equals("game")) {
            Timing.resetEnder();
            Discord.handle(response.getString("game"));
        }
    }
}
