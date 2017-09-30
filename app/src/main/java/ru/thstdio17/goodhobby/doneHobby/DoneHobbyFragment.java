package ru.thstdio17.goodhobby.doneHobby;

/**
 * Created by shcherbakov on 14.08.2017.
 */

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

import ru.thstdio17.goodhobby.R;
import ru.thstdio17.goodhobby.hobby.Hobby;
import ru.thstdio17.goodhobby.info.InfoActivity;
import ru.thstdio17.goodhobby.sqlite.SqliteBD;


/**
 * Created by shcherbakov on 11.08.2017.
 */

public class DoneHobbyFragment extends Fragment implements View.OnClickListener, DoneHobbyActivity.ToUdate {
    private static final String ID = "idHobby";
    private static String DONE = "done";
    TextView title, progress[] = new TextView[11], icon[] = new TextView[11];
    ImageView halfFon;
    String[] array;
    SqliteBD bs;
    int idHobby;
    Hobby hobby;
    boolean isDone = false;
    private int progressCount;
    private int MAX_COUNT = 1000;
    int color[] = new int[11];
    ValueAnimator anim[] = new ValueAnimator[11];

    public static DoneHobbyFragment newInstance(int id, boolean isDone) {
        Bundle args = new Bundle();
        args.putSerializable(ID, id);
        args.putSerializable(DONE, isDone);
        DoneHobbyFragment fragment = new DoneHobbyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bs = SqliteBD.getInstance(getContext());
        idHobby = getArguments().getInt(ID);
        hobby = bs.getHobbyForId(idHobby);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.m_done_hobby_done_fragment, container, false);
        title = (TextView) v.findViewById(R.id.textViewTitle);
        progress[0] = (TextView) v.findViewById(R.id.textView0);
        progress[1] = (TextView) v.findViewById(R.id.textView1);
        progress[2] = (TextView) v.findViewById(R.id.textView2);
        progress[3] = (TextView) v.findViewById(R.id.textView3);
        progress[4] = (TextView) v.findViewById(R.id.textView4);
        progress[5] = (TextView) v.findViewById(R.id.textView5);
        progress[6] = (TextView) v.findViewById(R.id.textView6);
        progress[7] = (TextView) v.findViewById(R.id.textView7);
        progress[8] = (TextView) v.findViewById(R.id.textView8);
        progress[9] = (TextView) v.findViewById(R.id.textView9);
        progress[10] = (TextView) v.findViewById(R.id.textView10);

        icon[0] = (TextView) v.findViewById(R.id.textIcon0);
        icon[1] = (TextView) v.findViewById(R.id.textIcon1);
        icon[2] = (TextView) v.findViewById(R.id.textIcon2);
        icon[3] = (TextView) v.findViewById(R.id.textIcon3);
        icon[4] = (TextView) v.findViewById(R.id.textIcon4);
        icon[5] = (TextView) v.findViewById(R.id.textIcon5);
        icon[6] = (TextView) v.findViewById(R.id.textIcon6);
        icon[7] = (TextView) v.findViewById(R.id.textIcon7);
        icon[8] = (TextView) v.findViewById(R.id.textIcon8);
        icon[9] = (TextView) v.findViewById(R.id.textIcon9);
        icon[10] = (TextView) v.findViewById(R.id.textIcon10);
        array = getResources().getStringArray(R.array.progress_item);
        isDone = getArguments().getBoolean(DONE);
        setCollor(isDone);
        for (int i = 0; i <= 10; i++) {

            icon[i].setBackgroundColor(color[i]);
            progress[i].setText(array[i]);
            if (i < 10) icon[i].setText("   " + i + "     ");
            else icon[i].setText("   " + i + "   ");
            icon[i].setTextColor(Color.WHITE);
            progress[i].setOnClickListener(this);
            icon[i].setOnClickListener(this);

        }
        title.setText(hobby.getName());
        return v;
    }

    private void setCollor(boolean isDone) {
        if (isDone) {
            for (int i = 0; i < 11; i++) {
                color[i] = Color.argb(255, 255 * (11 - i) / 11, 255 * (11 - i) / 11, 255 * (11 - i) / 11);
            }
        } else {
            for (int i = 0; i < 5; i++) {
                color[i] = Color.argb(255, 255, 255 - 255 * (5 - i) / 5, 0);
                color[i + 5] = Color.argb(255, 255 * (5 - i) / 5, 255, 0);

            }
            color[10] = Color.argb(255, 0, 255, 50);
        }
    }

    @Override
    public void onClick(View view) {
        if (isDone) {
            return;
        }
        int id = view.getId();
        for (int i = 0; i < 11; i++) {
            if (progress[i].getId() == id || icon[i].getId() == id) {
                progressCount = i;
                if (hobby.getStatus() != 0 && i == 0) showAlerNotStart();
                else showAlertSave(i);

            }
        }
    }

    private void showAlertSave(int i) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.m_done_hobby_done_fragment_dialog_result, null);
        TextView mess = (TextView) v.findViewById(R.id.mess);
        mess.setText(array[i]);
        TextView progressTXT = (TextView) v.findViewById(R.id.textViewProgress);
        progressTXT.setText(getResources().getText(R.string.text_progress_result_dialog) + String.valueOf(i));
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setView(v)
                .setCancelable(true)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (hobby.getStart() == 0) bs.startHobby(hobby);
                                hobby.setCount(bs.summProgressHobby(idHobby));
                                bs.setHobbyResult(idHobby, progressCount);
                                if (hobby.getCount() < MAX_COUNT && (hobby.getCount() + progressCount) >= MAX_COUNT)
                                    animation(1000);
                                else animation(0);
                                DoneHobbyActivity actyviti = (DoneHobbyActivity) getActivity();
                                isDone = true;
                                long durVib = 200;
                                Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                                vibrator.vibrate(durVib);
                                actyviti.reload();
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
    }

    private void animation(int idAnim) {
        int[] temp = Arrays.copyOf(color, color.length);
        setCollor(true);
        for (int i = 0; i < 11; i++) {
            anim[i] = ValueAnimator.ofInt(temp[i], color[i]);
            anim[i].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    for (int j = 0; j < 11; j++) {
                        if (animation.equals(anim[j])) {
                            icon[j].setBackgroundColor((Integer) animation.getAnimatedValue());
                        }
                    }
                }
            });
        }
        for (int i = 0; i < 11; i++) {
            anim[i].setStartDelay(i * 20);
            anim[i].setDuration(1);
            anim[i].start();
        }
    }

    private void showAlerNotStart() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage(getResources().getText(R.string.done_hobby_fragment_result_0))
                .setCancelable(true)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_done_done, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_info) {
            Intent intent = InfoActivity.newIntent(getContext(), 1);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onResume() {
        super.onResume();
        bs = SqliteBD.getInstance(getContext());
        hobby = bs.getHobbyForId(idHobby);
        title.setText(hobby.getName());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void update() {
        hobby = bs.getHobbyForId(idHobby);
        title.setText(hobby.getName());
    }
}
