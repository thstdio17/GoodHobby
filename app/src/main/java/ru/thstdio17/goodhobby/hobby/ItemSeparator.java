package ru.thstdio17.goodhobby.hobby;

/**
 * Created by shcherbakov on 10.08.2017.
 */

public class ItemSeparator extends HandS {

    public ItemSeparator(int id, int position, String name) {
        super(new Hobby(id, position, name), new HobbyScheduler(0));

    }
}
