package com.flexdule.android.control;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.flexdule.R;
import com.flexdule.android.control.sub.HelloRequestTask;
import com.flexdule.android.control.sub.SendSchedulesRequestTask;
import com.flexdule.android.manager.AndroidActivityAccessManager;
import com.flexdule.android.manager.AndroidScheduleAccessManager;
import com.flexdule.android.util.AU;
import com.flexdule.core.dtos.Activity;
import com.flexdule.core.dtos.Schedule;
import com.flexdule.core.dtos.ScheduleWithActivities;
import com.flexdule.core.manager.ActivityAccessManager;
import com.flexdule.core.manager.ScheduleAccessManager;

import java.util.ArrayList;
import java.util.List;

public class ExportActivity extends AppCompatActivity implements HelloRequestTask.HelloRequestTaskListener, SendSchedulesRequestTask.SendSchedulesRequestTaskListener {
    private static final String tag = ExportActivity.class.getSimpleName();

    EditText exportIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        Log.i(tag, "==========[ BEGIN onCreate " + tag + " ]==========");

        exportIp = findViewById(R.id.exportIp);
        Log.i(tag, "==========[ END onCreate " + tag + " ]==========");
    }

    public void onClickExport(View view) {
        Log.i(tag, "BEGIN onClickExport()");

        try {
            ScheduleAccessManager schM = new AndroidScheduleAccessManager(getApplicationContext());
            ActivityAccessManager actM = new AndroidActivityAccessManager(getApplicationContext());

            List<Schedule> schedules = schM.findAllSchedules();
            List<ScheduleWithActivities> schedulesWA = new ArrayList<>();
            for (Schedule sche : schedules) {
                ScheduleWithActivities schWA = new ScheduleWithActivities();
                schWA.setSchedule(sche);
                List<Activity> acs = actM.findActivitiesBySchedule(sche.getIdSchedule());
                schWA.setActivties(acs);
                schedulesWA.add(schWA);
            }
            Log.i(tag, "schedulesWA= " + schedulesWA);

            SendSchedulesRequestTask task = new SendSchedulesRequestTask(this, schedulesWA);
            task.execute(exportIp.getText().toString());

        } catch (Exception e) {
            Log.e(tag, "Error in onClickExport(): " + e);
            AU.toast("No se han podido recuperar los horarios para exportar." + e, this);
            e.printStackTrace();
        }

        Log.i(tag, "END onClickExport()");
    }

    public void onClickTestConnection(View view) {
        Log.i(tag, "DONE onClickTestConnection() exportIp= " + exportIp.getText());
        HelloRequestTask task = new HelloRequestTask(this);
        task.execute(exportIp.getText().toString());
    }

    @Override
    public void onHelloRequest(Boolean result) {
        if (result) {
            AU.toast("Conexión exitosa!", this, 1000);
        } else {
            AU.toast("No se ha podido conectar", this, 1000);
        }
    }

    @Override
    public void onSendSchedulesRequest(Boolean result) {
        if (result) {
            AU.toast("Horarios exportados con éxito", this, 2500);
        } else {
            AU.toast("No se han podido exportar los horarios", this, 2500);
        }
    }

    public void onClickBack(View view) {
        finish();
    }


}
