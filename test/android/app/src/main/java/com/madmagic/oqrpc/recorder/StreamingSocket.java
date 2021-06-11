package com.madmagic.oqrpc.recorder;

import android.util.Log;
import com.madmagic.oqrpc.Config;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class StreamingSocket extends Socket {

    private final DataOutputStream out;

    public StreamingSocket() throws Exception {
        super(Config.getAddress(), 16256);
        out = new DataOutputStream(getOutputStream());
        Log.d("OQRPC", "socket initialised");
    }

    public void streamImage(byte[] data) throws InterruptedException {
        Thread t = new Thread(() -> {
            try {
                out.writeInt(data.length);
                out.write(data);
            } catch (IOException e) {
                Log.d("OQRPC", Log.getStackTraceString(e));
            }
        });
        t.start();
        t.join();
    }

    public void end() throws IOException {
        out.close();
        close();
    }
}
