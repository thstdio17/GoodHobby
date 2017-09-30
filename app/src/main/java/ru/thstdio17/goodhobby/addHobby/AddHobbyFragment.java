package ru.thstdio17.goodhobby.addHobby;

import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import ru.thstdio17.goodhobby.R;
import ru.thstdio17.goodhobby.hobby.DateLab;
import ru.thstdio17.goodhobby.hobby.Hobby;
import ru.thstdio17.goodhobby.hobby.HobbyScheduler;
import ru.thstdio17.goodhobby.sqlite.SqliteBD;


/**
 * Created by shcherbakov on 09.08.2017.
 */

public class AddHobbyFragment extends Fragment implements View.OnClickListener, AddHobbyActivity.Calback, CompoundButton.OnCheckedChangeListener {
    private static String EXTRA_ID_HOBBY = "id_hobby";
    private String EXTRA_SHEDULER = "shelder";

    Hobby hobby;
    boolean isDialogDay = false;
    boolean isOneDay = true;
    int id = 0, position;
    private int iconId = 0;

    HobbyScheduler shelder;
    int shelderType = 0;
    boolean isDayCheck[] = {true, true, true, true, true, true, true};
    String[] dayWeekBrif;

    SqliteBD bs;
    TypedArray icons;

    EditText title, description;
    TextView[] days = new TextView[7];
    ImageView imageDays[] = new ImageView[7];
    ImageView imageIcon[] = new ImageView[12];
    ImageView icon;
    Spinner typeDay;
    ScrollView scroll;

    SwitchCompat swOld;
    TextView txtDateOld;
    EditText countOld;
    LinearLayout llNonNull;
    Button btnDateOld, btnDateOk;
    boolean isDialogDate = false;
    RelativeLayout scrollData;
    DatePicker dataPicer;
    Date oldDate;

