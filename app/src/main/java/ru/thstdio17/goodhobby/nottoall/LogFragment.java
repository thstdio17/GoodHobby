package ru.thstdio17.goodhobby.nottoall;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.thstdio17.goodhobby.R;
import ru.thstdio17.goodhobby.hobby.DateLab;
import ru.thstdio17.goodhobby.sqlite.SqliteBD;

/**
 * Created by shcherbakov on 06.09.2017.
 */

public class LogFragment extends Fragment {
    List<LogsUnit> list = new ArrayList<>();
    protected SqliteBD bs;
    Adapter adapter;
    RecyclerView mExesRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.activity_recycler_view, container, false);
        mExesRecyclerView = (RecyclerView) v
                .findViewById(R.id.activity_recycler_view);
        mExesRecyclerView.setLayoutManager(new LinearLayoutManager
                (getActivity()));

        return v;
    }

    protected void updateUI() {
          if (adapter == null) {
            adapter = new Adapter(list);
            mExesRecyclerView.setAdapter(adapter);
        } else {
            adapter.newList(list);
            adapter.notifyDataSetChanged();
        }
    }

    // Holder
    private class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView date, tag, txt;
        View item;
        int position;

        public Holder(View itemView, final int type) {
            super(itemView);
            itemView.setTag(type);
            date = (TextView) itemView.findViewById(R.id.textViewId);
            tag = (TextView) itemView.findViewById(R.id.textViewTag);
            txt = (TextView) itemView.findViewById(R.id.textViewTxt);
            itemView.setOnClickListener(this);


        }

        public void bindHolder(int position, LogsUnit lu) {
            this.position = position;
            date.setText(DateLab.parceDate(lu.getId(), getResources().getStringArray(R.array.mounth)));
            tag.setText(lu.getTag());
            txt.setText(lu.getTxt());
        }

        @Override
        public void onClick(View v) {

        }
    }

// Adapter

    private class Adapter extends RecyclerView.Adapter<LogFragment.Holder> {
        private List<LogsUnit> mList;
        int sizeMinus = 0;
        int[] adapterRange;

        public Adapter(List<LogsUnit> list) {
            newList(list);
        }

        private void newList(List<LogsUnit> list) {
            mList = list;
            adapterRange = new int[mList.size()];
            sizeMinus = list.size();
            for (int i = 0; i < sizeMinus; i++) {
                adapterRange[i] = i;
            }
        }


        @Override
        public int getItemViewType(int position) {

            return 0;
        }

        @Override
        public LogFragment.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view;
            view = layoutInflater
                    .inflate(R.layout.l_log, parent, false);
            return new LogFragment.Holder(view, viewType);

        }

        @Override
        public void onBindViewHolder(LogFragment.Holder holder, int position) {

            holder.bindHolder(position, mList.get(position));
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        loadList();
    }

    private void loadList() {
        bs = SqliteBD.getInstance(getContext());
        list = bs.readLog();
        updateUI();
    }
}
