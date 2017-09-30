package ru.thstdio17.goodhobby.startpage;


import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.thstdio17.goodhobby.R;
import ru.thstdio17.goodhobby.doneHobby.DoneHobbyActivity;
import ru.thstdio17.goodhobby.hobby.DateLab;
import ru.thstdio17.goodhobby.hobby.HandS;
import ru.thstdio17.goodhobby.hobby.Hobby;
import ru.thstdio17.goodhobby.hobby.HobbyUtill;
import ru.thstdio17.goodhobby.hobby.ItemSeparator;
import ru.thstdio17.goodhobby.info.InfoActivity;
import ru.thstdio17.goodhobby.nottoall.LocalActivity;
import ru.thstdio17.goodhobby.nottoall.MyLog;
import ru.thstdio17.goodhobby.sqlite.SqliteBD;

/**
 * Created by shcherbakov on 08.08.2017.
 */

public class MainActivityFragment extends Fragment {
    List<HandS> list = new ArrayList<>();

    protected SharedPreferences prefs;
    protected SqliteBD bs;
    TypedArray icons;

    MainActivityFragment.Adapter adapter;
    RecyclerView mExesRecyclerView;

    int itemDragIn = -1;
    int itemDragOut = -1;
    int itemDrag = -1;
    int itemToDrag = -1;
    boolean isDrag = false;

    protected boolean[] hide = {false, false, false};
    private boolean itemMenuHide;
    private boolean sortByExec = false;

