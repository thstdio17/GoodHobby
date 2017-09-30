package ru.thstdio17.goodhobby.sqlite.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by shcherbakov on 08.08.2017.
 */

public class CurrentTable {
    public static final String TABLE_NAME = "current";

    public final class Cols {

        public static final String ID = "id";
        public static final String START = "start";
        public static final String PROGRESS = "progress";

    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" +
                Cols.ID + " integer, " +
                Cols.START + " LARGEINT, " +
                Cols.PROGRESS + " integer" +
                ")");
    }

    public static ContentValues getContentValues(int id, long start, int progress) {

        ContentValues values = new ContentValues();
        values.put(Cols.ID, id);
        values.put(Cols.START, start);
        values.put(Cols.PROGRESS, progress);

        return values;
    }
}
