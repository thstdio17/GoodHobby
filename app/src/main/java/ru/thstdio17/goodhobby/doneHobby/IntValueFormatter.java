package ru.thstdio17.goodhobby.doneHobby;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by shcherbakov on 29.08.2017.
 */
public class IntValueFormatter implements IValueFormatter {

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return String.valueOf((int) value);
    }
}