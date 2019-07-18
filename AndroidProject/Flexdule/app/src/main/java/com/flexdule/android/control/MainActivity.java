package com.flexdule.android.control;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.flexdule.R;
import com.flexdule.core.dtos.Activity;
import com.flexdule.core.manager.ActivityAccessManager;
import com.flexdule.core.utils.CU;
import com.flexdule.android.manager.AndroidActivityAccessManager;
import com.flexdule.android.model.sqlite.FlexduleDataBase;
import com.flexdule.android.util.DebugU;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String tag = MainActivity.class.getName();

    private RecyclerView.LayoutManager layoutManager;
    private final boolean debugMode = true;

    FlexduleDataBase db;

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

            debugActions();

            // Se recuperan las actividades del horario
            List<Activity> acs = null;
            ActivityAccessManager aam = new AndroidActivityAccessManager(getApplicationContext());
            acs = aam.findBySchedule(1);

            // Se añade el adapter al Recycler y se bindean los objetos
            MainActivityAdapter mAdapter = new MainActivityAdapter(acs);
            recyclerView.setAdapter(mAdapter);

        } catch (Exception e) {
            Log.i(tag, "Error in onCreate: " + e);
            Toast.makeText(this, "Error in onCreate", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    public void debugActions() {
        try {
            ActivityAccessManager aam = new AndroidActivityAccessManager(getApplicationContext());

            Activity ac = new Activity();
            ac.setIdActivity(1);
            ac.setIdSchedule(1);
            ac.setName("Actividad 1");
            ac.setPositionInSchedule(1);
            ac.setColor("00ff00");
            ac.getConfigVars().setSn(CU.hourToDur("8:00"));
            ac.getConfigVars().setSx(CU.hourToDur("9:00"));
            ac.getConfigVars().setDn(CU.hourToDur("0:10"));
            ac.getConfigVars().setDx(CU.hourToDur("0:20"));
            ac.getConfigVars().setFn(CU.hourToDur(null));
            ac.getConfigVars().setFx(CU.hourToDur(null));
            aam.save(ac);

            // find All
            List<Activity> allAcs = aam.findAll();
            for (int i = 0; i < allAcs.size(); i++) {
                Log.i(tag, i + ": " + allAcs.get(i));
            }
        } catch (Exception e) {
            Log.i(tag, "Error en onCreate: " + e);
            Toast.makeText(this, "Error checkeando en las acciones de debug", Toast.LENGTH_LONG).show();
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


}
