package ru.thstdio17.goodhobby.servise;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by shcherbakov on 24.07.2017.
 */

public class ReciverStartService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, StartService.class));
        Log.i("Thstdio17_GoodHobby","start receive");
    }
}
