package ru.thstdio17.goodhobby.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.thstdio17.goodhobby.hobby.DateLab;
import ru.thstdio17.goodhobby.hobby.Hobby;
import ru.thstdio17.goodhobby.hobby.HobbyScheduler;
import ru.thstdio17.goodhobby.hobby.LogItem;
import ru.thstdio17.goodhobby.nottoall.LogsUnit;
import ru.thstdio17.goodhobby.sqlite.table.CurrentTable;
import ru.thstdio17.goodhobby.sqlite.table.FinishTable;
import ru.thstdio17.goodhobby.sqlite.table.HobbyTable;
import ru.thstdio17.goodhobby.sqlite.table.LogTable;
import ru.thstdio17.goodhobby.sqlite.table.ShedulerTable;

/**
 * Created by shcherbakov on 08.08.2017.
 */

public class SqliteBD {
    private static SqliteBD ourInstance = null;
    private Context mContext;
    private SQLiteDatabase mDatabase;


    public static SqliteBD getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SqliteBD(context);
        }
        return ourInstance;
    }

    private SqliteBD(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new BsHelper(mContext)
                .getWritableDatabase();

    }

    private Cursor myQuery(String tableName, String whereClause, String[] whereArgs) {


        Cursor cursor = mDatabase.query(
                tableName,
                null, // Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        int c = cursor.getCount();
        return cursor;
    }

    private Cursor myMaxQuery(String tableName, String column, String whereClause, String[] whereArgs) {

        String maxCol[] = {"Max(" + column + ") as id"};
        Cursor cursor = mDatabase.query(
                tableName,
                maxCol, // Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        int c = cursor.getCount();
        return cursor;
    }

    private String findMaxColl(String table, String coll, String whereClause, String[] whereArgs) {
        MyCursorWrapper cursor = new MyCursorWrapper(myMaxQuery(table, coll, whereClause, whereArgs));
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String str = cursor.getId();
                str = str == null ? "" : str;
                return str;
            }
        } finally {
            cursor.close();
        }
        return "";
    }

    private Cursor mySymQuery(String tableName, String column, String whereClause, String[] whereArgs) {

        String maxCol[] = {"Sum(" + column + ") as id"};
        Cursor cursor = mDatabase.query(
                tableName,
                maxCol, // Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );
        int c = cursor.getCount();
        return cursor;
    }

    private String findSummColl(String table, String coll, String whereClause, String[] whereArgs) {
        MyCursorWrapper cursor = new MyCursorWrapper(mySymQuery(table, coll, whereClause, whereArgs));
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String str = cursor.getId();
                str = str == null ? "" : str;
                return str;
            }
        } finally {
            cursor.close();
        }
        return "";
    }

    private Cursor myOrderQuery(String tableName, String whereClause, String[] whereArgs, String order) {


        Cursor cursor = mDatabase.query(
                tableName,
                null, // Columns - null выбирает все столбцы
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                order // orderBy
        );
        int c = cursor.getCount();
        return cursor;
    }

    public void setHobby(Hobby hobby) {
        ContentValues values = HobbyTable.getContentValues(hobby.toMass());
        mDatabase.insert(HobbyTable.TABLE_NAME, null, values);
    }

    public Hobby getHobbyForId(int id) {
        String myWhere = HobbyTable.Cols.ID + " = ?";
        String[] myArg = {new Integer(id).toString()};

        MyCursorWrapper cursor = new MyCursorWrapper(myQuery(HobbyTable.TABLE_NAME, myWhere, myArg));

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Hobby hobby = cursor.getHobby();
                return hobby;
            }
        } finally {
            cursor.close();
        }

        return null;
    }

    public Hobby getHobbyForPosition(int position) {
        String myWhere = HobbyTable.Cols.POSITION + " = ?";
        String[] myArg = {new Integer(position).toString()};

        MyCursorWrapper cursor = new MyCursorWrapper(myQuery(HobbyTable.TABLE_NAME, myWhere, myArg));

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Hobby hobby = cursor.getHobby();
                return hobby;
            }
        } finally {
            cursor.close();
        }

        return null;
    }

    public List<Hobby> getListHobby() {
        List<Hobby> list = new ArrayList<>();
        String myWhere = null;
        String[] myArg = null;

        MyCursorWrapper cursor = new MyCursorWrapper(myQuery(HobbyTable.TABLE_NAME, myWhere, myArg));

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursor.getHobby());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return list;
    }

    public int getMaxPosition() {
        String str = findMaxColl(HobbyTable.TABLE_NAME, HobbyTable.Cols.POSITION, null, null);
        if (str.equals("")) return 0;
        return Integer.parseInt(str);
    }

    public void setHobbyResult(int id, int progress) {
        long start = DateLab.now();
        ContentValues values = CurrentTable.getContentValues(id, start, progress);
        mDatabase.insert(CurrentTable.TABLE_NAME, null, values);
    }

    public void setHobbyResult(int id, long start, int progress) {
        ContentValues values = CurrentTable.getContentValues(id, start, progress);
        mDatabase.insert(CurrentTable.TABLE_NAME, null, values);
    }

    public void startHobby(Hobby hobby) {
        hobby.start();
        updateHobby(hobby);
    }

    public int summProgressHobby(int id) {
        String myWhere = CurrentTable.Cols.ID + " = ?";
        String[] myArg = {new Integer(id).toString()};
        String str = findSummColl(CurrentTable.TABLE_NAME, CurrentTable.Cols.PROGRESS, myWhere, myArg);
        if (str.equals("")) return 0;
        return Integer.parseInt(str);

    }

    public void updateHobby(Hobby hobby) {
        ContentValues values = HobbyTable.getContentValues(hobby.toMass());
        mDatabase.update(HobbyTable.TABLE_NAME, values,
                HobbyTable.Cols.ID + " = ?",
                new String[]{new Integer(hobby.getId()).toString()});
    }

    public List<LogItem> getLogHobby(int id) {
        List<LogItem> list = new ArrayList<>();
        String myWhere = CurrentTable.Cols.ID + " = ?";
        String[] myArg = {new Integer(id).toString()};

        MyCursorWrapper cursor = new MyCursorWrapper(myQuery(CurrentTable.TABLE_NAME, myWhere, myArg));

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                list.add(cursor.getLogItem());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public void finishHobby(Hobby hobby) {
        hobby.finish();
        updateHobby(hobby);
        ContentValues values = FinishTable.getContentValues(hobby.getId(), DateLab.now());
        mDatabase.insert(FinishTable.TABLE_NAME, null, values);
    }

    public long getHobbyFinish(int id) {
        String myWhere = FinishTable.Cols.ID + " = ?";
        String[] myArg = {new Integer(id).toString()};
        String str = findMaxColl(FinishTable.TABLE_NAME, FinishTable.Cols.FINISH, myWhere, myArg);
        if (str.equals("")) return 0;
        return Long.parseLong(str);
    }

    public void setSheduler(HobbyScheduler shelder) {
        for (int i = 0; i < shelder.size(); i++) {
            ContentValues values = ShedulerTable.getContentValues(shelder.getHobbyId(), shelder.getType(), shelder.getDays(i));
            mDatabase.insert(ShedulerTable.TABLE_NAME, null, values);
        }
    }

    public ArrayList<Integer> getIdsSheduler() {
        ArrayList<Integer> list = new ArrayList<>();
        String myWhere = null;
        String[] myArg = null;

        MyCursorWrapper cursor = new MyCursorWrapper(myQuery(ShedulerTable.TABLE_NAME, myWhere, myArg));

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String str = cursor.getId();
                if (str.equals("")) str = "0";
                list.add(Integer.parseInt(str));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        Set<Integer> uniqueList = new HashSet<Integer>(list);
        return new ArrayList<>(uniqueList);
    }

    public List<HobbyScheduler> getShedulerList() {
        ArrayList<Integer> ids = getIdsSheduler();
        List<HobbyScheduler> list = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            list.add(getShedulerList(ids.get(i)));
        }
        return list;
    }

    public HobbyScheduler getShedulerList(int id) {
        String myWhere = ShedulerTable.Cols.ID + " = ?";
        String[] myArg = {new Integer(id).toString()};

        MyCursorWrapper cursor = new MyCursorWrapper(myQuery(ShedulerTable.TABLE_NAME, myWhere, myArg));
        HobbyScheduler shelder = new HobbyScheduler();
        shelder.setHobbyId(id);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                cursor.changeShelder(shelder);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return shelder;
    }

    public void deleteShuduler(int id) {
        mDatabase.delete(ShedulerTable.TABLE_NAME,
                ShedulerTable.Cols.ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void deleteHobbyFull(int id) {
        deleteShuduler(id);
        mDatabase.delete(CurrentTable.TABLE_NAME,
                CurrentTable.Cols.ID + " = ?",
                new String[]{String.valueOf(id)});
        mDatabase.delete(HobbyTable.TABLE_NAME,
                HobbyTable.Cols.ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void writeLog(String tag, String txt) {
        ContentValues values = LogTable.getContentValues(DateLab.now(), tag,txt);
        mDatabase.insert(LogTable.TABLE_NAME, null, values);
    }
    public List<LogsUnit> readLog(String tag) {
        List<LogsUnit> list=new ArrayList<>();
        String myWhere = LogTable.Cols.TAG + " = ?";;
        String[] myArg = {tag};

        MyCursorWrapper cursor = new MyCursorWrapper(myQuery(LogTable.TABLE_NAME, myWhere, myArg));

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                LogsUnit log = cursor.getLog();
                 list.add(log);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return list;
    }

    public List<LogsUnit> readLog() {
        List<LogsUnit> list=new ArrayList<>();
        String myWhere = null;
        String[] myArg = null;

        MyCursorWrapper cursor = new MyCursorWrapper(myQuery(LogTable.TABLE_NAME, myWhere, myArg));

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                LogsUnit log = cursor.getLog();
                list.add(log);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return list;
    }
    public void delLog() {
        mDatabase.delete(LogTable.TABLE_NAME,
                null,
                null);
    }
}
