package ru.thstdio17.goodhobby.hobby;

/**
 * Created by shcherbakov on 12.08.2017.
 */

public class LogItem {
    long time;
    int count;

    public LogItem(long time, int count) {
        this.time = time;
        this.count = count;
    }

    public long getTime() {
        return time;
    }

    public int getCount() {
        return count;
    }

}
