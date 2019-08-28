package com.flexdule.android.control;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.flexdule.R;
import com.flexdule.android.manager.AndroidActivityAccessManager;
import com.flexdule.android.util.AK;
import com.flexdule.android.util.AU;
import com.flexdule.android.util.AndroidLog;
import com.flexdule.android.util.AppTimePickerDialog;

import flexdule.core.dtos.Activity;
import flexdule.core.dtos.ActivityVars;
import flexdule.core.dtos.Limits;
import flexdule.core.dtos.NX;
import flexdule.core.model.managers.ActivityAccessManager;
import flexdule.core.model.managers.ScheduleActivitiesManager;
import flexdule.core.util.AppColors;
import flexdule.core.util.CoreLog;
import flexdule.core.util.K;
import flexdule.core.util.Time;

public class ActivityEditActivity extends AppCompatActivity {
    private static final String tag = ActivityEditActivity.class.getSimpleName();

    Activity activity;
    ActivityVars conf;
    Limits limits;

    ActivityAccessManager actM;
    ScheduleActivitiesManager schActM;
    AppColors colors = new AppColors();

    EditText editName;
    Switch flexibleS, flexibleD, flexibleF;
    LinearLayout editLayout;
    CardView cardView;
    LinearLayout optionSn, optionSx, optionDn, optionDx, optionFn, optionFx;
    TextView labelOptionSn, labelOptionSx, labelOptionDn, labelOptionDx, labelOptionFn,
            labelOptionFx;
    TextView ghostSn, ghostSx, ghostDn, ghostDx, ghostFn, ghostFx;


    boolean dataBindFinished;

