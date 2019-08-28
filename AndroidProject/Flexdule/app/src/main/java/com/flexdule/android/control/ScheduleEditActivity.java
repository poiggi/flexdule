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

import com.flexdule.R;
import com.flexdule.android.manager.AndroidCookieAccessManager;
import com.flexdule.android.manager.AndroidScheduleAccessManager;
import com.flexdule.android.util.AK;
import com.flexdule.android.util.AU;

import flexdule.core.dtos.Schedule;
import flexdule.core.model.managers.ScheduleAccessManager;
import flexdule.core.util.AppColors;

public class ScheduleEditActivity extends AppCompatActivity {
    private static final String tag = ScheduleEditActivity.class.getName();

    Schedule schedule;
    AppColors colors = new AppColors();
    ScheduleAccessManager schM;
    EditText editName;
    ConstraintLayout editLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_edit);
        Log.i(tag, "==========[ BEGIN onCreate ]==========");

        editName = findViewById(R.id.editName);
        editLayout = findViewById(R.id.editLayout);

        try {
            schM = new AndroidScheduleAccessManager(getApplicationContext());
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Integer id = bundle.getInt(AK.KEY_ID_SCHEDULE);
                Log.i(tag, "id = " + id);
                schedule = schM.findScheduleById(id);
                Log.i(tag, "schedule = " + schedule);
            } else {
                // Es creación de horario
                Log.i(tag, "Creating new schedule");
//                findViewById(R.id.floatingActionEnter).setVisibility(View.VISIBLE);
                schedule = new Schedule();
                schedule.setColor(AppColors.COLOR_WHITE);
            }

            if (schedule != null) {
                editName.setText(schedule.getName());
                editLayout.setBackgroundColor(Color.parseColor("#" + schedule.getColor()));
            } else {
                schedule = new Schedule();
            }

        } catch (Exception e) {
            Log.e(tag, "Error in onCreate(): " + e);
            AU.toast("Error in onCreate(): " + e, getApplicationContext());
            e.printStackTrace();
        }
        Log.i(tag, "==========[ END onCreate ]==========");
    }

    @Override
    public void onPause() {
        if (schedule != null) saveOnExit(false);
        super.onPause();
    }

    protected void saveOnExit(boolean creatingAndEntering) {
        Log.i(tag, "BEGIN saveOnExit()");
        boolean saveSchedule = false;

        String name = editName.getText().toString();
        Log.i(tag, "name=" + name);

        if (!TextUtils.isEmpty(name)) {
            // Si el nombre es válido, se guarda
            schedule.setName(name);
            saveSchedule = true;
        } else {
            if (creatingAndEntering) {
                // Si se está usando el botón entrar de la creación de horario
                AU.toast("El horario debe tener un nombre", getApplicationContext());
            } else if (schedule.getIdSchedule() != null) {
                // Si el nombre no es válido, pero es edición y se está saliendo.
                // Se guarda con el antiguo nombre
                AU.toast("No se puede guardar horario sin un nombre. Nombre restaurado",
                        getApplicationContext());
                saveSchedule = true;
            } else {
                // Si el nombre no es válido y es creación, se desecha
                AU.toast("Horario sin nombre desechado", getApplicationContext());
            }
        }

        if (saveSchedule) {
            try {
                Integer id = schM.saveSchedule(schedule);
                if (schedule.getIdSchedule() == null) schedule.setIdSchedule(id);
            } catch (Exception e) {
                e.printStackTrace();
                AU.toast("Error al guardar el horario", getApplicationContext());
            }
        }
        Log.i(tag, "BEGIN saveOnExit()");
    }

    public void onClickBack(View v) {
        finish();
    }

    public void onClickColor(View v) {
        schedule.setColor(colors.getNextColor());
        editLayout.setBackgroundColor(Color.parseColor("#" + schedule.getColor()));
    }

    public void onClickDelete(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Eliminar horario?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                try {
                    schM.deleteScheduleById(schedule.getIdSchedule());
                    schedule = null; // Se elimina de memoria para que no se guarde al salir

                    Intent i = new Intent(ScheduleEditActivity.this, ScheduleListActivity.class);
                    finish();  //Kill the activity from which you will go to next activity
                    startActivity(i);
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

    public void onClickEnter(View view) {
        saveOnExit(true);
        if (schedule.getIdSchedule() != null) {// Si tiene id quiere decir que se ha guardado
            try {
                AndroidCookieAccessManager cooM = new AndroidCookieAccessManager(this);
                cooM.saveCooUsingSchedule(schedule.getIdSchedule());
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Log.e(tag, "Error in onClickEnter(): " + e);
                AU.toast("Error in onClickEnter(): " + e, getApplicationContext());
                e.printStackTrace();
            }
        }
    }
}