    public static AddHobbyFragment newInstance(int idHobby) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_ID_HOBBY, idHobby);
        AddHobbyFragment fragment = new AddHobbyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            shelder = savedInstanceState.getParcelable(EXTRA_SHEDULER);
        }
        bs = SqliteBD.getInstance(getContext());
        super.onCreate(savedInstanceState);
        int id = getArguments().getInt(EXTRA_ID_HOBBY);
        if (id > 0) {
            hobby = bs.getHobbyForId(id);
            shelder = bs.getShedulerList(id);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.m_add_hobby_fragment, container, false);
        initView(v);
        setListerns();
        loadHobbyToDisplay();
        return v;

    }

    private void loadHobbyToDisplay() {
        if (hobby != null) {
            title.setText(hobby.getName());
            setIconToView(icon, hobby.getIconId());
            description.setText(hobby.getDescription());
        }
        if (shelder != null) {
            shelderType = shelder.getType();
            typeDay.setSelection(shelderType);
            daysStatus(shelderType, false);
            switch (shelderType) {
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
    }

    private void initView(View v) {
        icons = getResources().obtainTypedArray(R.array.icon_res);
        dayWeekBrif = getResources().getStringArray(R.array.day_week_brif);
        icon = (ImageView) v.findViewById(R.id.imageIcon);
        title = (EditText) v.findViewById(R.id.editTextTitle);
          description = (EditText) v.findViewById(R.id.editTextDescription);
          typeDay = (Spinner) v.findViewById(R.id.spinner);

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

        scroll = (ScrollView) v.findViewById(R.id.scrollImage);

        imageIcon[0] = (ImageView) v.findViewById(R.id.imageIcon0);
        imageIcon[1] = (ImageView) v.findViewById(R.id.imageIcon1);
        imageIcon[2] = (ImageView) v.findViewById(R.id.imageIcon2);
        imageIcon[3] = (ImageView) v.findViewById(R.id.imageIcon3);
        imageIcon[4] = (ImageView) v.findViewById(R.id.imageIcon4);
        imageIcon[5] = (ImageView) v.findViewById(R.id.imageIcon5);
        imageIcon[6] = (ImageView) v.findViewById(R.id.imageIcon6);
        imageIcon[7] = (ImageView) v.findViewById(R.id.imageIcon7);
        imageIcon[8] = (ImageView) v.findViewById(R.id.imageIcon8);
        imageIcon[9] = (ImageView) v.findViewById(R.id.imageIcon9);
        imageIcon[10] = (ImageView) v.findViewById(R.id.imageIcon10);
        imageIcon[11] = (ImageView) v.findViewById(R.id.imageIcon11);

        swOld = (SwitchCompat) v.findViewById(R.id.switchOld);
        swOld.setOnCheckedChangeListener(this);
        llNonNull = (LinearLayout) v.findViewById(R.id.linearLayoutNotNull);
        txtDateOld = (TextView) v.findViewById(R.id.textViewData);
        btnDateOld = (Button) v.findViewById(R.id.buttonDateOld);
        btnDateOld.setOnClickListener(this);
        countOld = (EditText) v.findViewById(R.id.editTextCountOld);
        scrollData =  v.findViewById(R.id.scrollDataPicker);
       btnDateOk = (Button) v.findViewById(R.id.buttonDataOk);
       btnDateOk.setOnClickListener(this);
        dataPicer = v.findViewById(R.id.datePicker);
    }

    private void setListerns() {
        icon.setOnClickListener(this);
        for (int i = 0; i < imageIcon.length; i++) {
            setIconToView(imageIcon[i], i);
            imageIcon[i].setOnClickListener(this);
        }
        iconId = 0;
        typeDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                                       long id) {
                // TODO Auto-generated method stub
                if (shelderType != pos) {
                    shelderType = pos;
                    daysStatus(pos, true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        for (int i = 0; i < 7; i++) {
            days[i].setOnClickListener(this);
            imageDays[i].setMinimumWidth(width / 10);
            imageDays[i].setMinimumHeight(width / 10);
        }
    }

    private void daysStatus(int pos, boolean isNew) {
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
                if (isNew) dayCheck(1);
                break;
            case 2:
                for (int i = 0; i < 7; i++) {
                    days[i].setText(String.valueOf(i + 1));
                }
                dayCheckAll(false);
                if (isNew) dayCheck(2);
                break;
            case 3:
                for (int i = 0; i < 7; i++) {
                    days[i].setText(dayWeekBrif[i]);
                }
                dayCheckAll(false);

                break;
        }
    }

    private boolean save() {
        if(swOld.isChecked()) {
            if (countOld.getText().toString().equals("")){
                String str= (String) getText(R.string.add_hobby_error_old_count);
                Toast toast = Toast.makeText(getContext(),
                        str , Toast.LENGTH_SHORT);
                toast.show();
                return false;
            }

        }
        String strDescription = description.getText().toString();
        String strTitle = title.getText().toString();
        if (hobby != null) {
            hobby.setName(strTitle);
            hobby.setDescription(strDescription);
            hobby.setIconId(iconId);
            if (isDialogDay) hobby.setOneDay(isOneDay);
            update();
            return true;
        }
        position = bs.getMaxPosition() + 1;
        hobby = new Hobby(id, position, strTitle).setDescription(strDescription);
        hobby.setOneDay(isOneDay);
        hobby.setIconId(iconId);
        bs.setHobby(hobby);
        hobby = bs.getHobbyForPosition(position);
        createShelder();
        bs.setSheduler(shelder);
        updateLastHobby();
        return true;
    }

    private void updateLastHobby() {
        if(swOld.isChecked()) {
            long delta=DateLab.now()-oldDate.getTime();
            int days= (int) (delta/DateLab.HMs(24));
            int intOldCount=Integer.parseInt(countOld.getText().toString());
            int stepDay=1;
            int avgOldCount=intOldCount/days;
            if(avgOldCount==0) {avgOldCount=1;stepDay=days/intOldCount;}
            hobby.setStart(oldDate.getTime());
            hobby.setStatus(0);
            bs.updateHobby(hobby);
            int tempCount=0;
            for(int i=0;i<days;i+=stepDay){
                bs.setHobbyResult(hobby.getId(),oldDate.getTime()+i*DateLab.HMs(24),avgOldCount);
                tempCount+=avgOldCount;
            }
            //bs.setHobbyResult(hobby.getId(),intOldCount%days);
            bs.setHobbyResult(hobby.getId(),intOldCount-tempCount);
        }
    }


    private void update() {
        bs.updateHobby(hobby);
        bs.deleteShuduler(hobby.getId());
        createShelder();
        bs.setSheduler(shelder);
        updateLastHobby();
    }

    private void createShelder() {
        if (shelder == null) {
            shelder = new HobbyScheduler();
        }
        shelder.setHobbyId(hobby.getId());
        shelder.clearDays();
        int n = shelderType == 3 ? 0 : 1;

        for (int i = 0; i < 7; i++) {
            if (isDayCheck[i]) shelder.addDays(i + n);
        }
        if (shelderType == 3 && shelder.size() == 7) shelderType = 0;
        shelder.setType(shelderType);

    }

    private void clickDay(int id) {
        if (shelderType == 1 || shelderType == 2) dayCheckAll(false);
        else if (shelderType == 0) {
            typeDay.setSelection(3);
            shelderType = 3;
        }
        dayCheck(id);
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

    private void animation(View v, int on) {
        Animation animation;
        switch (on) {
            case 0:
                v.setVisibility(View.VISIBLE);
                animation = AnimationUtils.loadAnimation(getContext(), R.anim.go_out);
                v.setAnimation(animation);
                v.animate();
                ((AddHobbyActivity) getActivity()).fabVisble(false);
                break;
            case 1:
                animation = AnimationUtils.loadAnimation(getContext(), R.anim.go_on);
                v.setAnimation(animation);
                v.animate();
                ((AddHobbyActivity) getActivity()).fabVisble(true);
                v.setVisibility(View.GONE);
        }

    }

    private void setIconToView(ImageView image, int idRes) {
        iconId = idRes;
        image.setImageResource(icons.getResourceId(idRes, 0));
        //  image.setImageResource(iconRes[idRes]);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        switch (id) {
            case R.id.textDays0:
                clickDay(0);
                break;
            case R.id.textDays1:
                clickDay(1);
                break;
            case R.id.textDays2:
                clickDay(2);
                break;
            case R.id.textDays3:
                clickDay(3);
                break;
            case R.id.textDays4:
                clickDay(4);
                break;
            case R.id.textDays5:
                clickDay(5);
                break;
            case R.id.textDays6:
                clickDay(6);
                break;
            case R.id.imageIcon:
                animation(scroll, 0);
                break;
            case R.id.imageIcon0:
                setIconToView(icon, 0);
                animation(scroll, 1);
                break;
            case R.id.imageIcon1:
                setIconToView(icon, 1);
                animation(scroll, 1);
                break;
            case R.id.imageIcon2:
                setIconToView(icon, 2);
                animation(scroll, 1);
                break;
            case R.id.imageIcon3:
                setIconToView(icon, 3);
                animation(scroll, 1);
                break;
            case R.id.imageIcon4:
                setIconToView(icon, 4);
                animation(scroll, 1);
                break;
            case R.id.imageIcon5:
                setIconToView(icon, 5);
                animation(scroll, 1);
                break;
            case R.id.imageIcon6:
                setIconToView(icon, 6);
                animation(scroll, 1);
                break;
            case R.id.imageIcon7:
                setIconToView(icon, 7);
                animation(scroll, 1);
                break;
            case R.id.imageIcon8:
                setIconToView(icon, 8);
                animation(scroll, 1);
                break;
            case R.id.imageIcon9:
                setIconToView(icon, 9);
                animation(scroll, 1);
                break;
            case R.id.imageIcon10:
                setIconToView(icon, 10);
                animation(scroll, 1);
                break;
            case R.id.imageIcon11:
                setIconToView(icon, 11);
                animation(scroll, 1);
                break;
            case R.id.buttonDateOld:
                animation(scrollData, 0);
                break;
            case R.id.buttonDataOk:
                setOldDate();
                break;

        }

    }

    private void setOldDate() {
        int y = dataPicer.getYear();
        int m = dataPicer.getMonth();
        int d = dataPicer.getDayOfMonth();
        oldDate = DateLab.getDateYMD(y, m, d);
        long t1=DateLab.now();
        long t2=oldDate.getTime();
        long delta=t1-t2;
        if(delta>0)animation(scrollData, 1);
        else{
            String str= (String) getText(R.string.add_hobby_error_time);
            Toast toast = Toast.makeText(getContext(),
                   str, Toast.LENGTH_SHORT);
             toast.show();
        }
        txtDateOld.setText(DateLab.parceDateplus(oldDate.getTime(), getResources().getStringArray(R.array.mounth)));
    }

    @Override
    public void fab() {
        if(save()) getActivity().finish();
    }

    @Override
    public void back() {
        if (scroll.getVisibility() == View.VISIBLE) setOldDate();
        else if (scrollData.getVisibility() == View.VISIBLE) animation(scrollData, 1);
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(getResources().getText(R.string.add_hobby_is_save))
                    .setCancelable(true)
                    .setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    save();
                                    getActivity().finish();
                                    dialog.cancel();
                                }
                            }).setNegativeButton(getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            getActivity().finish();
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(EXTRA_SHEDULER, shelder);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        countOld.setFocusableInTouchMode(true);
        llNonNull.setVisibility(b ? View.VISIBLE : View.GONE);
    }
}
