package ru.thstdio17.goodhobby.hobby;

/**
 * Created by shcherbakov on 08.08.2017.
 */

public class Hobby {
    int id;
    int position;
    String name, description = "";
    int status = 1; //0 активна/ 1 неактивна/ 2 завершена
    long start = 0;
    int count;
    boolean oneDay = true;
    int iconId = 0;

    public Hobby(int id, int position, String name) {
        this.id = id;
        this.position = position;
        this.name = name;
    }

    public Hobby setPosition(int position) {
        this.position = position;
        return this;
    }

    public Hobby setName(String name) {
        this.name = name;
        return this;
    }

    public Hobby setDescription(String description) {
        this.description = description;
        return this;
    }

    public Hobby setStatus(int status) {
        this.status = status;
        return this;
    }

    public Hobby setStart(long start) {
        this.start = start;
        return this;
    }

    public int getIconId() {
        return iconId;
    }

    public Hobby setIconId(int iconId) {
        this.iconId = iconId;
        return this;
    }

    public int getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getStatus() {
        return status;
    }

    public long getStart() {
        return start;
    }


    public String[] toMass() {
        return new String[]{String.valueOf(id), String.valueOf(position), name, description, String.valueOf(start), String.valueOf(status), String.valueOf(iconId), oneDay ? "1" : "0"};

    }

    public int getCount() {
        return count;
    }

    public Hobby setCount(int count) {
        this.count = count;
        return this;
    }

    public void start() {
        start = DateLab.now();
        status = 0;
    }

    public void finish() {
        status = 2;
    }

    public boolean isOneDay() {
        return oneDay;
    }

    public Hobby setOneDay(boolean oneDay) {
        this.oneDay = oneDay;
        return this;
    }
}
