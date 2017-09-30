package ru.thstdio17.goodhobby.doneHobby;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ru.thstdio17.goodhobby.R;
import ru.thstdio17.goodhobby.addHobby.AddHobbyActivity;
import ru.thstdio17.goodhobby.hobby.DateLab;
import ru.thstdio17.goodhobby.hobby.Hobby;
import ru.thstdio17.goodhobby.hobby.HobbyScheduler;
import ru.thstdio17.goodhobby.sqlite.SqliteBD;

/**
 * Created by shcherbakov on 27.08.2017.
 */

public class DoneHobbyInfoFragment extends Fragment implements View.OnClickListener, DoneHobbyActivity.ToUdate {
    private static String EXTRA_ID_HOBBY = "id_hobby";

    SqliteBD bs;
    TypedArray icons;

    Hobby hobby;
    HobbyScheduler shelder;

    TextView title, description;
    TextView[] days = new TextView[7];
    ImageView imageDays[] = new ImageView[7];
    ImageView imageIcon[] = new ImageView[12];
    ImageView icon;
    TextView typeDay;

    TextView start, continion, progressRezult, shedulerTxt, nextTxt, prediction;
    Button edit, finish;
    boolean isDayCheck[] = {true, true, true, true, true, true, true};
    String[] dayWeekBrif;

