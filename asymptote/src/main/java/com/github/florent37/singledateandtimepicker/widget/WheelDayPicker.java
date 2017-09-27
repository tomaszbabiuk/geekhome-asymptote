package com.github.florent37.singledateandtimepicker.widget;

import android.content.Context;
import android.util.AttributeSet;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import eu.geekhome.asymptote.R;

public class WheelDayPicker extends WheelPicker {

    public static final int DAYS_PADDING = 364;
    private int defaultIndex;

    private int todayPosition;

    private SimpleDateFormat simpleDateFormat;

    private OnDaySelectedListener onDaySelectedListener;

    WheelPicker.Adapter adapter;

    public WheelDayPicker(Context context) {
        this(context, null);
    }

    public WheelDayPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.simpleDateFormat = new SimpleDateFormat("EEE d MMM", getCurrentLocale());
        this.adapter = new Adapter();
        setAdapter(adapter);

        updateDays();

        updateDefaultDay();
    }

    public WheelDayPicker setDayFormatter(SimpleDateFormat simpleDateFormat){
        this.simpleDateFormat = simpleDateFormat;
        updateDays();
        return this;
    }

    @Override
    protected void onItemSelected(int position, Object item) {
        if (null != onDaySelectedListener) {
            final String itemText = (String) item;
            final Date date = convertItemToDate(position);
            onDaySelectedListener.onDaySelected(this, position, itemText, date);
        }
    }

    @Override
    protected void onItemCurrentScroll(int position, Object item) {

    }

    @Override
    public int getDefaultItemPosition() {
        return defaultIndex;
    }

    private void updateDays() {
        final List<String> data = new ArrayList<>();

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, -1 * DAYS_PADDING - 1);
        for (int i = (-1) * DAYS_PADDING; i < 0; ++i) {
            instance.add(Calendar.DAY_OF_MONTH, 1);
            data.add(getFormattedValue(instance.getTime()));
        }

        todayPosition = data.size();
        defaultIndex = todayPosition;

        //today
        data.add(getResources().getString(R.string.picker_today));

        instance = Calendar.getInstance();

        for (int i = 0; i < DAYS_PADDING; ++i) {
            instance.add(Calendar.DATE, 1);
            data.add(getFormattedValue(instance.getTime()));
        }

        adapter.setData(data);
        notifyDatasetChanged();
    }

    protected String getFormattedValue(Object value) {
        return simpleDateFormat.format(value);
    }

    public void setOnDaySelectedListener(OnDaySelectedListener onDaySelectedListener) {
        this.onDaySelectedListener = onDaySelectedListener;
    }

    private void updateDefaultDay() {
        setSelectedItemPosition(defaultIndex);
    }

    public int getDefaultDayIndex() {
        return defaultIndex;
    }

    public Date getCurrentDate() {
        return convertItemToDate(super.getCurrentItemPosition());
    }

    private Date convertItemToDate(int itemPosition) {
        Date date = null;
        String itemText = adapter.getItemText(itemPosition);
        final Calendar todayCalendar = Calendar.getInstance();
        if (itemPosition == todayPosition) {
            date = todayCalendar.getTime();
        } else {
            try {
                date = simpleDateFormat.parse(itemText);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (date != null) {
            //try to know the year
            final Calendar dateCalendar = Calendar.getInstance();
            dateCalendar.setTime(date);

            todayCalendar.add(Calendar.DATE, (itemPosition - todayPosition));

            dateCalendar.set(Calendar.YEAR, todayCalendar.get(Calendar.YEAR));
            date = dateCalendar.getTime();
        }

        return date;
    }

    public String getCurrentDay() {
        return adapter.getItemText(getCurrentItemPosition());
    }

    public interface OnDaySelectedListener {
        void onDaySelected(WheelDayPicker picker, int position, String name, Date date);
    }
}