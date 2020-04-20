package com.madmagic.oqrpc;

import android.content.Context;

import com.rvalerio.fgchecker.AppChecker;

public class ActivityGetter {

    private static Context c;

    public static void define(Context co) {
        c = co;
    }

    public static String getName() {
        return new AppChecker().getForegroundApp(c);
    }
}
