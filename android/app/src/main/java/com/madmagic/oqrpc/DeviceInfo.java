package com.madmagic.oqrpc;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

import com.madmagic.oqrpc.main.MainService;
import com.rvalerio.fgchecker.AppChecker;

import org.json.JSONObject;

public class DeviceInfo {

    public static String getInfo(MainService s) {
        try {
            Log.d("OQRPC", "getting info");
            JSONObject r = new JSONObject();

            Intent batteryIntent = s.getBaseContext().getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            r.put("batteryLevel", level + "%");

            String name = BluetoothAdapter.getDefaultAdapter().getName();
            r.put("name", name);

            r.put("currentTop", getTopmost(s))
                    .put("ownAddress", MainService.getIp(s))
                    .put("pcAddress", Config.getAddress().isEmpty() ? "not set" : Config.getAddress());

            return r.toString(4);
        } catch (Exception ignored) {
            return "";
        }
    }

    public static String getTopmost(Context c) {
        return new AppChecker().getForegroundApp(c);
    }
}
