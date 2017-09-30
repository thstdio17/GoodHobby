package ru.thstdio17.goodhobby.doneHobby;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import java.util.List;

import ru.thstdio17.goodhobby.R;
import ru.thstdio17.goodhobby.abstrct.SinglePageFragmentActivity;
import ru.thstdio17.goodhobby.hobby.DateLab;
import ru.thstdio17.goodhobby.hobby.Hobby;
import ru.thstdio17.goodhobby.hobby.LogItem;
import ru.thstdio17.goodhobby.info.InfoActivity;
import ru.thstdio17.goodhobby.sqlite.SqliteBD;

/**
 * Created by shcherbakov on 11.08.2017.
 */

public class DoneHobbyActivity extends SinglePageFragmentActivity {
    private static String EXTRA_ID = "idHobby";
    int id;
    int numPage = 2;
    ToUdate[] update = new ToUdate[3];
    boolean isDone = false;
    protected SharedPreferences prefs;

    interface ToUdate {
        void update();
    }

    public static Intent newIntent(Context packageContext, int id) {
        Intent intent = new Intent(packageContext, DoneHobbyActivity.class);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }


    @Override
    protected int getNumberPage() {
        return numPage;
    }

    @Override
    protected Fragment setFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 1:
                fragment = DoneHobbyFragment.newInstance(id, isDone);
                update[position] = (DoneHobbyFragment) fragment;
                break;
            case 0:
                fragment = DoneHobbyInfoFragment.newInstance(id);
                update[position] = (DoneHobbyInfoFragment) fragment;
        }

        return fragment;
    }

    @Override
    public void init() {
        id = getIntent().getIntExtra(EXTRA_ID, 0);
        SqliteBD bs;
        Hobby hobby;
        bs = SqliteBD.getInstance(this);
        hobby = bs.getHobbyForId(id);
        if (hobby.isOneDay() && hobby.getStatus() == 0) {
            List<LogItem> tempLastDay = bs.getLogHobby(id);
            long last = 0;
            for (int i = 0; i < tempLastDay.size(); i++) {
                last = Math.max(last, tempLastDay.get(i).getTime());
            }
            long delta = (DateLab.now() - last) / DateLab.HMs(1);
            Log.i("Thstdio", hobby.getName() + " delta for done "
                    + delta);
            if (delta < 3) isDone = true;
        }
        fab.setVisibility(View.GONE);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean first_dialog = prefs.getBoolean("main_first_dialog", true);
        if (first_dialog) showFirstDialog();
    }

    private void showFirstDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true).setMessage(R.string.done_done_first_dialog)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putBoolean("main_first_dialog", false);
                                editor.commit();
                                Intent intent = InfoActivity.newIntent(getApplicationContext(), 1);
                                startActivity(intent);
                                dialog.cancel();
                            }
                        }
                ).setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("main_first_dialog", false);
                        editor.commit();
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected CharSequence getMyTitle(int position) {
        switch (position) {
            case 1:
                return "Привычка";
            case 2:
                return "Прогресс";
            case 0:
                return "Описание";
        }
        return "";
    }

    @Override
    protected int getCurrentItem() {
        return 1;
    }

    @Override
    protected void tabSelect(int idTab) {

    }

    @Override
    protected void fabOnClic() {

    }

    public void reload() {
        isDone = true;
        for (int i = 0; i < numPage; i++) update[i].update();
    }
}
