package ru.thstdio17.goodhobby.startpage;

import android.content.Intent;
import android.support.v4.app.Fragment;

import ru.thstdio17.goodhobby.abstrct.SingleFragmentActivity;
import ru.thstdio17.goodhobby.addHobby.AddHobbyActivity;
import ru.thstdio17.goodhobby.servise.StartService;

public class MainActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new MainActivityFragment();
    }

    @Override
    protected void init() {
        this.startService(new Intent(this, StartService.class));
        upHome(false);

    }

    @Override
    protected void fabOnClic() {

        Intent intent = new Intent(this, AddHobbyActivity.class);
        startActivity(intent);
    }
}
