package ru.thstdio17.goodhobby.nottoall;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import ru.thstdio17.goodhobby.sqlite.SqliteBD;

/**
 * Created by shcherbakov on 06.09.2017.
 */

public class MyLog {
    private static final MyLog ourInstance = new MyLog();
    private static SqliteBD mBs;
    private static Context mContext;
    private static boolean isConsoleLog = false;
    private static boolean isBsLog = false;

    public static MyLog getInstance(Context context) {
        mContext=context;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        isConsoleLog = prefs.getBoolean("isLogConsole", false);
        isBsLog = prefs.getBoolean("isLogBs", false);
        mBs = SqliteBD.getInstance(context);
        return ourInstance;
    }

    private MyLog() {
    }

  public static void log(String tag,String txt){
        if(isConsoleLog) {
              Log.i("Thstdio17_GoodHobby", tag+" --- " +txt);
        }
      if(isBsLog) {
         mBs.writeLog(tag,txt);
      }
  }
}
