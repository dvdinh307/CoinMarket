package midas.coinmarket.controller.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import midas.coinmarket.utils.AlarmManagerUtils;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            AlarmManagerUtils.instanceAlarm(context).setAlarmDaily();
        }
    }
}