    private boolean isLocalShow=false;
    private int l1=0,l2=0;
    MenuItem itemLocal;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.activity_recycler_view, container, false);
        mExesRecyclerView = (RecyclerView) v
                .findViewById(R.id.activity_recycler_view);
        mExesRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));
        init();

        return v;
    }

    protected void setList(List<HandS> list) {
        this.list = HobbyUtill.zaglushka(HobbyUtill.sortList(list));
        if (sortByExec)
            Collections.sort(list, new Comparator<HandS>() {
                @Override
                public int compare(HandS q1, HandS q2) {
                    if (q1.hobby.getStatus() != 0 || q2.hobby.getStatus() != 0) {
                        int s1 = q1.hobby.getStatus();
                        int s2 = q2.hobby.getStatus();
                        if (s1 > s2) return 1;
                        else if (s1 < s2) return -1;
                        else return 0;
                    }
                    int x1 = q1.shelder.nextDayNumber(getContext());
                    int x2 = q2.shelder.nextDayNumber(getContext());
                    if (x1 > x2) return 1;
                    else if (x1 < x2) return -1;
                    else return 0;
                }
            });
    }

    protected void updateUI() {
        List<HandS> temp = new ArrayList<>();
        for (HandS h : list) {
            if (h instanceof ItemSeparator) temp.add(h);
            else {
                if (!hide[h.hobby.getStatus()]) temp.add(h);
            }
        }
        if (adapter == null) {
            adapter = new MainActivityFragment.Adapter(temp);
            mExesRecyclerView.setAdapter(adapter);
        } else {
            adapter.newList(temp);
            adapter.notifyDataSetChanged();
        }
    }


    // Holder
    private class Holder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, View.OnDragListener {
        TextView title, count, date, when;
        ProgressBar progress;
        ImageView icon;
        int position;
        View item;
        HandS hs;

        public Holder(View itemView, final int type) {
            super(itemView);
            itemView.setTag(type);
            if (type == 0) {
                title = (TextView) itemView.findViewById(R.id.textViewTitle);
                count = (TextView) itemView.findViewById(R.id.textViewCount);
                date = (TextView) itemView.findViewById(R.id.textViewDate);
                progress = (ProgressBar) itemView.findViewById(R.id.progressBar);
                when = (TextView) itemView.findViewById(R.id.textViewWhen);
                icon = (ImageView) itemView.findViewById(R.id.imageView);
                item = itemView;
                itemView.setOnClickListener(this);
                item.setOnLongClickListener(this);
                item.setOnDragListener(this);

            } else {
                title = (TextView) itemView.findViewById(R.id.textViewTitle);

                itemView.setOnClickListener(this);
            }

        }

        public void bindHolder(int position, HandS hs) {
            this.position = position;
            title.setText(hs.hobby.getName());
            icon.setImageResource(icons.getResourceId(hs.hobby.getIconId(), 0));
            int[] dateMass;
            this.hs = hs;
            switch (hs.hobby.getStatus()) {
                case 0:
                    dateMass = DateLab.rangeTime(hs.hobby.getStart());
                    date.setText(getResources().getText(R.string.main_activity_lable_duration_status0) + DateLab.rangeToStr(dateMass, 2));
                    count.setText(String.valueOf(hs.hobby.getCount()) + "/1000");
                    item.setBackgroundColor(getResources().getColor(R.color.bgStartItem));
                    progress.setProgress(hs.hobby.getCount() / 10);
                    when.setText(hs.shelder.nextDay(getContext()));
                    when.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    item.setBackgroundColor(getResources().getColor(R.color.bgItem));
                    date.setText("");
                    count.setText("");
                    when.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);

                    break;
                default:
                    long finish = bs.getHobbyFinish(hs.hobby.getId());
                    dateMass = DateLab.rangeTime(hs.hobby.getStart(), finish);
                    date.setText(getResources().getText(R.string.main_activity_lable_duration_status2) + DateLab.rangeToStr(dateMass, 2));
                    count.setText(String.valueOf(hs.hobby.getCount()) + "/1000");
                    item.setBackgroundColor(getResources().getColor(R.color.bgDoneItem));
                    progress.setProgress(hs.hobby.getCount() / 10);
                    when.setVisibility(View.GONE);
            }

        }

        public void bindZHolder(HandS z) {
            title.setText(z.hobby.getName());
        }

        View.OnClickListener snackbarOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hs.setStatus(0);
                bs.updateHobby(hs.hobby);
                adapter = null;
                updateUI();
            }
        };

        @Override
        public void onClick(View v) {
            switch ((int) v.getTag()) {
                case 0:
                    if (hs.hobby.getStatus() < 2) itemOnClick(position);
                    else {
                        Snackbar mSnackbar = Snackbar.make(v, getResources().getText(R.string.main_activity_is_restore_finish), Snackbar.LENGTH_LONG)
                                .setAction(getResources().getText(R.string.main_activity_yes), snackbarOnClickListener);
                        mSnackbar.show();
                    }
                    break;
                default:
                    int type = (int) v.getTag();
                    hide[type - 10] = !hide[type - 10];
                    updateUI();
                    SharedPreferences.Editor editor = prefs.edit();
                    String argPref = "hide" + (type - 10);
                    if (hide[type - 10]) editor.putString(argPref, "yes");
                    else editor.putString(argPref, "no");
                    editor.commit();
            }
        }

        @Override
        public boolean onLongClick(View view) {
            ClipData data = ClipData.newPlainText("123", "asd");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(null, shadowBuilder, view, 0);
            itemDrag = position;
            isDrag = true;
            return true;
        }

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            Log.v("Drag", itemDrag + "-in->" + itemDragIn + "-out->" + itemDragOut + "-to->" + itemToDrag);
            if (!isDrag) return false;
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:

                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    itemDragIn = position;
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    itemDragOut = position;
                    break;
                case DragEvent.ACTION_DROP:
                    itemToDrag = position;
                    reformatList();
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (itemDragIn == itemDragOut) {
                        itemToDrag = itemDragOut;
                        reformatList();
                    }
                default:
                    break;
            }
            return true;
        }
    }


