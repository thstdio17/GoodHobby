package ru.thstdio17.goodhobby.addHobby;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;

import ru.thstdio17.goodhobby.R;
import ru.thstdio17.goodhobby.abstrct.SingleFragmentActivity;

/**
 * Created by shcherbakov on 09.08.2017.
 */

public class AddHobbyActivity extends SingleFragmentActivity {
    private static String EXTRA_ID = "hobby_id";
    Calback mCalback;

    interface Calback {
        void fab();
        void back();
    }

    @Override
    protected Fragment createFragment() {
        Fragment fragment = AddHobbyFragment.newInstance(getIntent().getIntExtra(EXTRA_ID, 0));
        fragment.setRetainInstance(true);
        mCalback = (Calback) fragment;
        return fragment;
    }

    @Override
    protected void init() {
        fab.setImageResource(R.drawable.ic_save);
    }

    @Override
    protected void fabOnClic() {
        if (mCalback == null) mCalback = (Calback) fragment;
        mCalback.fab();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (mCalback == null) mCalback = (Calback) fragment;
            mCalback.back();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent newIntent(Context packageContext, int id) {
        Intent intent = new Intent(packageContext, AddHobbyActivity.class);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }
    public void fabVisble(boolean show){
        if(show)fab.setVisibility(View.VISIBLE);
            else fab.setVisibility(View.GONE);
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        if (mCalback == null) mCalback = (Calback) fragment;
        mCalback.back();
    }
}
