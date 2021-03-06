package com.flexdule.android.control;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.flexdule.R;
import com.flexdule.android.control.sub.MainMenu;
import com.flexdule.android.control.sub.ScheduleListAdapter;
import com.flexdule.android.manager.AndroidScheduleAccessManager;
import com.flexdule.android.util.AU;

import java.util.List;

import flexdule.core.dtos.Schedule;
import flexdule.core.model.managers.ScheduleAccessManager;

public class ScheduleListActivity extends AppCompatActivity {
    private static final String tag = ScheduleListActivity.class.getSimpleName();

    private RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    ScheduleAccessManager schM;
    List<Schedule> schedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);
        Log.i(tag, "==========[ BEGIN onCreate ]==========");

        try {

            // Se instancia el RecyclerView
            layoutManager = new LinearLayoutManager(this);
            recyclerView = (RecyclerView) findViewById(R.id.recycler);
            recyclerView.setLayoutManager(layoutManager);

            // Se instancian los accessManagers
            schM = new AndroidScheduleAccessManager(getApplicationContext());

        } catch (Exception e) {
            Log.e(tag, "Error onCreate: " + e);
            AU.toast("Error onCreate" + e, this);
            e.printStackTrace();
        }
        Log.i(tag, "==========[ END onCreate ]==========");

    }

    @Override
    public void onResume() {
        super.onResume();
        recoverData();
    }

    private void recoverData() {
        Log.i(tag, "BEGIN recoverData()");
        try {

            // Se recupera la lista de horarios
            schedules = schM.findAllSchedules();

            // Se añade el adapter al Recycler y se bindean los objetos
            ScheduleListAdapter adapter = new ScheduleListAdapter(schedules);
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            Log.e(tag, "Error in recoverData(): " + e);
            AU.toast("Error in recoverData(): " + e, this);
            e.printStackTrace();
        }
    }

    public void onClicKAdd(View view) {
        Intent intent = new Intent(this, ScheduleEditActivity.class);
        startActivity(intent);
    }

    public void onClickMenu2(View view) {
        Log.i(tag, "BEGIN onClickMenu()");
        MainMenu.show(this, view);
    }
}
