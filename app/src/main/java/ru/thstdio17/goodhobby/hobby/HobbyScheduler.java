package ru.thstdio17.goodhobby.hobby;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.thstdio17.goodhobby.R;
import ru.thstdio17.goodhobby.sqlite.SqliteBD;

/**
 * Created by shcherbakov on 16.08.2017.
 */

public class HobbyScheduler implements Parcelable {
    int hobbyId = -1;
    int type; //0 каждый день  ; 1 каждый X день  ;2  количество в неделю ;3 выбрать дни
    ArrayList<Integer> days;
    int done2 = 0;  // сколько раз за последние 7 дней уже сделано. Для type=2

    public HobbyScheduler(int type) {
        this.type = type;
        days = new ArrayList();
    }

    public HobbyScheduler() {
        days = new ArrayList();
    }

    public HobbyScheduler setType(int type) {
        this.type = type;
        return this;
    }

    public HobbyScheduler setHobbyId(int hobbyId) {
        this.hobbyId = hobbyId;
        return this;
    }

    public int getHobbyId() {
        return hobbyId;
    }

    public int getType() {
        return type;
    }

    public int getDays(int i) {
        return days.get(i);
    }

    public ArrayList<Integer> getDays() {
        return days;
    }

    public int size() {
        return days.size();
    }

    public HobbyScheduler addDays(int day) {
        this.days.add(day);
        return this;
    }

    public int nextDayNumber(Context context) {

        SqliteBD bs = SqliteBD.getInstance(context);
        Hobby hobby = bs.getHobbyForId(hobbyId);
        List<LogItem> list;
        //проверка на активность
        if (hobby.getStatus() != 0) return -1;
        list = bs.getLogHobby(hobbyId);
        Collections.sort(list, new Comparator<LogItem>() {
            @Override
            public int compare(LogItem l1, LogItem l2) {
                if (l1.getTime() > l2.getTime()) return 1;
                else if (l1.getTime() < l2.getTime()) return -1;
                else return 0;
            }
        });
        int[] rangeDay = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            rangeDay[i] = DateLab.countDayFromNow(list.get(i).getTime());
        }
        int n = 0;
        switch (type) {
            case 0:
                if (rangeDay[rangeDay.length - 1] == 0) n = 1;
                return n;
            case 1:
                n = days.get(0) - rangeDay[rangeDay.length - 1];
                n = n < 0 ? 0 : n;
                return n;
            case 2:
                int i = 1;
                while (rangeDay[rangeDay.length - i] < 7) {
                    if (rangeDay.length - i == 0) break;
                    i++;
                }
                done2 = i;
                if (i < days.get(0)) {
                    if (rangeDay[rangeDay.length - 1] == 0) n = 1;
                    return n;
                } else {

                    return 7 - rangeDay[rangeDay.length - 1];
                }
            case 3:
                int weekDay = DateLab.dayWeekNow();
                if (rangeDay[rangeDay.length - 1] == 0) n = 1;
                int newWeekDay = (weekDay + n) % 7;
                int temp = days.get(0);
                for (int j : days) {
                    if (newWeekDay <= j) {
                        temp = j;
                        break;
                    }
                }
                if (newWeekDay > temp) return temp + 7 - weekDay;
                return temp - weekDay;
        }

        return n;
    }

    public String nextDay(Context context) {
        int day = nextDayNumber(context);
        if (day < 0) return context.getString(R.string.start_today);
        String[] nextStr = context.getResources().getStringArray(R.array.next_day);
        return nextStr[day];
    }


    @Override
    public String toString() {
        String str = "";
        switch (type) {
            case 0:
                return "Каждый день";
            case 1:
                return "Каждый " + days.get(0) + "-й день";
            case 2:
                return days.get(0) + " дн в неделю";
            case 3:
                String[] weekday = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};
                str = " По дням недели:" + System.lineSeparator();
                for (int i : days) {
                    str += " " + weekday[i];
                }
        }
        return str;
    }

    public int getDone2() {
        return done2;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.hobbyId);
        dest.writeInt(this.type);
        dest.writeList(this.days);
        dest.writeInt(this.done2);
    }

    protected HobbyScheduler(Parcel in) {
        this.hobbyId = in.readInt();
        this.type = in.readInt();
        this.days = new ArrayList<Integer>();
        in.readList(this.days, Integer.class.getClassLoader());
        this.done2 = in.readInt();
    }

    public static final Parcelable.Creator<HobbyScheduler> CREATOR = new Parcelable.Creator<HobbyScheduler>() {
        @Override
        public HobbyScheduler createFromParcel(Parcel source) {
            return new HobbyScheduler(source);
        }

        @Override
        public HobbyScheduler[] newArray(int size) {
            return new HobbyScheduler[size];
        }
    };

    public void clearDays() {
        this.days = new ArrayList<Integer>();
    }
}
