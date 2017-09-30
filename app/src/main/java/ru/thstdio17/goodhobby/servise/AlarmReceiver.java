package ru.thstdio17.goodhobby.servise;

/**
 * Created by shcherbakov on 21.09.2017.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import java.util.List;

import ru.thstdio17.goodhobby.R;
import ru.thstdio17.goodhobby.doneHobby.DoneHobbyActivity;
import ru.thstdio17.goodhobby.hobby.Hobby;
import ru.thstdio17.goodhobby.hobby.HobbyScheduler;
import ru.thstdio17.goodhobby.sqlite.SqliteBD;

public class AlarmReceiver extends BroadcastReceiver {
  Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext=context;

        isRunNote();
    }
    SqliteBD bs;
    private void isRunNote() {
         bs = SqliteBD.getInstance(mContext);
        List<Hobby> tlist = bs.getListHobby();

        for (Hobby h : tlist)
            if (h.getStatus() == 0) {
                HobbyScheduler shelder = bs.getShedulerList(h.getId());
                if (shelder.nextDayNumber(mContext) < 1) show(h);

            }

    }

    public void show(Hobby hobby) {

        Intent notificationIntent = DoneHobbyActivity.newIntent(mContext, hobby.getId());
        PendingIntent contentIntent = PendingIntent.getActivity(mContext,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Resources res = mContext.getResources();
        TypedArray icons;
        icons = res.obtainTypedArray(R.array.icon_res);

        Notification.Builder builder = new Notification.Builder(mContext);
        Drawable myIcon = res.getDrawable(icons.getResourceId(hobby.getIconId(), 0));
        Bitmap icon;
        try {
            icon = Bitmap.createBitmap(myIcon.getIntrinsicWidth(), myIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_4444);

            Canvas canvas = new Canvas(icon);
            myIcon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            myIcon.draw(canvas);
        } catch (OutOfMemoryError e) {
            // Handle the error
            return;
        }


        builder.setContentIntent(contentIntent)
                // большая картинка
                .setLargeIcon(icon)
                .setSmallIcon(R.drawable.icon0_target)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                //.setContentText(res.getString(R.string.notifytext))
                .setContentText(hobby.getName()); // Текст уведомления

        // Notification notification = builder.getNotification(); // до API 16
        Notification notification = builder.build();

        NotificationManager notificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1000 + hobby.getId(), notification);
    }
}
