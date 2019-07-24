package com.flexdule.android.control;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.flexdule.R;
import com.flexdule.android.util.AppTimePickerDialog;
import com.flexdule.android.util.U;
import com.flexdule.core.dtos.Activity;
import com.flexdule.core.dtos.Time;
import com.flexdule.core.util.CU;

public class ActivityEditActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    Time time = new Time();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_edit);

        setFlexibleListeners();

        LinearLayout l = findViewById(R.id.optionDn);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppTimePickerDialog d = new AppTimePickerDialog(ActivityEditActivity.this, "Duración inventada minmaxima", new Time("0:10"), new Time("23:45"), false, time);
                d.show();

            }
        });


    }

    public void setFlexibleListeners() {

        // Flexible S
        Switch sS = findViewById(R.id.optionSwitchS);
        sS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LinearLayout l = findViewById(R.id.optionSx);
                TextView tv = findViewById(R.id.labelOptionSn);

                if (isChecked) {
                    l.setVisibility(View.VISIBLE);
                    tv.setText("Hora de Inicio Mínima");
                } else {
                    l.setVisibility(View.GONE);
                    tv.setText("Hora de Inicio");
                }
            }
        });

        // Flexible D
        Switch sD = findViewById(R.id.optionSwitchD);
        sD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LinearLayout l = findViewById(R.id.optionDx);
                TextView tv = findViewById(R.id.labelOptionDn);

                if (isChecked) {
                    l.setVisibility(View.VISIBLE);
                    tv.setText("Duración Mínima");
                } else {
                    l.setVisibility(View.GONE);
                    tv.setText("Duración");
                }
            }
        });

        // Flexible F
        Switch sF = findViewById(R.id.optionSwitchF);
        sF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LinearLayout l = findViewById(R.id.optionFx);
                TextView tv = findViewById(R.id.labelOptionFn);

                if (isChecked) {
                    l.setVisibility(View.VISIBLE);
                    tv.setText("Hora de Finalización Mínima");
                } else {
                    l.setVisibility(View.GONE);
                    tv.setText("Hora de Finalización");
                }
            }
        });


    }

    public void onClickDelete(View view) {
    }

    public void onClickBack(View view) {
    }

    public void onClickColor(View view) {
        U.toast(time+"", getApplicationContext());
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

    }


}
