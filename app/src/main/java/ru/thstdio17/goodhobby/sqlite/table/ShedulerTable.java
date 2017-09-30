package ru.thstdio17.goodhobby.sqlite.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by shcherbakov on 16.08.2017.
 */

public class ShedulerTable {
    public static final String TABLE_NAME = "sheduler";

    public final class Cols {

        public static final String ID = "id";
        public static final String TYPE = "type";
        public static final String VALUE = "value";

    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" +
                ShedulerTable.Cols.ID + " integer, " +
                Cols.TYPE + " integer, " +
                Cols.VALUE + " integer" +
                ")");
    }

    public static ContentValues getContentValues(int id, long type, int value) {

        ContentValues values = new ContentValues();
        values.put(ShedulerTable.Cols.ID, id);
        values.put(Cols.TYPE, type);
        values.put(Cols.VALUE, value);

        return values;
    }


}