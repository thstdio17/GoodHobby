package ru.thstdio17.goodhobby.sqlite.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by shcherbakov on 06.09.2017.
 */

public class LogTable {
    public static final String TABLE_NAME = "logs";

    public final class Cols {

        public static final String ID_DATE = "id";
        public static final String TAG = "tag";
        public static final String TXT = "text";

    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" +
                Cols.ID_DATE + " LARGEINT, " +
                Cols.TAG + " TEXT, " +
                Cols.TXT + " TEXT" +
                ")");
    }

    public static ContentValues getContentValues(long id, String tag, String text) {

        ContentValues values = new ContentValues();
        values.put(Cols.ID_DATE, id);
        values.put(Cols.TAG, tag);
        values.put(Cols.TXT, text);
        return values;
    }

}
