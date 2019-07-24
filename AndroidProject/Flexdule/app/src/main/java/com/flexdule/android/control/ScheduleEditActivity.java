package com.flexdule.android.control;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flexdule.R;
import com.flexdule.android.manager.AndroidScheduleAccessManager;
import com.flexdule.android.util.K;
import com.flexdule.android.util.U;
import com.flexdule.core.dtos.Schedule;
import com.flexdule.core.manager.ScheduleAccessManager;
import com.flexdule.core.util.AppColors;

public class ScheduleEditActivity extends AppCompatActivity {
    private static final String tag = ScheduleEditActivity.class.getName();

    Schedule schedule;
    AppColors colors = new AppColors();
    ScheduleAccessManager schM;
    EditText editName;
    ConstraintLayout editLayout;
    boolean saveScheduleEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_edit);
        editName = findViewById(R.id.editName);
        editLayout = findViewById(R.id.editLayout);

        try {
            schM = new AndroidScheduleAccessManager(getApplicationContext());
            Intent intent = getIntent();
            Integer id = intent.getExtras().getInt(K.EXTRA_ID_SCHEDULE);
            Log.i(tag, "id = " + id);
            if (id != null) {
                schedule = schM.findScheduleById(id);
                Log.i(tag, "schedule = " + schedule);
            }

            if (schedule != null) {
                editName.setText(schedule.getName());
                editLayout.setBackgroundColor(Color.parseColor("#" + schedule.getColor()));
            } else {
                schedule = new Schedule();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPause() {
        exitOperations();
        super.onPause();
    }

    public void exitOperations() {
        Log.i(tag, "BEGIN exitOperations()");

        String name = editName.getText().toString();
        Log.i(tag, "name=" + name);

        if (!TextUtils.isEmpty(name)) {
            // Si el nombre es válido, se guarda
            schedule.setName(name);
            saveScheduleEdit = true;
        } else {
            if (schedule.getIdSchedule() != null) {
                // Si el nombre no es válido, pero es edición, se guarda sin el nombre
                U.toast("No se puede guardar el nombre de horario vacío", getApplicationContext());
                saveScheduleEdit = true;
            } else {
                // Si el nombre no es válido y es creación, se desecha
                U.toast("Horario sin nombre desechado", getApplicationContext());
            }
        }

        if (saveScheduleEdit) {
            try {
                schM.saveSchedule(schedule);
            } catch (Exception e) {
                e.printStackTrace();
                U.toast("Error al guardar el horario", getApplicationContext());
            }
        }

        Log.i(tag, "BEGIN exitOperations()");
    }

    public void onClickBack(View v) {
        finish();
    }

    public void onClickColor(View v) {
        schedule.setColor(colors.getNextColor());
        editLayout.setBackgroundColor(Color.parseColor("#" + schedule.getColor()));
    }

    public void onClickDelete(View v) {
        confirmDialog();
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Eliminar horario?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                try {
                    schM.deleteScheduleById(schedule.getIdSchedule());
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.DKGRAY);
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.DKGRAY);
        TextView tv = dialog.findViewById(android.R.id.message);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
    }
}