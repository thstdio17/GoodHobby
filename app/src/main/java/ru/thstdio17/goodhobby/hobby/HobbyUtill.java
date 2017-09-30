package ru.thstdio17.goodhobby.hobby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by shcherbakov on 09.08.2017.
 */

public class HobbyUtill {
    /**
     * @param list List Hobby
     * @param from id hoobby  которое перемещается
     * @param to   id hoobby  куда перемещается
     * @return List Hobby
     */
    public static List<HandS> changeItemes(List<HandS> list, int from, int to) {
        if (list.get(from).hobby.status > list.get(to).hobby.status) {
            list.get(from).hobby.setPosition(-1);
            return sortList(list);
        } else if (list.get(from).hobby.status < list.get(to).hobby.status) {
            list.get(from).hobby.setPosition(100);
            return sortList(list);
        }

        List<HandS> temp = new ArrayList<>(list);
        list.set(to, temp.get(from));
        for (int i = to + 1; i <= from; i++) {
            list.set(i, temp.get(i - 1));

        }
        for (int i = from; i < to; i++) {
            list.set(i, temp.get(i + 1));
        }
        return sortList(reformatPosition(list));
    }

    public static List<HandS> sortList(List<HandS> list) {
        Collections.sort(list, new Comparator<HandS>() {
            @Override
            public int compare(HandS h1, HandS h2) {
                int status1 = h1.hobby.getStatus();
                int status2 = h2.hobby.getStatus();
                if (status1 > status2) {
                    return 1;
                } else if (status2 > status1) {
                    return -1;
                } else {
                    if(h1 instanceof ItemSeparator) return -1;
                    if(h2 instanceof ItemSeparator) return 1;
                    int position1 = h1.hobby.getPosition();
                    int position2 = h2.hobby.getPosition();
                    if (position1 > position2) return 1;
                    else return -1;
                }

            }
        });
        list = reformatPosition(list);
        return list;
    }

    private static List<HandS> reformatPosition(List<HandS> list) {
        int position = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof ItemSeparator) position = 0;
            list.get(i).hobby.setPosition(position++);
        }
        return list;
    }

    public static List<HandS> zaglushka(List<HandS> list) {

        int[] temp = {-1, -1, -1};
        for (int i = 0; i < list.size(); i++) {
            temp[list.get(i).hobby.status] = i;
                    }
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i) instanceof ItemSeparator)  temp[list.get(i).hobby.status] = -1;
        }
        int iPlus = 1;
        if (temp[1] != -1) {
            list.add(temp[0] + iPlus++, new ItemSeparator(0, -1000000, "Планируемые").setStatus(1));
        }
        if (temp[2] != -1) {
            list.add(Math.max(temp[0], temp[1]) + iPlus++, new ItemSeparator(0, -1000000, "Завершенные").setStatus(2));
        }
        return list;
    }
}
