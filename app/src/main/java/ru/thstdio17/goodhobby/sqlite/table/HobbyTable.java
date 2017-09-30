package ru.thstdio17.goodhobby.sqlite.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by shcherbakov on 08.08.2017.
 */

public class HobbyTable {
    public static final String TABLE_NAME = "hobby";

    public final class Cols {

        public static final String ID = "id";
        public static final String POSITION = "position";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String START = "start";
        public static final String STATUS = "status";
        public static final String ICON_ID = "icon_id";
        public static final String ONE_DAY = "one_day";
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" +
                Cols.ID + " integer primary key autoincrement, " +
                Cols.POSITION + " integer, " +
                Cols.NAME + ", " +
                Cols.DESCRIPTION + ", " +
                Cols.START + " LARGEINT, " +
                Cols.STATUS + " integer, " +
                Cols.ICON_ID + " integer, " +
                Cols.ONE_DAY + " integer" +
                ")");

    }

    public static ContentValues getContentValues(String[] val) {

        ContentValues values = new ContentValues();
        //  values.put(Cols.ID, val[0]);
        values.put(Cols.POSITION, val[1]);
        values.put(Cols.NAME, val[2]);
        values.put(Cols.DESCRIPTION, val[3]);
        values.put(Cols.START, val[4]);
        values.put(Cols.STATUS, val[5]);
        values.put(Cols.ICON_ID, val[6]);
        values.put(Cols.ONE_DAY, val[7]);
        return values;
    }
}
