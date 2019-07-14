package com.flexdule;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.flexdule.Core.U;
import com.flexdule.model.Activity;
import com.flexdule.model.ActivityVars;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Activity> acs = new ArrayList<>();

        Activity ac1 = new Activity();
        ac1.setColor(Color.parseColor("#ff0000"));
        ac1.setName("Ac test 1");
        ac1.setPositionInSchedule(1);
        ActivityVars ac1FinalVars = new ActivityVars();
        ac1FinalVars.setIn(U.hourToDur("9:00"));
        ac1FinalVars.setIx(U.hourToDur("10:00"));
        ac1FinalVars.setDn(U.hourToDur("1:00"));
        ac1FinalVars.setDx(U.hourToDur("2:00"));
        ac1FinalVars.setFn(U.hourToDur("15:00"));
        ac1FinalVars.setFx(U.hourToDur("16:00"));
        ac1.setFinalVars(ac1FinalVars);
        acs.add(ac1);

        Activity ac2 = new Activity();
        ac2.setColor(Color.parseColor("#00ff00"));
        ac2.setName("Ac test 2");
        ac2.setPositionInSchedule(2);
        ActivityVars ac2FinalVars = new ActivityVars();
        ac2FinalVars.setIn(U.hourToDur("10:00"));
        ac2FinalVars.setIx(U.hourToDur("11:00"));
        ac2FinalVars.setDn(U.hourToDur("2:00"));
        ac2FinalVars.setDx(U.hourToDur("2:00"));
        ac2FinalVars.setFn(U.hourToDur("16:00"));
        ac2FinalVars.setFx(U.hourToDur("17:00"));
        ac2.setFinalVars(ac2FinalVars);
        acs.add(ac2);

        Activity ac3 = new Activity();
        ac3.setColor(Color.parseColor("#0000ff"));
        ac3.setName("Ac test 3");
        ac3.setPositionInSchedule(3);
        ActivityVars ac3FinalVars = new ActivityVars();
        ac3FinalVars.setIn(U.hourToDur("11:00"));
        ac3FinalVars.setIx(U.hourToDur("12:00"));
        ac3FinalVars.setDn(null);
        ac3FinalVars.setDx(null);
        ac3FinalVars.setFn(U.hourToDur("18:00"));
        ac3FinalVars.setFx(U.hourToDur("18:00"));
        ac3.setFinalVars(ac3FinalVars);
        acs.add(ac3);

        ArrayList<String> actividades = new ArrayList<>();
        actividades.add("Una");
        actividades.add("Dos");
        actividades.add("Tres");
        MainActivityAdapter mAdapter = new MainActivityAdapter(acs);
        recyclerView.setAdapter(mAdapter);

    }

    public void test (View v){
        Toast.makeText(this, "test clic!!", Toast.LENGTH_SHORT).show();
    }
}
