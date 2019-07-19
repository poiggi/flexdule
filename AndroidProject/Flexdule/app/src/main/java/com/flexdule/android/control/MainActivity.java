package com.flexdule.android.control;

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
import com.flexdule.android.manager.AndroidActivityAccessManager;
import com.flexdule.android.manager.AndroidCookieAccessManager;
import com.flexdule.android.manager.AndroidScheduleAccessManager;
import com.flexdule.android.util.DebugU;
import com.flexdule.core.dtos.Activity;
import com.flexdule.core.dtos.Cookie;
import com.flexdule.core.dtos.Schedule;
import com.flexdule.core.manager.ActivityAccessManager;
import com.flexdule.core.manager.CookieAccesManager;
import com.flexdule.core.manager.ScheduleAccessManager;
import com.flexdule.core.util.AppColors;
import com.flexdule.core.util.CK;
import com.flexdule.core.util.CU;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String tag = MainActivity.class.getName();

    private RecyclerView.LayoutManager layoutManager;
    private final boolean debugMode = true;

    CookieAccesManager cooM;
    ScheduleAccessManager schM;
    ActivityAccessManager actM;

    Schedule schedule;
    Cookie usingSchCookie;
    List<Activity> activities = new ArrayList<>();
    boolean sampleCreated = false;
    String delay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(tag, "BEGIN onCreate");

        try {
            // Se instancia el layout
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            // Se instancia el RecyclerView
            layoutManager = new LinearLayoutManager(this);
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
            recyclerView.setLayoutManager(layoutManager);

            // Se instancian los accessManagers
            cooM = new AndroidCookieAccessManager(getApplicationContext());
            schM = new AndroidScheduleAccessManager(getApplicationContext());
            actM = new AndroidActivityAccessManager(getApplicationContext());

            debugActionsBegin();

            // Se recupera el usingSchema
            findUsingSchedule();

            // Se recuperan las actividades del horario
            if (!sampleCreated) activities = actM.findBySchedule(schedule.getIdSchedule());


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
            TextView textViewDelay = findViewById(R.id.textViewDelay);
            textViewDelay.setText(delay);

            // Se añade el adapter al Recycler y se bindean los objetos
            MainActivityAdapter mAdapter = new MainActivityAdapter(activities);
            recyclerView.setAdapter(mAdapter);

            debugActionsEnd();

        } catch (Exception e) {
            Log.i(tag, "Error in onCreate: " + e);
            Toast.makeText(this, "Error in onCreate", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public void findUsingSchedule() {
        try {
            usingSchCookie = cooM.findById(CK.COOKIE_USING_SCHEDULE);
            boolean createCookie = false;

            if (usingSchCookie != null) {
                int idSchedule = Integer.valueOf(usingSchCookie.getValue());
                Log.i(tag, "Searching schedule by usingSchCookie: " + idSchedule);
                schedule = schM.findById(idSchedule);
            } else {
                Log.w(tag, "usingSchCookie not found.");
                createCookie = true;
                Log.i(tag, "Searching first schedule in DB.");
                schedule = schM.findFirstOne();
            }

            if (schedule == null) {
                Log.w(tag, "Using schedule not found. Creating a sample one.");
                createSampleSchedule();
            }

            if (createCookie) {
                String idSchedule = schedule.getIdSchedule().toString();
                Log.i(tag, "Saving new usingSchCookie: " + idSchedule);
                usingSchCookie = new Cookie();
                usingSchCookie.setIdCookie(CK.COOKIE_USING_SCHEDULE);
                usingSchCookie.setValue(idSchedule);
                cooM.save(usingSchCookie);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void onClickMenu(View v) {
        Toast.makeText(this, "click menu", Toast.LENGTH_SHORT).show();
    }

    public void onClickChange(View v) {
        Toast.makeText(this, "click change", Toast.LENGTH_SHORT).show();
    }

    public void onClickEdit(View v) {
        Toast.makeText(this, "click edit", Toast.LENGTH_SHORT).show();

        try {
            DebugU.printAllDb(v.getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClickAdd(View v) {
        Toast.makeText(this, "click add", Toast.LENGTH_SHORT).show();
    }

    public void createSampleSchedule() throws Exception {
        schedule = new Schedule();
        schedule.setIdSchedule(1);
        schedule.setName("Nuevo Horario");
        schedule.setColor(AppColors.getRandomColor());
        schM.save(schedule);

        Activity ac1 = new Activity();
        ac1.setIdSchedule(schedule.getIdSchedule());
        ac1.setName("Desayunar");
        ac1.setPositionInSchedule(1);
        ac1.setColor(AppColors.getRandomColor());
        ac1.getConfigVars().setSn(CU.hourToDur("8:00"));
        ac1.getConfigVars().setSx(CU.hourToDur("9:00"));
        ac1.getConfigVars().setDn(CU.hourToDur("0:10"));
        ac1.getConfigVars().setDx(CU.hourToDur(null));
        ac1.getConfigVars().setFn(CU.hourToDur(null));
        ac1.getConfigVars().setFx(CU.hourToDur(null));
        activities.add(ac1);

        Activity ac2 = new Activity();
        ac2.setIdSchedule(schedule.getIdSchedule());
        ac2.setName("Trabajar");
        ac2.setPositionInSchedule(2);
        ac2.setColor(AppColors.getRandomColor());
        ac2.getConfigVars().setSn(CU.hourToDur("10:30"));
        ac2.getConfigVars().setSx(CU.hourToDur("10:30"));
        ac2.getConfigVars().setDn(CU.hourToDur(null));
        ac2.getConfigVars().setDx(CU.hourToDur(null));
        ac2.getConfigVars().setFn(CU.hourToDur("18:30"));
        ac2.getConfigVars().setFx(CU.hourToDur("19:15"));
        activities.add(ac2);

        Activity ac3 = new Activity();
        ac3.setIdSchedule(schedule.getIdSchedule());
        ac3.setName("Hacer deporte");
        ac3.setPositionInSchedule(3);
        ac3.setColor(AppColors.getRandomColor());
        ac3.getConfigVars().setSn(CU.hourToDur(null));
        ac3.getConfigVars().setSx(CU.hourToDur(null));
        ac3.getConfigVars().setDn(CU.hourToDur(null));
        ac3.getConfigVars().setDx(CU.hourToDur(null));
        ac3.getConfigVars().setFn(CU.hourToDur(null));
        ac3.getConfigVars().setFx(CU.hourToDur("21:30"));
        activities.add(ac3);

        actM.save(activities);

        Log.i(tag, "Sample Schedule created:");
        Log.i(tag, "sch = " + schedule);
        Log.i(tag, "ac1 =" + ac1);
        Log.i(tag, "ac2 =" + ac2);
        Log.i(tag, "ac3 =" + ac3);

    }

    public void debugActionsBegin() throws Exception {
        Log.i(tag, "debugActionsBegin:");

        DebugU.printAllDb(getApplicationContext());
    }

    public void debugActionsEnd() throws Exception {
        Log.i(tag, "debugActionsEnd:");
//        schM.deleteById(1);

//
//        Activity ac1 = new Activity();
//        ac1.setIdSchedule(schedule.getIdSchedule());
//        ac1.setName("extra");
//        ac1.setPositionInSchedule(activities.size());
//        ac1.setColor(AppColors.getColors().get(CK.COLOR_WHITE));
//        ac1.getConfigVars().setSn(CU.hourToDur(null));
//        ac1.getConfigVars().setSx(CU.hourToDur(null));
//        ac1.getConfigVars().setDn(CU.hourToDur("0:10"));
//        ac1.getConfigVars().setDx(CU.hourToDur("0:10"));
//        ac1.getConfigVars().setFn(CU.hourToDur(null));
//        ac1.getConfigVars().setFx(CU.hourToDur(null));
//        activities.add(ac1);

//        Activity ac2 = new Activity();
//        ac2.setIdSchedule(schedule.getIdSchedule());
//        ac2.setName("extra");
//        ac2.setPositionInSchedule(activities.size());
//        ac2.setColor(AppColors.getRandomColor());
//        ac2.getConfigVars().setSn(CU.hourToDur(null));
//        ac2.getConfigVars().setSx(CU.hourToDur(null));
//        ac2.getConfigVars().setDn(CU.hourToDur("0:10"));
//        ac2.getConfigVars().setDx(CU.hourToDur("0:10"));
//        ac2.getConfigVars().setFn(CU.hourToDur(null));
//        ac2.getConfigVars().setFx(CU.hourToDur(null));
//        activities.add(ac2);
//
//        Activity ac3 = new Activity();
//        ac3.setIdSchedule(schedule.getIdSchedule());
//        ac3.setName("extra");
//        ac3.setPositionInSchedule(activities.size());
//        ac3.setColor(AppColors.getRandomColor());
//        ac3.getConfigVars().setSn(CU.hourToDur(null));
//        ac3.getConfigVars().setSx(CU.hourToDur(null));
//        ac3.getConfigVars().setDn(CU.hourToDur("0:10"));
//        ac3.getConfigVars().setDx(CU.hourToDur("0:10"));
//        ac3.getConfigVars().setFn(CU.hourToDur(null));
//        ac3.getConfigVars().setFx(CU.hourToDur(null));
//        activities.add(ac3);
//
//        Activity ac4 = new Activity();
//        ac4.setIdSchedule(schedule.getIdSchedule());
//        ac4.setName("extra");
//        ac4.setPositionInSchedule(activities.size());
//        ac4.setColor(AppColors.getRandomColor());
//        ac4.getConfigVars().setSn(CU.hourToDur(null));
//        ac4.getConfigVars().setSx(CU.hourToDur(null));
//        ac4.getConfigVars().setDn(CU.hourToDur("0:10"));
//        ac4.getConfigVars().setDx(CU.hourToDur("0:10"));
//        ac4.getConfigVars().setFn(CU.hourToDur(null));
//        ac4.getConfigVars().setFx(CU.hourToDur(null));
//        activities.add(ac4);
//
//        Activity ac5 = new Activity();
//        ac5.setIdSchedule(schedule.getIdSchedule());
//        ac5.setName("extra");
//        ac5.setPositionInSchedule(activities.size());
//        ac5.setColor(AppColors.getRandomColor());
//        ac5.getConfigVars().setSn(CU.hourToDur(null));
//        ac5.getConfigVars().setSx(CU.hourToDur(null));
//        ac5.getConfigVars().setDn(CU.hourToDur("0:10"));
//        ac5.getConfigVars().setDx(CU.hourToDur("0:10"));
//        ac5.getConfigVars().setFn(CU.hourToDur(null));
//        ac5.getConfigVars().setFx(CU.hourToDur(null));
//        activities.add(ac5);
//
//        actM.save(activities);

        DebugU.printAllDb(getApplicationContext());
    }

}
