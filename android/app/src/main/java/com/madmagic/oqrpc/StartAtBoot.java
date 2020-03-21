package com.madmagic.oqrpc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class StartAtBoot extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "received", Toast.LENGTH_LONG).show();

        Intent i = new Intent(context, MainService.class);
        context.startService(i);
    }
}
