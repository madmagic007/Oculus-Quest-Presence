package com.madmagic.oqrpc.api;

import com.madmagic.oqrpc.source.Config;
import com.madmagic.oqrpc.gui.ConfigGUI;
import com.madmagic.oqrpc.source.Discord;
import com.madmagic.oqrpc.source.Main;
import com.madmagic.oqrpc.source.SystemTrayHandler;
import com.madmagic.oqrpc.source.Timing;
import org.json.JSONObject;

import java.awt.*;

public class ResponseHandler {

    public static String handle(String type) {
        String s = "{}";

        if (type.equals("started")) {
            System.out.println("received device online message");
            SystemTrayHandler.notif("Quest online", "Your Quest is online");
            Discord.init();
            Timing.startRequester();
            Timing.startEnder();
        }

        if (type.equals("ended")) {
            System.out.println("device offline");
            SystemTrayHandler.notif("Quest offline", "RPC service on your Quest has stopped");
            Discord.terminate();
            Timing.terminate();
        }

        if (type.equals("connect")) {
            s = new JSONObject()
                    .put("connected", "And you found another secret :)")
                    .toString(4);
        }

        if (type.equals("note")) {
            SystemTrayHandler.notif("test", "test");
        }

        return s;
    }
}
