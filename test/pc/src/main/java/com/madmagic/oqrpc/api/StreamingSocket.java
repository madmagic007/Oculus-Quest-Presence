package com.madmagic.oqrpc.api;

import com.madmagic.oqrpc.gui.ScreenShareGUI;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class StreamingSocket extends ServerSocket {

    public StreamingSocket() throws IOException {
        super(16256);
        System.out.println("Starts streaming socket");

        new Thread(() -> {
            try {
                Socket s = accept();
                DataInputStream in = new DataInputStream(s.getInputStream());
                ScreenShareGUI.init();

                while (!s.isClosed()) {
                    int length = in.readInt();
                    byte[] data = new byte[length];
                    in.readFully(data);
                    ScreenShareGUI.displayFrame(data);
                }
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