    /**
     * Se guardan los últimos tiempos establecidos para las variables, diferenciando entre sus
     * formas flexibles y la no flexible, de modo que si se modifica el modo (flexible o no), se
     * restoren los datos. Para evitar incoherencias, solo se guardan los datos del último grupo
     * de variables que se esté operando (S, D o F): si se edita un grupo, se limpian los datos
     * del resto de grupos.
     */
    Time lastS, lastSn, lastSx;
    Time lastD, lastDn, lastDx;
    Time lastF, lastFn, lastFx;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_edit);
        Log.i(tag, "==========[ BEGIN onCreate ]==========");

        initUiVars();

        // Se inician los managers
        try {
            actM = new AndroidActivityAccessManager(getApplicationContext());
            CoreLog coreLog = new AndroidLog(ScheduleActivitiesManager.class.getSimpleName());
            schActM = new ScheduleActivitiesManager(coreLog);
        } catch (Exception e) {
            Log.e(tag, "Error initializing managers, in onCreate(): " + e);
            e.printStackTrace();
        }

        recoverData();
        initFlexibleListeners();
        initDataBind();

        Log.i(tag, "==========[ END onCreate ]==========");
    }

    @Override
    protected void onPause() {
        if (activity != null) saveOnExit();
        super.onPause();
    }

    private void recoverData() {
        Log.i(tag, "BEGIN recoverData()");

        Intent intent = getIntent();
        activity = (Activity) intent.getExtras().getSerializable(AK.KEY_SERIALIZED_ACTIVITY);
        conf = activity.getConfigVars();
        limits = activity.getLimits();

        Log.i(tag, "END recoverData(). activity = " + activity);
    }

    private void initUiVars() {
        Log.i(tag, "BEGIN initUiVars()");
        editName = findViewById(R.id.editName);
        editLayout = findViewById(R.id.editLayout);
        cardView = findViewById(R.id.cardView);
        flexibleS = findViewById(R.id.flexibleS);
        flexibleD = findViewById(R.id.flexibleD);
        flexibleF = findViewById(R.id.flexibleF);
        optionSn = findViewById(R.id.optionSn);
        optionSx = findViewById(R.id.optionSx);
        optionDn = findViewById(R.id.optionDn);
        optionDx = findViewById(R.id.optionDx);
        optionFn = findViewById(R.id.optionFn);
        optionFx = findViewById(R.id.optionFx);
        labelOptionSn = findViewById(R.id.labelOptionSn);
        labelOptionSx = findViewById(R.id.labelOptionSx);
        labelOptionDn = findViewById(R.id.labelOptionDn);
        labelOptionDx = findViewById(R.id.labelOptionDx);
        labelOptionFn = findViewById(R.id.labelOptionFn);
        labelOptionFx = findViewById(R.id.labelOptionFx);
        ghostSn = findViewById(R.id.ghostSn);
        ghostSx = findViewById(R.id.ghostSx);
        ghostDn = findViewById(R.id.ghostDn);
        ghostDx = findViewById(R.id.ghostDx);
        ghostFn = findViewById(R.id.ghostFn);
        ghostFx = findViewById(R.id.ghostFx);
        Log.i(tag, "END initUiVars()");
    }

    private void initDataBind() {
        Log.i(tag, "BEGIN initDataBind()");

        if (activity.getName() != null) editName.setText(activity.getName());

        if (activity.getColor() == null) activity.setColor(AppColors.COLOR_WHITE);
        updateColor();

        // Si N y X coinciden y la noFlexibilidad es viable, se establece
        if ((conf.getSn() != null && conf.getSn().equals(conf.getSx()))
                || (conf.getSn() == null && conf.getSx() == null)
                && schActM.validateDisableFlexible(K.S, activity)) {
            setS(conf.getSn());
            flexibleS.setChecked(false);
        } else {
            setSn(conf.getSn());
            setSx(conf.getSx());
        }

        if ((conf.getDn() != null && conf.getDn().equals(conf.getDx()))
                || (conf.getDn() == null && conf.getDx() == null)
                && schActM.validateDisableFlexible(K.D, activity)) {
            setD(conf.getDn());
            flexibleD.setChecked(false);
        } else {
            setDn(conf.getDn());
            setDx(conf.getDx());
        }

        if ((conf.getFn() != null && conf.getFn().equals(conf.getFx()))
                || (conf.getFn() == null && conf.getFx() == null)
                && schActM.validateDisableFlexible(K.F, activity)) {
            setF(conf.getFn());
            flexibleF.setChecked(false);
        } else {
            setFn(conf.getFn());
            setFx(conf.getFx());
        }

        dataBindFinished = true;

        Log.i(tag, "END initDataBind()");
    }


    private void initFlexibleListeners() {
        Log.i(tag, "BEGIN initFlexibleListeners()");

        // Flexible S
        flexibleS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(tag, "trying to change flexible S to " + isChecked);
                // Si los lasts decada caso son no nulos y válidos, se restablecen. Si no, a null
                if (isChecked) {
                    optionSx.setVisibility(View.VISIBLE);
                    labelOptionSn.setText("Hora de Inicio Mínima");

                    if (lastSn != null) {
                        NX minMaxSn = schActM.calcMinMaxSn(activity, true);
                        if (lastSn.isInRange(minMaxSn.getN(), minMaxSn.getX())) setSn(lastSn);
                        else setSn(null);
                    } else {
                        setSn(null);
                    }

                    if (lastSx != null) {
                        NX minMaxSx = schActM.calcMinMaxSx(activity, true);
                        if (lastSx.isInRange(minMaxSx.getN(), minMaxSx.getX())) setSx(lastSx);
                        else setSx(null);
                    } else {
                        setSx(null);
                    }

                } else {
                    if (validateAndAmendDisableFlexible(K.S)) {
                        optionSx.setVisibility(View.GONE);
                        labelOptionSn.setText("Hora de Inicio");

                        if (lastS != null) {
                            NX minMaxS = schActM.calcNoFlexibleMinMax(K.S, activity);
                            if (lastS.isInRange(minMaxS.getN(), minMaxS.getX())) setS(lastS);
                            else setS(null);
                        } else {
                            setS(null);
                        }
                    }
                }
            }
        });

        // Flexible D
        flexibleD.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(tag, "trying to change flexible D to " + isChecked);
                if (isChecked) {
                    optionDx.setVisibility(View.VISIBLE);
                    labelOptionDn.setText("Duración Mínima");

                    if (lastDn != null) {
                        NX minMaxDn = schActM.calcMinMaxDn(activity, true);
                        if (lastDn.isInRange(minMaxDn.getN(), minMaxDn.getX())) setDn(lastDn);
                        else setDn(null);
                    } else {
                        setDn(null);
                    }

                    if (lastDx != null) {
                        NX minMaxDx = schActM.calcMinMaxDx(activity, true);
                        if (lastDx.isInRange(minMaxDx.getN(), minMaxDx.getX())) setDx(lastDx);
                        else setDx(null);
                    } else {
                        setDx(null);
                    }

                } else {
                    if (validateAndAmendDisableFlexible(K.D)) {
                        optionDx.setVisibility(View.GONE);
                        labelOptionDn.setText("Duración");

                        if (lastD != null) {
                            NX minMaxD = schActM.calcNoFlexibleMinMax(K.D, activity);
                            if (lastD.isInRange(minMaxD.getN(), minMaxD.getX())) setD(lastD);
                            else setD(null);
                        } else {
                            setD(null);
                        }
                    }
                }
            }
        });

        // Flexible F
        flexibleF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(tag, "trying to change flexible F to " + isChecked);
                if (isChecked) {
                    optionFx.setVisibility(View.VISIBLE);
                    labelOptionFn.setText("Hora de Finalización Mínima");

                    if (lastFn != null) {
                        NX minMaxFn = schActM.calcMinMaxFn(activity, true);
                        if (lastFn.isInRange(minMaxFn.getN(), minMaxFn.getX())) setFn(lastFn);
                        else setFn(null);
                    } else {
                        setFn(null);
                    }

                    if (lastFx != null) {
                        NX minMaxFx = schActM.calcMinMaxFx(activity, true);
                        if (lastFx.isInRange(minMaxFx.getN(), minMaxFx.getX())) setFx(lastFx);
                        else setFx(null);
                    } else {
                        setFx(null);
                    }

                } else {
                    if (validateAndAmendDisableFlexible(K.F)) {
                        optionFx.setVisibility(View.GONE);
                        labelOptionFn.setText("Hora de Finalización");

                        if (lastF != null) {
                            NX minMaxF = schActM.calcNoFlexibleMinMax(K.F, activity);
                            if (lastF.isInRange(minMaxF.getN(), minMaxF.getX())) setF(lastF);
                            else setF(null);
                        } else {
                            setF(null);
                        }
                    }
                }
            }
        });

        Log.i(tag, "END initFlexibleListeners()");
    }

    private void saveOnExit() {
        Log.i(tag, "BEGIN saveOnExit(). activity= " + activity);
        boolean save = false;
        Long saved = null;

        activity.setName(editName.getText().toString());

        // Si es nueva actividad sin editar, se desecha
        if (activity.getIdActivity() == null) {
            boolean name = !TextUtils.isEmpty(activity.getName());
            boolean sn = conf.getSn() != null;
            boolean sx = conf.getSx() != null;
            boolean dn = conf.getDn() != null;
            boolean dx = conf.getDx() != null;
            boolean fn = conf.getFn() != null;
            boolean fx = conf.getFx() != null;
            boolean color = !"ffffff".equals(activity.getColor());
            save = name || sn || sx || dn || dx || fn || fx || color;
            if (!save) AU.toast("Actividad vacía desechada", getApplicationContext());
        } else {
            save = true;
        }

        if (save) {
            try {
                saved = actM.saveActivity(activity);
            } catch (Exception e) {
                e.printStackTrace();
                AU.toast("Error al guardar el horario", getApplicationContext());
            }
        }

        Log.i(tag, "BEGIN saveOnExit(). saved= " + saved);
    }


    public void onClickDelete(View view) {
        Log.i(tag, "BEGIN onClickDelete()");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Eliminar actividad?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                try {
                    actM.deleteActivityById(activity.getIdActivity());
                    activity = null; // Se elimina de memoria para que no se guarde al salir
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

    public void onClickBack(View view) {
        finish();
    }

    public void onClickColor(View view) {
        activity.setColor(colors.getNextColor());
        updateColor();
        Log.i(tag, "DONE onClickColor(). color= " + activity.getColor());
    }

    private void updateColor() {
        int color = Color.parseColor("#" + activity.getColor());
        editLayout.setBackgroundColor(color);
        cardView.setCardBackgroundColor(color);
    }

    private void setS(Time time) {
        conf.setSn(Time.copy(time));
        conf.setSx(Time.copy(time));
        timeLabel(ghostSn, time);
        lastS = Time.copy(time);
    }

    private void setSn(Time time) {
        conf.setSn(Time.copy(time));
        timeLabel(ghostSn, time);
        lastSn = Time.copy(time);
    }

    private void setSx(Time time) {
        conf.setSx(Time.copy(time));
        timeLabel(ghostSx, time);
        lastSx = Time.copy(time);
    }

    private void setD(Time time) {
        conf.setDn(Time.copy(time));
        conf.setDx(Time.copy(time));
        timeLabel(ghostDn, time);
        lastD = Time.copy(time);
    }

    private void setDn(Time time) {
        conf.setDn(Time.copy(time));
        timeLabel(ghostDn, time);
        lastDn = Time.copy(time);
    }

    private void setDx(Time time) {
        conf.setDx(Time.copy(time));
        timeLabel(ghostDx, time);
        lastDx = Time.copy(time);
    }

    private void setF(Time time) {
        conf.setFn(Time.copy(time));
        conf.setFx(Time.copy(time));
        timeLabel(ghostFn, time);
        lastF = Time.copy(time);
    }

    private void setFn(Time time) {
        conf.setFn(Time.copy(time));
        timeLabel(ghostFn, time);
        lastFn = Time.copy(time);
    }

    private void setFx(Time time) {
        conf.setFx(Time.copy(time));
        timeLabel(ghostFx, time);
        lastFx = Time.copy(time);
    }

    private void timeLabel(TextView label, Time time) {
        if (time != null) label.setText(time.toString());
        else label.setText("-");
    }

    private boolean validateAndAmendDisableFlexible(String var) {
        Log.i(tag, "BEGIN validateAndAmendDisableFlexible(). var= " + var);

        boolean validated = true;

        if (dataBindFinished) {
            validated = schActM.validateDisableFlexible(var, activity);
            if (!validated) {
                switch (var) {
                    case K.S:
                        flexibleS.setChecked(true);
                        break;
                    case K.D:
                        flexibleD.setChecked(true);
                        break;
                    case K.F:
                        flexibleF.setChecked(true);
                        break;
                }

                AU.toast("Debe ser flexible. Otros tiempos constriñen el cambio.",
                        getApplicationContext(), 2000);
            }
        }
        Log.i(tag, "END validateAndAmendDisableFlexible(). validated= " + validated);
        return validated;
    }


    public void onClickSn(View view) {
        Log.d(tag, "DONE onClickSn()");
        NX minMax;
        if (flexibleS.isChecked()) {
            minMax = schActM.calcMinMaxSn(activity, true);
        } else {
            minMax = schActM.calcNoFlexibleMinMax(K.S, activity);
        }

        AppTimePickerDialog d = new AppTimePickerDialog(ActivityEditActivity.this,
                labelOptionSn.getText().toString(),
                minMax.getN(), minMax.getX(),
                false,
                conf.getSn());
        d.addListener(new AppTimePickerDialog.AppTimePickerListener() {
            @Override
            public void onTimePicked(Time time) {
                if (flexibleS.isChecked()) setSn(time);
                else setS(time);
            }
        });
        d.show();
    }

    public void onClickDn(View view) {
        Log.d(tag, "DONE onClickDn()");
        NX minMax;
        if (flexibleD.isChecked()) {
            minMax = schActM.calcMinMaxDn(activity, true);
        } else {
            minMax = schActM.calcNoFlexibleMinMax(K.D, activity);
        }

        AppTimePickerDialog d = new AppTimePickerDialog(ActivityEditActivity.this,
                labelOptionDn.getText().toString(),
                minMax.getN(), minMax.getX(),
                true,
                conf.getDn());
        d.addListener(new AppTimePickerDialog.AppTimePickerListener() {
            @Override
            public void onTimePicked(Time time) {
                if (flexibleD.isChecked()) setDn(time);
                else setD(time);
            }
        });
        d.show();
    }

    public void onClickFn(View view) {
        Log.d(tag, "DONE onClickFn()");
        NX minMax;
        if (flexibleF.isChecked()) {
            minMax = schActM.calcMinMaxFn(activity, true);
        } else {
            minMax = schActM.calcNoFlexibleMinMax(K.F, activity);
        }

        AppTimePickerDialog d = new AppTimePickerDialog(ActivityEditActivity.this,
                labelOptionFn.getText().toString(),
                minMax.getN(), minMax.getX(),
                false,
                conf.getFn());
        d.addListener(new AppTimePickerDialog.AppTimePickerListener() {
            @Override
            public void onTimePicked(Time time) {
                if (flexibleF.isChecked()) setFn(time);
                else setF(time);
            }
        });
        d.show();
    }

    public void onClickSx(View view) {
        Log.d(tag, "DONE onClickSx()");
        NX minMax = schActM.calcMinMaxSx(activity, true);
        AppTimePickerDialog d = new AppTimePickerDialog(ActivityEditActivity.this,
                labelOptionSx.getText().toString(),
                minMax.getN(), minMax.getX(),
                false,
                conf.getSx());
        d.addListener(new AppTimePickerDialog.AppTimePickerListener() {
            @Override
            public void onTimePicked(Time time) {
                setSx(time);
            }
        });
        d.show();
    }

    public void onClickDx(View view) {
        Log.d(tag, "DONE onClickDx()");
        NX minMax = schActM.calcMinMaxDx(activity, true);
        AppTimePickerDialog d = new AppTimePickerDialog(ActivityEditActivity.this,
                labelOptionDx.getText().toString(),
                minMax.getN(), minMax.getX(),
                true,
                conf.getDx());
        d.addListener(new AppTimePickerDialog.AppTimePickerListener() {
            @Override
            public void onTimePicked(Time time) {
                setDx(time);
            }
        });
        d.show();
    }

    public void onClickFx(View view) {
        Log.d(tag, "DONE onClickFx()");
        NX minMax = schActM.calcMinMaxFx(activity, true);
        AppTimePickerDialog d = new AppTimePickerDialog(ActivityEditActivity.this,
                labelOptionFx.getText().toString(),
                minMax.getN(), minMax.getX(),
                false,
                conf.getFx());
        d.addListener(new AppTimePickerDialog.AppTimePickerListener() {
            @Override
            public void onTimePicked(Time time) {
                setFx(time);
            }
        });
        d.show();
    }

}
