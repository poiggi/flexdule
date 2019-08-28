package com.flexdule.android.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.TimePicker;

import com.flexdule.R;

import java.util.ArrayList;
import java.util.List;

import flexdule.core.util.Time;

public class AppTimePickerDialog extends Dialog implements View.OnClickListener {
    public static final String tag = AppTimePickerDialog.class.getSimpleName();

    protected TimePicker picker;
    protected Time min;
    protected Time max;
    protected Time lastTime;
    protected boolean spinner;
    protected String title;
    protected Time actual;

    protected List<AppTimePickerListener> listeners = new ArrayList<>();

    public AppTimePickerDialog(Context context, String title, Time min, Time max, boolean spinner, Time actual) {
        super(context);
        this.min = min;
        this.max = max;
        this.spinner = spinner;
        this.title = title;
        this.actual = actual;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (spinner) {
            setContentView(R.layout.time_picker_spinner);
        } else {
            setContentView(R.layout.time_picker_clock);
        }

        findViewById(R.id.cancelButton).setOnClickListener(this);
        findViewById(R.id.acceptButton).setOnClickListener(this);
        findViewById(R.id.deleteButton).setOnClickListener(this);

        initTitle();
        initPicker();
    }

    private void initTitle() {
        String minTime = "0:00 ", maxTime = " 23:59";
        if (min != null) minTime = min.toString();
        if (max != null) maxTime = max.toString();

        ((TextView) findViewById(R.id.title)).setText(title);
        ((TextView) findViewById(R.id.minMaxSubtitle)).setText("[ " + minTime + " - " + maxTime + " ]");
    }

    public void initPicker() {
        picker = findViewById(R.id.picker);
        if (spinner) picker.setIs24HourView(true);
        else picker.setIs24HourView(false);
        picker.setIs24HourView(true);
        if (actual != null) {
            setPickerTime(actual);
        } else if (min != null) {
            setPickerTime(min);
        } else {
            setPickerTime(new Time());
        }
        picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                validateAndPickChange(hourOfDay, minute);
                Log.d(tag, "pickerTime= " + picker.getHour() + ":" + picker.getMinute() + ", lastTime=" + lastTime);
            }
        });
    }


    @Override
    public void onClick(View v) {
        Log.d(tag, "pickerTime= " + picker.getHour() + ":" + picker.getMinute() + ", lastTime=" + lastTime);

        switch (v.getId()) {
            case R.id.cancelButton:
                Log.i(tag, "Picker: Cencelled");
                dismiss();
                break;
            case R.id.acceptButton:
                Time result = new Time(picker.getHour() + ":" + picker.getMinute());
                Log.i(tag, "Picker: Duration picked= " + result);
                for (AppTimePickerListener listener : listeners) {
                    listener.onTimePicked(result);
                }
                dismiss();
                break;
            case R.id.deleteButton:
                Log.i(tag, "Picker: Duration deleted");
                for (AppTimePickerListener listener : listeners) {
                    listener.onTimePicked(null);
                }
                dismiss();
                break;
        }
    }

    public void validateAndPickChange(int hourOfDay, int minute) {
        Time i = new Time(hourOfDay, minute);
        boolean repeated = false;
        Log.v(tag, "changed: " + hourOfDay + ", " + minute + " [ i= " + i + ", min= " + min + ", max=" + max + ", lastTime= " + lastTime + "]");

        if (spinner && lastTime != null && lastTime.getHours() == 23 && i.getHours() == 0) {
            // Si cambia de 23h a 0h y el max es menor o igual que 23:59 (por los minutos), cambia a max
            Log.d(tag, "[from 23 to 00]");
            if (max != null && max.lessOrEqualTo(Time.parse("23:59"))) {
                Log.d(tag, "picker changed to MAX time [from 23 to 00]");
                setMax();
                repeated = true;
            }

        } else if (spinner && lastTime != null && lastTime.getHours() == 0 && i.getHours() == 23) {
            // Si cambia de 0h a 23h y min es mayor o igual que 00:00 (por los minutos), cambia a min.
            Log.d(tag, "[from 00 to 23]");
            if (min != null && min.greaterOrEqualTo(Time.parse("00:00"))) {
                Log.d(tag, "picker changed to MIN time [from 00 to 23]");
                setMin();
                repeated = true;
            }

        } else if (min != null && i.compareTo(min) < 0) {
            Log.d(tag, "picker changed to MIN time");
            setMin();

        } else if (max != null && i.compareTo(max) > 0) {
            Log.d(tag, "picker changed to MAX time");
            setMax();
        } else {
        }

        if (!repeated) {
            lastTime = i;
        }
    }

    protected void setMin() {
        setPickerTime(min);
        AU.toast("mínimo " + min, getContext(), 500);
    }

    protected void setMax() {
        setPickerTime(max);
        AU.toast("máximo " + max, getContext(), 500);
    }

    public void setPickerTime(Time time) {
        picker.setHour(time.getHours());
        picker.setMinute(time.getMinutes());
    }

    public interface AppTimePickerListener {
        public void onTimePicked(Time time);
    }

    public void addListener(AppTimePickerListener listener) {
        listeners.add(listener);
    }
}
