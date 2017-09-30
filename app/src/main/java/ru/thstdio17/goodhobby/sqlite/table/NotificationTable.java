package ru.thstdio17.goodhobby.sqlite.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by shcherbakov on 18.08.2017.
 */

public class NotificationTable {
    public static final String TABLE_NAME = "notification";

    public final class Cols {

        public static final String ID = "id";
        public static final String H = "h";
        public static final String M = "m";
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" +
                Cols.ID + " integer primary key autoincrement, " +
                Cols.H + " integer, " +
                Cols.M + " integer" +
                ")");

    }

    public static ContentValues getContentValues(int id, int h, int m) {

        ContentValues values = new ContentValues();
        values.put(Cols.ID, id);
        values.put(Cols.H, h);
        values.put(Cols.M, m);
        return values;
    }
}
