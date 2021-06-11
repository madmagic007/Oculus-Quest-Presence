package com.madmagic.oqrpc;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.util.Log;
import com.madmagic.oqrpc.main.MainService;
import com.rvalerio.fgchecker.AppChecker;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class DeviceInfo {

    public static String getInfo(MainService s) {
        try {
            Log.d("OQRPC", "getting info");
            JSONObject r = new JSONObject();

            Intent batteryIntent = s.getBaseContext().getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            r.put("batteryLevel", level + "%");

            r.put("name", getName());

            String[] topMost = getTopmost(s);
            r.put("currentTop", new JSONArray().put(topMost[0]).put(topMost[1]))
                    .put("ownAddress", MainService.getIp(s))
                    .put("pcAddress", Config.getAddress().isEmpty() ? "not set" : Config.getAddress());

            return r.toString(4);
        } catch (Exception ignored) {
            return "";
        }
    }

    public static String getName() {
        return BluetoothAdapter.getDefaultAdapter().getName();
    }

    private static final AppChecker checker = new AppChecker();

    public static String[] getTopmost(Context c) {
        String[] data = new String[] {checker.getForegroundApp(c), ""};

        try {
            PackageInfo packageInfo = c.getPackageManager().getPackageInfo(data[0], PackageManager.GET_META_DATA);

            try (ApkFile apkFile = new ApkFile(new File(packageInfo.applicationInfo.sourceDir))) {
                ApkMeta meta = apkFile.getApkMeta();
                data[1] = meta.getName();
            }
        } catch (Exception e) {
            Log.d("OQRPC", Log.getStackTraceString(e));
        }

        return data;
    }
}
