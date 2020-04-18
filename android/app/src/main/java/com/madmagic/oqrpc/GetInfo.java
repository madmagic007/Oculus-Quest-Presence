package com.madmagic.oqrpc;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import com.rvalerio.fgchecker.AppChecker;

import org.json.JSONObject;

import java.net.InetAddress;

public class GetInfo {

    public static String getInfo() {
        try {
            Log.d("OQRPC", "getting info");
            JSONObject r = new JSONObject();
            Service s = MainService.s;

            Intent batteryIntent = s.getBaseContext().getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            r.put("batteryLevel", level + "%");

            String name = BluetoothAdapter.getDefaultAdapter().getName();
            r.put("name", name);

            r.put("currentTop", new AppChecker().getForegroundApp(s.getBaseContext()))
                    .put("ownAdress", MainService.getIp())
                    .put("pcIp", ConfigCreator.getIp().isEmpty() ? "not set" : ConfigCreator.getIp());
            return r.toString(4);
        } catch (Exception ignored) {
            return "";
        }
    }
}
