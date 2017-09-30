package ru.thstdio17.goodhobby.hobby;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by shcherbakov on 22.06.2017.
 */

public class DateLab {
    private static int zone = 3;
    private static Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
            Locale.getDefault());
    // private static String[] month = {"Января", "Февраля", "Марта", "Апреля", "Мая", "Июня", "Июля", "Августа", "Сентября", "Октября", "Ноября", "Декабря"};

    public static long now() {
        Date date = new Date();
        return date.getTime();
    }

    public static long nowTZ() {

        return now() + HMs(localTZ());
    }

    private static int localTZ() {
        int i = parseToUserTime(now())[3];
        int l = calendar.get(Calendar.HOUR_OF_DAY);
        return 3;
    }

    public static String parseSecondt(int sec, String separator) {
        String str = "";
        int h = 0, m = 0, s = 0;
        h = sec / 3600;
        m = (sec - h * 3600) / 60;
        s = sec % 60;
        return String.format("%02d%s%02d%s%02d",
                h, separator, m, separator, s);

    }

    public static String parceDate(long time, String[] month) {
        int[] date = parseToUserTime(time);
        return String.format("%d %s %d  %02d:%02d",
                date[2], month[date[1]], date[0], date[3], date[4]);

    }
    public static String parceDateplus(long time, String[] month) {
        int[] date = parseToUserTime(time);
        return String.format("%d %s %d  %02d:%02d",
                date[2], month[date[1]+1], date[0], date[3], date[4]);

    }
    public static int[] parseToUserTime(long time) {
        calendar.setTimeInMillis(time);
        Date currentLocalTime = calendar.getTime();
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        String localTime = date.format(currentLocalTime);
        String[] strTime = localTime.split("-");
        int[] dateTime = new int[5];
        for (int i = 0; i < 5; i++) {
            dateTime[i] = Integer.parseInt(strTime[i]);
        }
        dateTime[1]--;
        return dateTime;
    }


    public static int[] rangeTime(long start, long end) {
        int[] mStart, mEnd, mRezult;
        mStart = parseToUserTime(start);
        mEnd = parseToUserTime(end);
        mRezult = new int[]{0, 0, 0, 0, 0};
        int[] delta = {0, 12, 31, 24, 60};
        for (int i = 4; i >= 0; i--) {
            mRezult[i] += mEnd[i] - mStart[i];
            if (mRezult[i] < 0) {
                mRezult[i - 1] -= 1;
                mRezult[i] += delta[i];
            }

        }
        return mRezult;
    }

    public static int[] rangeTime(long start) {
        return rangeTime(start, now());
    }

    public static String rangeToStr(int[] time, int count) {
        String str = "";
        String[] one = {"Г", "Мес", "Дн", "Ч", "Мин"};
        int i = 0;
        while (count > 0) {
            if (time[i] != 0) {
                count--;
                str = str + time[i] + one[i] + "  ";
            }
            i++;
            if (i == 5) count = -1;
        }
        return str.trim();

    }

    public static int countDayFromNow(long time) {
        long dayMs = HMs(24);
        long last = getDay000(time);
        long now = getDay000(now());
        return (int) ((now - last) / dayMs);
    }

    public static long getDay000(long time) {
        time = time + HMs(localTZ());
        return time - (time % HMs(24));
    }

    public static int dayWeekNow() {
        calendar.setTimeInMillis(now());
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int weekDay = 1;
        if (Calendar.MONDAY == day) weekDay = 0;
        else if (Calendar.TUESDAY == day) weekDay = 1;
        else if (Calendar.WEDNESDAY == day) weekDay = 2;
        else if (Calendar.THURSDAY == day) weekDay = 3;
        else if (Calendar.FRIDAY == day) weekDay = 4;
        else if (Calendar.SATURDAY == day) weekDay = 5;
        else if (Calendar.SUNDAY == day) weekDay = 6;

        return weekDay;
    }

    public static int numberOfWeekNow(long time) {
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public static int numberYear(long time) {
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.YEAR);
    }

    public static long nowDayPlusHour(int i) {
        Calendar calendar = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();

        calendar.set(Calendar.YEAR, cal.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
        calendar. set(Calendar.HOUR_OF_DAY, i);
        calendar.set(Calendar.MINUTE, new Random().nextInt(10));
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.before(cal)) calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTimeInMillis();

    }


    public static long HMs(int i) {
        return 3600 * 1000 * i;
    }

    public static Date getDateYMD(int y, int m, int d) {
        Calendar calendar = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.MONTH, m);
        calendar.set(Calendar.DAY_OF_MONTH, d);
        return calendar.getTime();
    }
}
