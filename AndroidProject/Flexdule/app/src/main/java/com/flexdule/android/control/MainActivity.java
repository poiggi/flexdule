package com.flexdule.android.control;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.flexdule.R;
import com.flexdule.android.control.sub.MainActivityAdapter;
import com.flexdule.android.manager.AndroidActivityAccessManager;
import com.flexdule.android.manager.AndroidCookieAccessManager;
import com.flexdule.android.manager.AndroidScheduleAccessManager;
import com.flexdule.android.util.AndroidLog;
import com.flexdule.android.util.DebugU;
import com.flexdule.android.util.AK;
import com.flexdule.android.util.AU;
import com.flexdule.core.dtos.Activity;
import com.flexdule.core.dtos.Cookie;
import com.flexdule.core.dtos.Schedule;
import com.flexdule.core.manager.ActivityAccessManager;
import com.flexdule.core.manager.CookieAccesManager;
import com.flexdule.core.manager.ScheduleAccessManager;
import com.flexdule.core.manager.ScheduleActivitiesManager;
import com.flexdule.core.util.AppColors;
import com.flexdule.core.util.K;
import com.flexdule.core.util.CoreLog;
import com.flexdule.core.util.Time;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String tag = MainActivity.class.getName();

    private RecyclerView.LayoutManager layoutManager;
    private TextView textViewDelay;
    RecyclerView recyclerView;

    private final boolean debugMode = true;

    CookieAccesManager cooM;
    ScheduleAccessManager schM;
    ActivityAccessManager actM;
    ScheduleActivitiesManager scheduleActivitiesManager;

    Schedule schedule;
    Cookie cooUsingSchedule;
    List<Activity> activities;
    boolean sampleCreated = false;
    String delay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(tag, "==========[ BEGIN onCreate ]==========");

        try {
            // Se instancia el layout
            textViewDelay = findViewById(R.id.textViewDelay);

            // Se instancia el RecyclerView
            layoutManager = new LinearLayoutManager(this);
            recyclerView = (RecyclerView) findViewById(R.id.recycler);
            recyclerView.setLayoutManager(layoutManager);

            // Se instancian los accessManagers
            cooM = new AndroidCookieAccessManager(getApplicationContext());
            schM = new AndroidScheduleAccessManager(getApplicationContext());
            actM = new AndroidActivityAccessManager(getApplicationContext());
            CoreLog log = new AndroidLog(ScheduleActivitiesManager.class.getSimpleName());
            scheduleActivitiesManager = new ScheduleActivitiesManager(log);

            // Se gestiona la información
            manageData();

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
        manageData();
    }

    public void manageData() {
        Log.i(tag, "BEGIN manageData()");
        try {
            debugActionsBegin();

            schedule = null;
            activities = new ArrayList<>();
            delay = null;

            // Se recupera el usingSchema
            findUsingSchedule();

            // Se recuperan las actividades del horario
            if (!sampleCreated)
                activities = actM.findActivitiesBySchedule(schedule.getIdSchedule());

            // Se calcula el contexto del horario
            scheduleActivitiesManager.calcContext(activities);

            // Se bindea el horario a la cabecera
            if (schedule != null) {
                if (schedule.getColor() != null) {
                    CardView cardViewMenu = findViewById(R.id.cardViewMenu);
                    cardViewMenu.setCardBackgroundColor(Color.parseColor("#" + schedule.getColor()));
                }
                TextView textViewTitle = findViewById(R.id.textViewTitle);
                textViewTitle.setText(schedule.getName());
            }

            // Se bindea el contador de retraso
            textViewDelay.setText(delay);

            debugActionsEnd();

            // Se añade el adapter al Recycler y se bindean los objetos
            MainActivityAdapter mAdapter = new MainActivityAdapter(activities);
            recyclerView.setAdapter(mAdapter);

        } catch (Exception e) {
            Log.e(tag, "Error onCreate: " + e);
            AU.toast("Error onCreate" + e, this);
            e.printStackTrace();
        }

        Log.i(tag, "END manageData(). schedule=" + schedule + ", delay=" + delay + ", activities=" + activities);
    }

    public void findUsingSchedule() {
        Log.i(tag, "BEGIN findUsingSchedule()");
        try {
            cooUsingSchedule = cooM.findCookieByName(K.COOKIE_USING_SCHEDULE);
            boolean createCookie = false;
            boolean createdSchedule = false;

            if (cooUsingSchedule != null) {
                int idSchedule = Integer.valueOf(cooUsingSchedule.getValue());
                schedule = schM.findScheduleById(idSchedule);
                Log.i(tag, "Searching schedule by cooUsingSchedule: " + idSchedule + ", schedule= " + schedule);

            } else {
                Log.w(tag, "cooUsingSchedule not found.");
            }

            if (schedule == null) {
                Log.i(tag, "Using schedule not found. Searching first schedule in DB.");
                schedule = schM.findFirstSchedule();
                createCookie = true;
            }

            if (schedule == null) {
                Log.w(tag, "Using schedule not found. Creating a sample one.");
                createSampleSchedule();
                if (schedule != null) {
                    createdSchedule = true;
                }
                createCookie = true;
            }

            if (schedule != null && createCookie) {

                String idSchedule = schedule.getIdSchedule().toString();
                Log.i(tag, "Saving cooUsingSchedule: " + idSchedule);
                cooUsingSchedule = new Cookie();
                cooUsingSchedule.setName(K.COOKIE_USING_SCHEDULE);
                cooUsingSchedule.setValue(idSchedule);
                cooM.saveCookie(cooUsingSchedule);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(tag, "END findUsingSchedule(). schedule= " + schedule + ", cooUsingSchedule=" + cooUsingSchedule);
    }


    public void onClickMenu(View v) {
        Log.i(tag, "BEGIN onClickMenu()");
        Toast.makeText(this, "click menu", Toast.LENGTH_SHORT).show();
    }

    public void onClickChange(View v) {
        Log.i(tag, "BEGIN onClickChange()");
        Toast.makeText(this, "click change", Toast.LENGTH_SHORT).show();

        try {
            DebugU.printAllDb(v.getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickEdit(View v) {
        Log.i(tag, "BEGIN onClickEdit()");
        Toast.makeText(this, "click edit", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, ScheduleEditActivity.class);
        intent.putExtra(AK.EXTRA_ID_SCHEDULE, schedule.getIdSchedule());
        startActivity(intent);
    }

    public void onClickAdd(View v) {
        Log.i(tag, "BEGIN onClickAdd()");

        // Se crea una nueva actividad. Se crea aquí para acceder al contexto del horario (id, etc)
        Activity newActivity = new Activity();
        newActivity.setIdSchedule(schedule.getIdSchedule());
        newActivity.setPositionInSchedule(activities.size());

        // Se recalcula el contexto para añadir límites a la nueva actividad
        activities.add(newActivity);
        try {
            scheduleActivitiesManager.calcContext(activities);
        } catch (Exception e) {
            Log.e(tag, "Error in onClickAdd(): " + e);
            e.printStackTrace();
        }

        // Se lanza el intent con la nueva actividad
        Intent intent = new Intent(this, ActivityEditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AK.SERIALIZED_ACTIVITY, newActivity);
        intent.putExtras(bundle);

        Log.i(tag, "END onClickAdd(). bundle= " + bundle + ", intent= " + intent);
        startActivity(intent);
    }

    public void createSampleSchedule() throws Exception {
        Log.i(tag, "BEGIN createSampleSchedule()");
        schedule = new Schedule();
        schedule.setName("Nuevo Horario");
        schedule.setColor(AppColors.COLOR_WHITE);
        Integer id = schM.saveSchedule(schedule);

        if (id != null) {

            schedule.setIdSchedule(id);

            Activity ac1 = new Activity();
            ac1.setIdSchedule(schedule.getIdSchedule());
            ac1.setName("Desayunar");
            ac1.setPositionInSchedule(0);
            ac1.setColor(AppColors.getRandomColor());
            ac1.getConfigVars().setSn(Time.parse("8:00"));
            ac1.getConfigVars().setSx(Time.parse("9:00"));
            ac1.getConfigVars().setDn(Time.parse("0:10"));
            ac1.getConfigVars().setDx(Time.parse(null));
            ac1.getConfigVars().setFn(Time.parse(null));
            ac1.getConfigVars().setFx(Time.parse(null));
            activities.add(ac1);

            Activity ac2 = new Activity();
            ac2.setIdSchedule(schedule.getIdSchedule());
            ac2.setName("Trabajar");
            ac2.setPositionInSchedule(1);
            ac2.setColor(AppColors.getRandomColor());
            ac2.getConfigVars().setSn(Time.parse("10:30"));
            ac2.getConfigVars().setSx(Time.parse("10:30"));
            ac2.getConfigVars().setDn(Time.parse(null));
            ac2.getConfigVars().setDx(Time.parse(null));
            ac2.getConfigVars().setFn(Time.parse("18:30"));
            ac2.getConfigVars().setFx(Time.parse("19:15"));
            activities.add(ac2);

            Activity ac3 = new Activity();
            ac3.setIdSchedule(schedule.getIdSchedule());
            ac3.setName("Hacer deporte");
            ac3.setPositionInSchedule(2);
            ac3.setColor(AppColors.getRandomColor());
            ac3.getConfigVars().setSn(Time.parse(null));
            ac3.getConfigVars().setSx(Time.parse(null));
            ac3.getConfigVars().setDn(Time.parse(null));
            ac3.getConfigVars().setDx(Time.parse(null));
            ac3.getConfigVars().setFn(Time.parse(null));
            ac3.getConfigVars().setFx(Time.parse("21:30"));
            activities.add(ac3);

            actM.saveActivities(activities);

            Log.i(tag, "Sample Schedule created:");
            Log.i(tag, "sch = " + schedule);
            Log.i(tag, "ac1 =" + ac1);
            Log.i(tag, "ac2 =" + ac2);
            Log.i(tag, "ac3 =" + ac3);
        }
        Log.i(tag, "END createSampleSchedule()");

    }

    public void debugActionsBegin() throws Exception {
        Log.i(tag, "BEGIN debugActionsBegin:");
        DebugU.printAllDb(getApplicationContext());


        Log.i(tag, "END debugActionsBegin:");
    }

    public void debugActionsEnd() throws Exception {
        Log.i(tag, "BEGIN debugActionsEnd:");
//        schM.deleteById(1);

//
//        Activity ac1 = new Activity();
//        ac1.setIdSchedule(schedule.getIdSchedule());
//        ac1.setName("extra");
//        ac1.setPositionInSchedule(activities.size());
//        ac1.setColor(AppColors.getColors().get(K.COLOR_WHITE));
//        ac1.getConfigVars().setSn(C.hourToDur(null));
//        ac1.getConfigVars().setSx(C.hourToDur(null));
//        ac1.getConfigVars().setDn(C.hourToDur("0:10"));
//        ac1.getConfigVars().setDx(C.hourToDur("0:10"));
//        ac1.getConfigVars().setFn(C.hourToDur(null));
//        ac1.getConfigVars().setFx(C.hourToDur(null));
//        activities.add(ac1);
//
//        Activity ac2 = new Activity();
//        ac2.setIdSchedule(schedule.getIdSchedule());
//        ac2.setName("extra");
//        ac2.setPositionInSchedule(activities.size());
//        ac2.setColor(AppColors.getColors().get(K.COLOR_WHITE));
//        ac2.getConfigVars().setSn(C.hourToDur(null));
//        ac2.getConfigVars().setSx(C.hourToDur(null));
//        ac2.getConfigVars().setDn(C.hourToDur("0:10"));
//        ac2.getConfigVars().setDx(C.hourToDur("0:10"));
//        ac2.getConfigVars().setFn(C.hourToDur(null));
//        ac2.getConfigVars().setFx(C.hourToDur(null));
//        activities.add(ac2);
//
//        Activity ac3 = new Activity();
//        ac3.setIdSchedule(schedule.getIdSchedule());
//        ac3.setName("extra");
//        ac3.setPositionInSchedule(activities.size());
//        ac3.setColor(AppColors.getColors().get(K.COLOR_WHITE));
//        ac3.getConfigVars().setSn(C.hourToDur(null));
//        ac3.getConfigVars().setSx(C.hourToDur(null));
//        ac3.getConfigVars().setDn(C.hourToDur("0:10"));
//        ac3.getConfigVars().setDx(C.hourToDur("0:10"));
//        ac3.getConfigVars().setFn(C.hourToDur(null));
//        ac3.getConfigVars().setFx(C.hourToDur(null));
//        activities.add(ac3);
//
//        Activity ac4 = new Activity();
//        ac4.setIdSchedule(schedule.getIdSchedule());
//        ac4.setName("extra");
//        ac4.setPositionInSchedule(activities.size());
//        ac4.setColor(AppColors.getColors().get(K.COLOR_WHITE));
//        ac4.getConfigVars().setSn(C.hourToDur(null));
//        ac4.getConfigVars().setSx(C.hourToDur(null));
//        ac4.getConfigVars().setDn(C.hourToDur("0:10"));
//        ac4.getConfigVars().setDx(C.hourToDur("0:10"));
//        ac4.getConfigVars().setFn(C.hourToDur(null));
//        ac4.getConfigVars().setFx(C.hourToDur(null));
//        activities.add(ac4);
//
//        Activity ac5 = new Activity();
//        ac5.setIdSchedule(schedule.getIdSchedule());
//        ac5.setName("extra");
//        ac5.setPositionInSchedule(activities.size());
//        ac5.setColor(AppColors.getColors().get(K.COLOR_WHITE));
//        ac5.getConfigVars().setSn(C.hourToDur(null));
//        ac5.getConfigVars().setSx(C.hourToDur(null));
//        ac5.getConfigVars().setDn(C.hourToDur("0:10"));
//        ac5.getConfigVars().setDx(C.hourToDur("0:10"));
//        ac5.getConfigVars().setFn(C.hourToDur(null));
//        ac5.getConfigVars().setFx(C.hourToDur(null));
//        activities.add(ac5);
//
//        actM.save(activities);

        DebugU.printAllDb(getApplicationContext());
        Log.i(tag, "END debugActionsEnd:");
    }

}
