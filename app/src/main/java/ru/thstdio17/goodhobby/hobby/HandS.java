package ru.thstdio17.goodhobby.hobby;

/**
 * Created by shcherbakov on 24.08.2017.
 */

public class HandS {
   public Hobby hobby;
   public HobbyScheduler shelder;

    public HandS(Hobby hobby, HobbyScheduler shelder) {
        this.hobby = hobby;
        this.shelder = shelder;
    }

    public HandS setStatus(int i) {
        hobby.setStatus(i);
        return this;
    }
}