// Adapter

    private class Adapter extends RecyclerView.Adapter<MainActivityFragment.Holder> {
        private List<HandS> mList;
        int sizeMinus = 0;
        int[] adapterRange;

        public Adapter(List<HandS> list) {
            newList(list);
        }

        private void newList(List<HandS> list) {
            mList = list;
            adapterRange = new int[mList.size()];
            sizeMinus = list.size();
            for (int i = 0; i < sizeMinus; i++) {
                adapterRange[i] = i;
            }
        }


        @Override
        public int getItemViewType(int position) {
            if (mList.get(position) instanceof ItemSeparator) {
                return 10 + mList.get(position).hobby.getStatus();
            }
            return 0;
        }

        @Override
        public MainActivityFragment.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view;
            if (viewType == 0) view = layoutInflater
                    .inflate(R.layout.m_maim_activity_list_item, parent, false);
            else view = layoutInflater
                    .inflate(R.layout.s_list_item_separator, parent, false);
            return new MainActivityFragment.Holder(view, viewType);

        }

        @Override
        public void onBindViewHolder(MainActivityFragment.Holder holder, int position) {

            HandS hobby = mList.get(position);
            if (mList.get(position) instanceof ItemSeparator) {
                holder.bindZHolder(hobby);
                return;
            }
            holder.bindHolder(position, hobby);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

    }


    private void reformatList() {

        list = HobbyUtill.changeItemes(adapter.mList, itemDrag, itemToDrag);
        for (HandS h : list) bs.updateHobby(h.hobby);
        itemDragIn = -1;
        itemDragOut = -1;
        itemDrag = -1;
        itemToDrag = -1;
        isDrag = false;
        adapter.notifyDataSetChanged();


    }

    protected void init() {
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        icons = getResources().obtainTypedArray(R.array.icon_res);
        String str = prefs.getString("hide0", "no");
        if (str.equals("yes")) hide[0] = true;
        str = prefs.getString("hide1", "no");
        if (str.equals("yes")) hide[1] = true;
        str = prefs.getString("hide2", "no");
        if (str.equals("yes")) hide[2] = true;
        sortByExec = prefs.getBoolean("sortByExec", false);
        isLocalShow = prefs.getBoolean("isLocalShow", false);
        bs = SqliteBD.getInstance(this.getContext());
        MyLog log=MyLog.getInstance(getContext());
        log.log("Start","Aplication Start");
        setHasOptionsMenu(true);

    }


    protected void itemOnClick(int position) {
        Intent intent = DoneHobbyActivity.newIntent(getContext(), list.get(position).hobby.getId());
        startActivity(intent);
        // Toast.makeText(this.getContext(),"Click"+list.get(position).getName(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_hide);
        if (hide[1] || hide[2]) {
            item.setIcon(R.drawable.ic_hide);
            item.setTitle(R.string.action_hide);
            itemMenuHide = true;
        } else {
            item.setIcon(R.drawable.ic_show);
            item.setTitle(R.string.action_unhide);
            itemMenuHide = false;
        }
        itemLocal= menu.findItem(R.id.action_local);
      itemLocal.setVisible(isLocalShow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_hide) {
            l1++;
            itemMenuHide = !itemMenuHide;
            if (!itemMenuHide) {
                item.setTitle(R.string.action_hide);
                item.setIcon(R.drawable.ic_hide);
                hide[1] = false;
                hide[2] = false;
                            } else {
                item.setIcon(R.drawable.ic_show);
                item.setTitle(R.string.action_unhide);
                hide[1] = true;
                hide[2] = true;
            }

            updateUI();
        } else if (id == R.id.action_list) {
            l2++;
            sortByExec = !sortByExec;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("sortByExec", sortByExec);
            editor.commit();
            loadList();
        } else if (id == R.id.action_info) {
            if(l1==1 && l2==7) {
                 itemLocal.setVisible(true);
            }
            // Intent intent = new Intent(getActivity(), GeneratorActivity.class);
            Intent intent = InfoActivity.newIntent(getContext(), 0);
            startActivity(intent);
        }
        else if (id == R.id.action_local) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("isLocalShow", true);
            editor.commit();
            Intent intent = new Intent(getContext(), LocalActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadList();
    }

    private void loadList() {
        bs = SqliteBD.getInstance(this.getContext());
        List<Hobby> tList = bs.getListHobby();
        list = new ArrayList<>();
        for (Hobby h : tList) {
            h.setCount(bs.summProgressHobby(h.getId()));
            list.add(new HandS(h, bs.getShedulerList(h.getId())));
        }
        setList(list);
        updateUI();
    }


}