    public static DoneHobbyInfoFragment newInstance(int idHobby) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_ID_HOBBY, idHobby);
        DoneHobbyInfoFragment fragment = new DoneHobbyInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        bs = SqliteBD.getInstance(getContext());
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.m_done_hobby_info_fragment, container, false);
        initView(v);
        return v;

    }

    private void initView(View v) {
        icons = getResources().obtainTypedArray(R.array.icon_res);
        dayWeekBrif = getResources().getStringArray(R.array.day_week_brif);

        title = (TextView) v.findViewById(R.id.editTextTitle);
        description = (TextView) v.findViewById(R.id.editTextDescription);
        icon = (ImageView) v.findViewById(R.id.imageIcon);

        typeDay = (TextView) v.findViewById(R.id.spinner);

        days[0] = (TextView) v.findViewById(R.id.textDays0);
        days[1] = (TextView) v.findViewById(R.id.textDays1);
        days[2] = (TextView) v.findViewById(R.id.textDays2);
        days[3] = (TextView) v.findViewById(R.id.textDays3);
        days[4] = (TextView) v.findViewById(R.id.textDays4);
        days[5] = (TextView) v.findViewById(R.id.textDays5);
        days[6] = (TextView) v.findViewById(R.id.textDays6);


        imageDays[0] = (ImageView) v.findViewById(R.id.imageDays0);
        imageDays[1] = (ImageView) v.findViewById(R.id.imageDays1);
        imageDays[2] = (ImageView) v.findViewById(R.id.imageDays2);
        imageDays[3] = (ImageView) v.findViewById(R.id.imageDays3);
        imageDays[4] = (ImageView) v.findViewById(R.id.imageDays4);
        imageDays[5] = (ImageView) v.findViewById(R.id.imageDays5);
        imageDays[6] = (ImageView) v.findViewById(R.id.imageDays6);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        for (int i = 0; i < 7; i++) {

            imageDays[i].setMinimumWidth(width / 10);
            imageDays[i].setMinimumHeight(width / 10);
        }

        start = (TextView) v.findViewById(R.id.textViewStart);
        continion = (TextView) v.findViewById(R.id.textViewContinion);
        progressRezult = (TextView) v.findViewById(R.id.textViewProgresRezult);
        prediction = (TextView) v.findViewById(R.id.textViewPredict);
        nextTxt = (TextView) v.findViewById(R.id.textViewNext);
        edit = (Button) v.findViewById(R.id.buttonEdit);
        finish = (Button) v.findViewById(R.id.buttonFinish);
        edit.setOnClickListener(this);
        finish.setOnClickListener(this);
    }

    private void loadHobbyToDisplay() {
        int id = getArguments().getInt(EXTRA_ID_HOBBY);
        hobby = bs.getHobbyForId(id);
        hobby.setCount(bs.summProgressHobby(id));
        shelder = bs.getShedulerList(id);
        title.setText(hobby.getName());
        icon.setImageResource(icons.getResourceId(hobby.getIconId(), 0));
        if (hobby.getDescription().equals("")) description.append("");
        else description.append(hobby.getDescription());
        if (hobby.getStatus() == 0) {
            long dayMs = 1000 * 3600 * 24;
            long time = (DateLab.now() - hobby.getStart()) / dayMs + 1;
            time = time * (1000 - hobby.getCount()) * dayMs / hobby.getCount();
            if (time <= 0) {
                prediction.setText(getResources().getText(R.string.done));
            } else {
                time += DateLab.now();
                prediction.setText(DateLab.rangeToStr(DateLab.rangeTime(DateLab.now(), time), 5));
            }
            String strNextTxt = shelder.nextDay(this.getContext());
            nextTxt.setText(strNextTxt);
            start.setText(DateLab.parceDate(hobby.getStart(), getResources().getStringArray(R.array.mounth)));
            continion.setText(DateLab.rangeToStr(DateLab.rangeTime(hobby.getStart()), 2));
            progressRezult.setText(hobby.getCount() + "/1000");
        } else {
            nextTxt.setText(getResources().getText(R.string.start_today));
        }
        daysStatus(shelder.getType());
        switch (shelder.getType()) {
            case 0:
                dayCheckAll(true);
                break;
            case 1:
            case 2:
                dayCheck(shelder.getDays(0) - 1);
                break;
            case 3:
                for (int i = 0; i < shelder.size(); i++) dayCheck(shelder.getDays(i));
        }


    }

    private void dayCheck(int index) {
        if (isDayCheck[index]) {
            imageDays[index].setVisibility(View.INVISIBLE);
            days[index].setTextColor(Color.BLACK);
        } else {
            imageDays[index].setVisibility(View.VISIBLE);
            days[index].setTextColor(Color.WHITE);
        }
        isDayCheck[index] = !isDayCheck[index];
    }

    private void dayCheckAll(boolean on) {
        for (int i = 0; i < 7; i++) {
            isDayCheck[i] = !on;
            dayCheck(i);
        }
    }

    private void daysStatus(int pos) {
        String[] shelderTypeStr = getResources().getStringArray(R.array.shelder_day);
        String str = shelderTypeStr[pos];
        if (shelder.getType() == 2){
            int dayLast=shelder.getDays(0) - shelder.getDone2();
            dayLast=dayLast<0?0:dayLast;
            str = str + String.format(getContext().getString(R.string.remained), dayLast);}
        typeDay.setText(str);
        imageDays[0].setVisibility(View.VISIBLE);
        days[0].setVisibility(View.VISIBLE);

        switch (pos) {
            case 0:
                for (int i = 0; i < 7; i++) {
                    days[i].setText(dayWeekBrif[i]);
                }
                dayCheckAll(true);
                break;
            case 1:
                imageDays[0].setVisibility(View.GONE);
                days[0].setVisibility(View.GONE);
                for (int i = 0; i < 7; i++) {
                    days[i].setText(String.valueOf(i + 1));
                }
                dayCheckAll(false);
                break;
            case 2:
                for (int i = 0; i < 7; i++) {
                    days[i].setText(String.valueOf(i + 1));
                }
                dayCheckAll(false);
                break;
            case 3:
                for (int i = 0; i < 7; i++) {
                    days[i].setText(dayWeekBrif[i]);
                }
                dayCheckAll(false);

                break;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.buttonEdit:
                startActivity(AddHobbyActivity.newIntent(this.getContext(), hobby.getId()));
                break;
            case R.id.buttonFinish:
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setMessage(getResources().getText(R.string.confirm_finish))
                        .setCancelable(true)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        bs.finishHobby(hobby);
                                        getActivity().finish();
                                        dialog.cancel();
                                    }
                                })
                        .setNegativeButton(getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
//           case R.id.buttonDelete:
//            bs.deleteHobbyFull(hobby.getId());
//            getActivity().finish();
//               break;
        }

    }

    @Override
    public void update() {
        loadHobbyToDisplay();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_done_info, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_graph) {
            Intent intent = GraphActivity.newIntent(getContext(), hobby.getId());
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadHobbyToDisplay();
    }
}
