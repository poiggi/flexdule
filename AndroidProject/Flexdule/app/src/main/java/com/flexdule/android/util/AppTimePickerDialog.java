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
import com.flexdule.core.dtos.Time;
import com.flexdule.core.util.CU;

import java.util.ArrayList;
import java.util.List;

public class AppTimePickerDialog extends Dialog implements View.OnClickListener {
    public static final String tag = AppTimePickerDialog.class.getSimpleName();

    protected TimePicker picker;
    protected Time min;
    protected Time max;
    protected Time lastTime;
    protected boolean spinner;
    protected String title;
//    protected List<AppTimePickerDialogInterface> listeners = new ArrayList<>();
    protected Time saving;

    public AppTimePickerDialog(Context context, String title, Time min, Time max, boolean spinner, Time saveIn) {
        super(context);
        this.min = min;
        this.max = max;
        this.spinner = spinner;
        this.title = title;
        this.saving = saveIn;
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

        initTitle();

        initPicker();


    }

    private void initTitle() {
        String minTime = "", maxTime = "";
        if(min != null) minTime = min.toString();
        if(max != null) maxTime = max.toString();

        ((TextView) findViewById(R.id.title)).setText(title);
        ((TextView) findViewById(R.id.minMaxSubtitle)).setText("[ " + minTime + " - " + maxTime + " ]");
    }

    public void initPicker(){
        picker = findViewById(R.id.picker);
        if (spinner) picker.setIs24HourView(true);
        else picker.setIs24HourView(false);
        if (min != null) {
            setPickerTime(min);
        } else {
            setPickerTime(new Time());
        }
        picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                validateChange(hourOfDay, minute);
            }
        });
    }


    @Override
    public void onClick(View v) {
        Log.d(tag, "clicked: " + v.getId());
        switch (v.getId()) {
            case R.id.cancelButton:
                dismiss();
                break;
            case R.id.acceptButton:
                Log.d(tag, "saving= "+saving);
                saving.toZero();
                saving.addHours(picker.getHour());
                saving.addMinutes(picker.getHour());
                Log.d(tag,"picker values: "+ picker.getHour()+":"+picker.getMinute());
                Log.i(tag,"Duration picked and saved: "+ saving);
                dismiss();
                break;
        }
    }

    public void validateChange(int hourOfDay, int minute) {
        Time i = new Time(hourOfDay, minute);
        boolean repeated = false;
        Log.v(tag, "changed: " + hourOfDay + ", " + minute + " [ i= " + i + ", min= " + min + ", max=" + max + ", lastTime= " + lastTime + "]");
        Log.v(tag, "min? " + i.compareTo(min) + ", max? " + i.compareTo(max));

        if (spinner && lastTime != null && lastTime.getHours() == 23 && i.getHours() == 0) {
            Log.d(tag, "[from 23 to 00]");
            if (max != null && max.compareTo(Time.parse("24:00")) < 0) {
                Log.d(tag, "picker changed to MAX time [from 23 to 00]");
                setPickerTime(max);
                repeated = true;
            }

        } else if (spinner && lastTime != null && lastTime.getHours() == 0 && i.getHours() == 23) {
            Log.d(tag, "[from 00 to 23]");
            if (max != null && Time.parse("00:00").compareTo(min) < 0) {
                Log.d(tag, "picker changed to MIN time [from 00 to 23]");
                setPickerTime(min);
                repeated = true;
            }

        } else if (min != null && i.compareTo(min) < 0) {
            Log.d(tag, "picker changed to MIN time");
            setPickerTime(min);

        } else if (max != null && i.compareTo(max) > 0) {
            Log.d(tag, "picker changed to MAX time");
            setPickerTime(max);
        }

        if (!repeated) lastTime = i;
    }

    public void setPickerTime(Time time) {
        picker.setHour(time.getHours());
        picker.setMinute(time.getMinutes());
    }

}
