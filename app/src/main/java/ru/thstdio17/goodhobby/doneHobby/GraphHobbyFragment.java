package ru.thstdio17.goodhobby.doneHobby;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.thstdio17.goodhobby.R;
import ru.thstdio17.goodhobby.hobby.DateLab;
import ru.thstdio17.goodhobby.hobby.LogItem;
import ru.thstdio17.goodhobby.sqlite.SqliteBD;

/**
 * Created by shcherbakov on 12.08.2017.
 */

public class GraphHobbyFragment extends Fragment {
    private static final String ID = "idHobby";
    protected SqliteBD bs;

    protected BarChart mChartDay, mChartCount;

    public static GraphHobbyFragment newInstance(int id) {
        Bundle args = new Bundle();
        args.putSerializable(ID, id);
        GraphHobbyFragment fragment = new GraphHobbyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.m_done_hobby_graph, container, false);

        int id = getArguments().getInt(ID);
        bs = SqliteBD.getInstance(getContext());
        List<LogItem> list = bs.getLogHobby(id);
        Map<Integer, Integer> day = new HashMap<>();
        Map<Integer, Integer> count = new HashMap<>();
        for (LogItem l : list) {
            int week = DateLab.numberYear(l.getTime()) * 100 + DateLab.numberOfWeekNow(l.getTime());
            if (!day.containsKey(week)) day.put(week, 1);
            else {
                int tDay = day.get(week);
                day.put(week, 1 + tDay);
            }
            if (!count.containsKey(week)) count.put(week, l.getCount());
            else {
                int tCount = count.get(week);
                count.put(week, l.getCount() + tCount);
            }

        }
        mChartDay = v.findViewById(R.id.chartDay);
        mChartCount = v.findViewById(R.id.chartCount);
        init(mChartDay);
        setData(mChartDay, day);
        init(mChartCount);
        setData(mChartCount, count);
        return v;
    }

    private void init(BarChart mChart) {
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);

        mChart.getDescription().setEnabled(false);

        mChart.setMaxVisibleValueCount(60);

        mChart.setPinchZoom(false);

        mChart.setDrawGridBackground(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);

        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.NONE);

    }

    private void setData(BarChart mChart, Map<Integer, Integer> map) {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        List<Integer> indexs = new ArrayList<>();
        indexs.addAll(map.keySet());
        Collections.sort(indexs, new Comparator<Integer>() {
            @Override
            public int compare(Integer i1, Integer i2) {
                if (i1 > i2) return 1;
                if (i1 < i2) return -1;
                return 0;
            }
        });
        int index = 1;
        for (int i : indexs) {

            yVals1.add(new BarEntry(index++, map.get(i)));
        }


        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            set1.setValueFormatter(new IntValueFormatter());
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "");

            set1.setDrawIcons(false);

            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setValueFormatter(new IntValueFormatter());
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);

            mChart.setData(data);
        }
    }


}

