package ru.thstdio17.goodhobby.sqlite.table;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by shcherbakov on 15.08.2017.
 */

public class FinishTable {
    public static final String TABLE_NAME = "finish";

    public final class Cols {

        public static final String ID = "id";
        public static final String FINISH = "finish";

    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(" +
                FinishTable.Cols.ID + " integer, " +
                Cols.FINISH + " LARGEINT" +
                ")");
    }

    public static ContentValues getContentValues(int id, long time) {

        ContentValues values = new ContentValues();
        values.put(Cols.ID, id);
        values.put(Cols.FINISH, time);


        return values;
    }
}
