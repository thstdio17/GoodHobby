package ru.thstdio17.goodhobby.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ru.thstdio17.goodhobby.sqlite.table.CurrentTable;
import ru.thstdio17.goodhobby.sqlite.table.FinishTable;
import ru.thstdio17.goodhobby.sqlite.table.HobbyTable;
import ru.thstdio17.goodhobby.sqlite.table.LogTable;
import ru.thstdio17.goodhobby.sqlite.table.NotificationTable;
import ru.thstdio17.goodhobby.sqlite.table.ShedulerTable;


/**
 * Created by shcherbakov on 08.06.2017.
 */

public class BsHelper extends SQLiteOpenHelper {

    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "hobby.db";
    private boolean mNew = false;

    public BsHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public boolean isNew() {
        return mNew;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        mNew = true;
        HobbyTable.createTable(db);
        CurrentTable.createTable(db);
        FinishTable.createTable(db);
        ShedulerTable.createTable(db);
        NotificationTable.createTable(db);
        LogTable.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion==1){
            LogTable.createTable(db);
        }
    }

}
