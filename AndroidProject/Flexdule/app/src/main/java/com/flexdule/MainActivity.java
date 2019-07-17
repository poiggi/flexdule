package com.flexdule;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.flexdule.Core.CU;
import com.flexdule.model.dtos.Activity;
import com.flexdule.model.dtos.ActivityVars;
import com.flexdule.model.sqlite.AppDataBase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.LayoutManager layoutManager;

    AppDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<Activity> acs = new ArrayList<>();

        Activity ac1 = new Activity();
        ac1.setColor("#ff0000");
        ac1.setName("Ac test 1");
        ac1.setPositionInSchedule(1);
        ActivityVars ac1FinalVars = new ActivityVars();
        ac1FinalVars.setSn(CU.hourToDur("9:00"));
        ac1FinalVars.setSx(CU.hourToDur("10:00"));
        ac1FinalVars.setDn(CU.hourToDur("1:00"));
        ac1FinalVars.setDx(CU.hourToDur("2:00"));
        ac1FinalVars.setFn(CU.hourToDur("15:00"));
        ac1.setFinalVars(ac1FinalVars);
        acs.add(ac1);

        Activity ac2 = new Activity();
        ac2.setColor("#00ff00");
        ac2.setName("Ac test 2");
        ac2.setPositionInSchedule(2);

        ActivityVars ac2ConfigVars = new ActivityVars();
        ac2ConfigVars.setDn(CU.hourToDur("2:00"));
        ac2.setConfigVars(ac2ConfigVars);
        ActivityVars ac2FinalVars = new ActivityVars();
        ac2FinalVars.setSn(CU.hourToDur("10:00"));
        ac2FinalVars.setSx(CU.hourToDur("11:00"));
        ac2FinalVars.setDn(CU.hourToDur("2:00"));
        ac2FinalVars.setDx(CU.hourToDur("2:00"));
        ac2FinalVars.setDx(CU.hourToDur("2:00"));
        ac2FinalVars.setFn(CU.hourToDur("16:00"));
        ac2FinalVars.setFx(CU.hourToDur("17:00"));
        ac2.getConfigVars().setFx(CU.hourToDur("17:00"));

        ac2.setFinalVars(ac2FinalVars);
        acs.add(ac2);

        Activity ac3 = new Activity();
        ac3.setColor("#0000ff");
        ac3.setName("Ac test 3");
        ac3.setPositionInSchedule(3);
        ActivityVars ac3FinalVars = new ActivityVars();
        ac3FinalVars.setSn(CU.hourToDur("11:00"));
        ac3.getConfigVars().setSn(CU.hourToDur("11:00"));
        ac3FinalVars.setSx(CU.hourToDur("12:00"));
        ac3FinalVars.setDn(null);
        ac3FinalVars.setDx(null);
        ac3FinalVars.setFn(CU.hourToDur("18:00"));
        ac3FinalVars.setFx(CU.hourToDur("18:00"));
        ac3.setFinalVars(ac3FinalVars);
        acs.add(ac3);

        acs.add(ac2);
        acs.add(ac2);
        acs.add(ac2);
        acs.add(ac2);
        acs.add(ac2);

        MainActivityAdapter mAdapter = new MainActivityAdapter(acs);
        recyclerView.setAdapter(mAdapter);


    }

    public void onClickMenu(View v){
        Toast.makeText(this, "click menu", Toast.LENGTH_SHORT).show();
    }

    public void onClickChange(View v){
        Toast.makeText(this, "click change", Toast.LENGTH_SHORT).show();
    }

    public void onClickEdit(View v){
        Toast.makeText(this, "click edit", Toast.LENGTH_SHORT).show();
    }

    public void onClickAdd(View v){
        Toast.makeText(this, "click add", Toast.LENGTH_SHORT).show();
    }


}
