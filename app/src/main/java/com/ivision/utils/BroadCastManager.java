package com.ivision.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BroadCastManager {

    private static BroadCastManager broadCastManager = new BroadCastManager();

    public static BroadCastManager getInstance() {
        return broadCastManager;
    }

    //Registered broadcast recipient
    public void registerReceiver(Activity activity, BroadcastReceiver receiver, IntentFilter filter) {
        activity.registerReceiver(receiver, filter);
    }

    //Logout broadcast recipient
    public void unregisterReceiver(Activity activity, BroadcastReceiver receiver) {
        activity.unregisterReceiver(receiver);
    }

    //Send broadcast
    public void sendBroadCast(Activity activity, Intent intent) {
        activity.sendBroadcast(intent);
    }

    public void sendBroadCast(Context context, Intent intent) {
        context.sendBroadcast(intent);
    }
}
