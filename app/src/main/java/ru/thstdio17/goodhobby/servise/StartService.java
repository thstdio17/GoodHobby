package ru.thstdio17.goodhobby.servise;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ru.thstdio17.goodhobby.R;
import ru.thstdio17.goodhobby.doneHobby.DoneHobbyActivity;
import ru.thstdio17.goodhobby.hobby.DateLab;
import ru.thstdio17.goodhobby.hobby.Hobby;
import ru.thstdio17.goodhobby.hobby.HobbyScheduler;
import ru.thstdio17.goodhobby.nottoall.MyLog;
import ru.thstdio17.goodhobby.sqlite.SqliteBD;

public class StartService extends Service {
    SqliteBD bs;
    private int NOTIFY_ID = 1;
    int i = 1;
    private Timer mTimer;
    private MyLog log;

    final static int RQS_TIME = 1;

    public StartService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();

    }

    private void init() {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), RQS_TIME, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, DateLab.nowDayPlusHour(8),
              AlarmManager.INTERVAL_DAY,
                pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, DateLab.nowDayPlusHour(18),
                AlarmManager.INTERVAL_DAY,
                pendingIntent);
//        mTimer = new Timer();
//        TimerTask task1 = new TimerTask() {
//            @Override
//            public void run() {
//                log.log("service", "Schedule morning done");
//                isRunNote();
//            }
//        };
//        TimerTask task2 = new TimerTask() {
//            @Override
//            public void run() {
//                log.log("service", "Schedule evening done");
//                isRunNote();
//            }
//        };
//        Date date1, date2;
//        date1 = DateLab.nowDayPlusHour(8);
//        date2 = DateLab.nowDayPlusHour(18);
//        log.log("service", "Schedule morning" + date1.toString());
//        mTimer.schedule(task1, date1, DateLab.HMs(24));
//        log.log("service", "Schedule evening" + date2.toString());
//        mTimer.schedule(task2, date2, DateLab.HMs(24));
    }

    @Override
    public void onDestroy() {
        Log.i("Thstdio17_GoodHobby", "Stop Service");
        super.onDestroy();
    }
}
