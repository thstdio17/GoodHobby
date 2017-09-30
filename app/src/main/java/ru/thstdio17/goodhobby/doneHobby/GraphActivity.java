package ru.thstdio17.goodhobby.doneHobby;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import ru.thstdio17.goodhobby.abstrct.SingleFragmentActivity;

/**
 * Created by shcherbakov on 29.08.2017.
 */

public class GraphActivity extends SingleFragmentActivity {
    private static String EXTRA_ID="id";

    @Override
    protected Fragment createFragment() {
        return GraphHobbyFragment.newInstance(getIntent().getIntExtra(EXTRA_ID,0));
    }

    @Override
    protected void init() {
  fab.setVisibility(View.GONE);
    }

    @Override
    protected void fabOnClic() {

    }
    public static Intent newIntent(Context packageContext, int id) {
        Intent intent = new Intent(packageContext, GraphActivity.class);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }
}
