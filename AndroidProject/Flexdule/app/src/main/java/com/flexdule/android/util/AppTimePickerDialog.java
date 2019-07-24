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
import com.flexdule.core.util.CU;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AppTimePickerDialog extends Dialog implements View.OnClickListener {
    public static final String tag = AppTimePickerDialog.class.getSimpleName();

    protected TimePicker picker;
    protected Duration min;
    protected Duration max;
    protected Duration lastTime;
    protected boolean spinner;
    protected String title;
    protected List<AppTimePickerDialogInterface> listeners = new ArrayList<>();
    protected com.flexdule.core.dtos.Activity saving;

    public AppTimePickerDialog(Context context, String title, Duration min, Duration max, boolean spinner, com.flexdule.core.dtos.Activity saveIn) {
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

        ((TextView) findViewById(R.id.title)).setText(title);
        ((TextView) findViewById(R.id.minMaxSubtitle)).setText("[ " + CU.durToHour(min) + " - " + CU.durToHour(max) + " ]");
        findViewById(R.id.cancelButton).setOnClickListener(this);
        findViewById(R.id.acceptButton).setOnClickListener(this);

        picker = findViewById(R.id.picker);
        if (spinner) picker.setIs24HourView(true);
        else picker.setIs24HourView(false);
        if (min != null) {
            setPickerTime(min);
        } else {
            setPickerTime(CU.hourToDur("00:00"));
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
                Duration saving = Duration.ofHours(0);
                Log.v(tag, "saving= "+saving);
                saving =saving.plusHours(picker.getHour());
                saving =saving.plusMinutes(picker.getMinute());
                this.saving.getFinalVars().setDn(saving);
                Log.v(tag, "saving= "+saving);
                Log.d(tag,"Picker values: "+ picker.getHour()+":"+picker.getMinute());
                Log.i(tag,"Duration picked and saved: "+ saving);
                dismiss();
                break;
        }
    }

    public void validateChange(int hourOfDay, int minute) {
        Duration i = Duration.ofHours(hourOfDay);
        i = i.plusMinutes(minute);
        boolean repeated = false;
        Log.v(tag, "changed: " + hourOfDay + ", " + minute + " [ i= " + i + ", min= " + min + ", max=" + max + ", lastTime= " + lastTime + "]");
        Log.v(tag, "min? " + i.compareTo(min) + ", max? " + i.compareTo(max));

        if (spinner && lastTime != null && CU.durHours(lastTime) == 23 && CU.durHours(i) == 0) {
            Log.d(tag, "[from 23 to 00]");
            if (max != null && max.compareTo(CU.hourToDur("24:00")) < 0) {
                Log.d(tag, "picker changed to MAX time [from 23 to 00]");
                setPickerTime(max);
                repeated = true;
            }

        } else if (spinner && lastTime != null && CU.durHours(lastTime) == 0 && CU.durHours(i) == 23) {
            Log.d(tag, "[from 00 to 23]");
            if (max != null && CU.hourToDur("00:00").compareTo(min) < 0) {
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

    public void setPickerTime(Duration duration) {
        picker.setHour(CU.durHours(duration));
        picker.setMinute(CU.durMinutes(duration));
    }


    public interface AppTimePickerDialogInterface {
        public void onAccept(Duration durationPicked);
    }
}
