package ru.thstdio17.goodhobby.abstrct;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Spinner;

import ru.thstdio17.goodhobby.R;


/**
 * Created by shcherbakov on 26.04.2017.
 */


public abstract class SinglePageFragmentActivity extends AppCompatActivity implements View.OnClickListener {

    protected FloatingActionButton fab;
    protected ViewPager mViewPager;
    protected TabLayout tabLayout;
    protected Spinner toolSpinner;
    protected Toolbar toolbar;
    private int numberPage;

    protected FragmentManager fragmentManager;
    FragmentStatePagerAdapter pageAdapter;

    protected abstract int getNumberPage();

    protected abstract Fragment setFragment(int position);

    public abstract void init();

    protected abstract CharSequence getMyTitle(int position);// определяет имена секций

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.abstract_page_app_bar_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        mViewPager = (ViewPager) findViewById(R.id.activity_pager_view_pager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        init();
        numberPage = getNumberPage();
        fragmentManager = getSupportFragmentManager();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabSelect(mViewPager.getCurrentItem());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabLayout.setupWithViewPager(mViewPager);
        tabIsVisible();

    }

    protected abstract int getCurrentItem();

    protected void setAdapter() {
        fragmentManager = getSupportFragmentManager();

        pageAdapter = new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int namberOfDay) {

                return setFragment(namberOfDay);
            }

            @Override
            public int getCount() {
                return numberPage;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getMyTitle(position);
            }
        };
        mViewPager.setAdapter(pageAdapter);
        mViewPager.setCurrentItem(getCurrentItem());
    }

    protected void tabIsVisible() {
    }

    protected abstract void tabSelect(int idTab);

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) fabOnClic();
    }

    protected abstract void fabOnClic();

    @Override
    public void onResume() {
        super.onResume();
        setAdapter();

    }

}