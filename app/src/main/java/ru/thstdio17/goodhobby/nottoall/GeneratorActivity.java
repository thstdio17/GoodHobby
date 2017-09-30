package ru.thstdio17.goodhobby.nottoall;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import ru.thstdio17.goodhobby.R;
import ru.thstdio17.goodhobby.hobby.DateLab;
import ru.thstdio17.goodhobby.hobby.Hobby;
import ru.thstdio17.goodhobby.sqlite.SqliteBD;

public class GeneratorActivity extends AppCompatActivity implements View.OnClickListener {
    EditText idHobby, count;
    TextView list;

    protected SqliteBD bs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l_generator);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        idHobby = (EditText) findViewById(R.id.editTextID);
        count = (EditText) findViewById(R.id.editTextCount);
        list = (TextView) findViewById(R.id.textViewHobby);
        bs = SqliteBD.getInstance(getApplicationContext());
        for (Hobby h : bs.getListHobby()) {
            if (h.getStatus() == 1) {
                list.append(h.getId() + "  " + h.getName() +
                        System.lineSeparator());
            }
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int idWork = Integer.parseInt(idHobby.getText().toString());
        int countWork = Integer.parseInt(count.getText().toString());
        ArrayList<Integer> countArray = new ArrayList<>();
        int tempCount = countWork;
        Random r = new Random();
        int lost = r.nextInt(15);
        int good = r.nextInt(20);
        while (tempCount > 0) {
            if (r.nextInt(100) < 10) {
                countArray.add(-1);
            } else {
                int x = r.nextInt(10 + good);
                x = x > 10 ? 10 : x;
                x = tempCount - x > 0 ? x : tempCount;
                tempCount -= x;
                countArray.add(x);
            }
        }
        list.setText("");
        Hobby hobby = bs.getHobbyForId(idWork);
        int day = countArray.size();
        hobby.setStart(DateLab.now() - DateLab.HMs(24) * day);
        list.append("Start " + DateLab.parceDate((DateLab.now() - DateLab.HMs(24 * day)), getResources().getStringArray(R.array.mounth)) + System.lineSeparator());
        list.append("Days:"+day);
        hobby.setStatus(0);
        bs.updateHobby(hobby);
         for (int i : countArray) {
            long delta = DateLab.HMs(24) * day;
            long time = DateLab.now() - delta;
            if (i >= 0) {

                bs.setHobbyResult(idWork, time, i);
            }
            day--;
        }
        list.append("Done");
    }
}
