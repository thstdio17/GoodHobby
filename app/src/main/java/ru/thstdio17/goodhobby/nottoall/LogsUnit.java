package ru.thstdio17.goodhobby.nottoall;

import ru.thstdio17.goodhobby.sqlite.table.LogTable;

/**
 * Created by shcherbakov on 06.09.2017.
 */

public class LogsUnit {
    long id;
    String tag;
    String txt;

    public long getId() {
        return id;
    }

    public LogsUnit setId(long id) {
        this.id = id;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public LogsUnit setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getTxt() {
        return txt;
    }

    public LogsUnit setTxt(String txt) {
        this.txt = txt;
        return this;
    }
}
