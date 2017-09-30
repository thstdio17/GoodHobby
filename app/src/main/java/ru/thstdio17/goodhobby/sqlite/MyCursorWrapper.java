package ru.thstdio17.goodhobby.sqlite;


import android.database.Cursor;
import android.database.CursorWrapper;

import ru.thstdio17.goodhobby.hobby.Hobby;
import ru.thstdio17.goodhobby.hobby.HobbyScheduler;
import ru.thstdio17.goodhobby.hobby.LogItem;
import ru.thstdio17.goodhobby.nottoall.LogsUnit;
import ru.thstdio17.goodhobby.sqlite.table.CurrentTable;
import ru.thstdio17.goodhobby.sqlite.table.HobbyTable;
import ru.thstdio17.goodhobby.sqlite.table.LogTable;
import ru.thstdio17.goodhobby.sqlite.table.ShedulerTable;

/**
 * Created by shcherbakov on 09.06.2017.
 */

public class MyCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public MyCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public String[] getList() {
        String[] str = new String[5];
        str[0] = getString(getColumnIndex(HobbyTable.Cols.ID));
        str[1] = getString(getColumnIndex(HobbyTable.Cols.POSITION));
        str[2] = getString(getColumnIndex(HobbyTable.Cols.NAME));
        str[3] = getString(getColumnIndex(HobbyTable.Cols.DESCRIPTION));
        str[4] = getString(getColumnIndex(HobbyTable.Cols.START));
        str[5] = getString(getColumnIndex(HobbyTable.Cols.STATUS));
        return str;

    }

    public Hobby getHobby() {
        String[] str = new String[5];
        int id = getInt(getColumnIndex(HobbyTable.Cols.ID));
        int position = getInt(getColumnIndex(HobbyTable.Cols.POSITION));
        String name = getString(getColumnIndex(HobbyTable.Cols.NAME));
        String description = getString(getColumnIndex(HobbyTable.Cols.DESCRIPTION));
        long start = getLong(getColumnIndex(HobbyTable.Cols.START));
        int status = getInt(getColumnIndex(HobbyTable.Cols.STATUS));
        int oneDay = getInt(getColumnIndex(HobbyTable.Cols.ONE_DAY));
        int iconId = getInt(getColumnIndex(HobbyTable.Cols.ICON_ID));
        Hobby hobby = new Hobby(id, position, name);
        hobby.setDescription(description);
        hobby.setStart(start);
        hobby.setStatus(status);
        hobby.setOneDay(oneDay == 1);
        hobby.setIconId(iconId);
        return hobby;
    }

    public String getId() {
        String id = getString(getColumnIndex("id"));
        return id;
    }

    public LogItem getLogItem() {
        long time = getLong(getColumnIndex(CurrentTable.Cols.START));
        int progress = getInt(getColumnIndex(CurrentTable.Cols.PROGRESS));
        return new LogItem(time, progress);
    }

    public void changeShelder(HobbyScheduler shelder) {
        int type = getInt(getColumnIndex(ShedulerTable.Cols.TYPE));
        int values = getInt(getColumnIndex(ShedulerTable.Cols.VALUE));
        shelder.setType(type);
        shelder.addDays(values);
    }

    public LogsUnit getLog() {
        LogsUnit log= new LogsUnit();
        long id= getLong(getColumnIndex(LogTable.Cols.ID_DATE));
        String tag= getString(getColumnIndex(LogTable.Cols.TAG));
        String txt= getString(getColumnIndex(LogTable.Cols.TXT));
        log.setId(id).setTag(tag).setTxt(txt);
        return log;
    }
}
