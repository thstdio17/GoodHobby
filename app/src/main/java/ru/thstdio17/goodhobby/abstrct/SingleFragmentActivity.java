package ru.thstdio17.goodhobby.abstrct;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ru.thstdio17.goodhobby.R;


/**
 * Created by shcherbakov on 19.06.2017.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity
        implements View.OnClickListener {
    protected abstract Fragment createFragment();

    protected abstract void init();

    protected FloatingActionButton fab;
    private FragmentManager fm;
    protected Fragment fragment;

    // Добавляем фрагмент в стек с тегом
    public void addFragment(Fragment fragment, String tagFragment) {
        fm.beginTransaction()
                .add(R.id.fragmentContainer, fragment, tagFragment)
                .commit();
    }

    // Удаляем фрагмент из стека по тегу
    public void detachFragment(String tagFragment) {

        Fragment fragment = fm.findFragmentByTag(tagFragment);
        fm.beginTransaction().detach(fragment).commit();
    }

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.abstract_list_app_bar_main;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = createFragment();
            addFragment(fragment, "main");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        upHome(true);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        init();

    }

    public void upHome(boolean enable) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) fabOnClic();
    }

    protected abstract void fabOnClic();

}
