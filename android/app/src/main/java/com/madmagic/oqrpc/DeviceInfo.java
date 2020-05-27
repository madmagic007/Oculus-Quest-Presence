package com.madmagic.oqrpc;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

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
                    .put("ownAdress", MainService.getIp(s))
                    .put("pcIp", Config.getIp().isEmpty() ? "not set" : Config.getIp());

            return r.toString(4);
        } catch (Exception ignored) {
            return "";
        }
    }

    public static String getTopmost(MainService s) {
        return new AppChecker().getForegroundApp(s.getBaseContext());
    }
}
