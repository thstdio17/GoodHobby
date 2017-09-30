package ru.thstdio17.goodhobby.nottoall;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ru.thstdio17.goodhobby.R;
import ru.thstdio17.goodhobby.abstrct.SingleFragmentActivity;

public class LogActivity extends SingleFragmentActivity {


    @Override
    protected Fragment createFragment() {
        return new LogFragment();
    }

    @Override
    protected void init() {

    }

    @Override
    protected void fabOnClic() {

    }
}
