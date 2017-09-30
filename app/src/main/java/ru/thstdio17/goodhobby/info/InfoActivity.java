package ru.thstdio17.goodhobby.info;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;

import ru.thstdio17.goodhobby.abstrct.SingleFragmentActivity;

/**
 * Created by shcherbakov on 29.08.2017.
 */

public class InfoActivity extends SingleFragmentActivity {
    private static String EXTRA_WEB_ID="web_id";

    @Override
    protected Fragment createFragment() {
        return InfoFragment.newInstance(getIntent().getIntExtra(EXTRA_WEB_ID,0));
    }

    @Override
    protected void init() {
  fab.setVisibility(View.GONE);
    }

    @Override
    protected void fabOnClic() {

    }
    public static Intent newIntent(Context packageContext, int id) {
        Intent intent = new Intent(packageContext, InfoActivity.class);
        intent.putExtra(EXTRA_WEB_ID, id);
        return intent;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
           onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
