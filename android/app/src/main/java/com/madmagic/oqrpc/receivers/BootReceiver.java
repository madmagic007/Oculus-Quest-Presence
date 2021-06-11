package com.madmagic.oqrpc.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.madmagic.oqrpc.main.MainService;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context.getApplicationContext(), MainService.class);
        context.startForegroundService(i);
    }
}
