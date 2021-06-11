package com.madmagic.oqrpc.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.madmagic.oqrpc.Config;
import com.madmagic.oqrpc.main.MainService;

public class ScreenReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_SCREEN_OFF:
                if (MainService.isRunning(context.getApplicationContext(), MainService.class) && Config.getSleepWake()) {
                    context.stopService(new Intent(context.getApplicationContext(), MainService.class));
                }
                break;
            case Intent.ACTION_SCREEN_ON:
                if (!MainService.isRunning(context.getApplicationContext(), MainService.class) && Config.getSleepWake())
                    context.startService(new Intent(context.getApplicationContext(), MainService.class));
                break;
        }
    }

    private static boolean registered = false;
    public static void register(Context context) {
        if (registered) return;
        registered = true;

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        BroadcastReceiver mReceiver = new ScreenReceiver();
        context.registerReceiver(mReceiver, filter);
    }
}
