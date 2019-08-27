package com.flexdule.android.control.sub;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flexdule.R;
import com.flexdule.android.control.ActivityEditActivity;
import com.flexdule.android.util.AK;
import com.flexdule.core.dtos.Activity;
import com.flexdule.core.dtos.NX;
import com.flexdule.core.util.K;
import com.flexdule.core.util.Time;

import java.util.ArrayList;
import java.util.List;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.MainActivityViewHolder> {
    private static final String tag = MainActivityAdapter.class.getSimpleName();

    private List<Activity> items;

    public MainActivityAdapter(List<Activity> items) {
        if (items != null) {
            this.items = items;
        } else {
            items = new ArrayList<>();
        }
    }

    @Override
    public MainActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_main_activity,
                parent, false);
        return new MainActivityViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MainActivityViewHolder h, int position) {
        Context ctxt = h.card.getContext();
        Activity ac = items.get(position);

        // Dto de Actividad
        h.activity = ac;

        // Nombre
        h.nameView.setText(ac.getName());

        // Color
        GradientDrawable gradientDrawable = (GradientDrawable) h.card.getBackground().mutate();
        gradientDrawable.setColor(Color.parseColor("#" + ac.getColor()));

        // Inicio
        bindDurationPairsToLayout(ctxt, ac.getFinalVars().getS(), ac.getConfigVars().getS(),
                h.textS1, h.textS2, K.DISPLAY_HOUR);
        // Duración
        bindDurationPairsToLayout(ctxt, ac.getFinalVars().getD(), ac.getConfigVars().getD(),
                h.textD1, h.textD2, K.DISPLAY_TEXT);
        // Finalización
        bindDurationPairsToLayout(ctxt, ac.getFinalVars().getF(), ac.getConfigVars().getF(),
                h.textF1, h.textF2, K.DISPLAY_HOUR);

    }

    public void bindDurationPairsToLayout(Context ctxt, NX fin, NX conf, TextView tv1,
                                          TextView tv2, int displayMode) {

        boolean bold = false;

        if (fin.getN() != null || fin.getX() != null) {

            boolean finalVarsEqual = fin.getN() != null && fin.getN().equals(fin.getX());
            boolean configVarsEqual = conf.getN() != null && conf.getN().equals(conf.getX());
            boolean configVarsNull = conf.getN() == null && conf.getX() == null;
            if ((finalVarsEqual && configVarsNull) || configVarsEqual) {

                if (fin.getN() != null && fin.getN().equals(conf.getN())
                        && fin.getX() != null && fin.getX().equals(conf.getX())) bold = true;
                bindDurationToLabel(fin.getN(), tv1, displayMode, bold);
                tv2.setVisibility(View.GONE);

            } else {
                // Se muestran las dos etiquetas

                boolean nBold;
                if (fin.getN() != null && fin.getN().equals(conf.getN())) nBold = true;
                else nBold = false;

                boolean xBold;
                if (fin.getX() != null && fin.getX().equals(conf.getX())) xBold = true;
                else xBold = false;

                // En caso de que la variable menor sea mayor que la mayor, se invierte
                if(fin.getN() != null && fin.getX()!=null && fin.getN().greaterThan(fin.getX())){
                    Time aux = Time.copy(fin.getX());
                    fin.setX(Time.copy(fin.getN()));
                    fin.setN(aux);
                }

                bindDurationToLabel(fin.getN(), tv1, displayMode, nBold);
                bindDurationToLabel(fin.getX(), tv2, displayMode, xBold);

            }
        } else {
            // Solo una etiqueta vacía
            bold = true;
            bindDurationToLabel(null, tv1, displayMode, bold);
            tv2.setVisibility(View.GONE);
        }
    }

    public void bindDurationToLabel(Time time, TextView tv, int displayMode, boolean bold) {
        String s;
        int align = View.TEXT_ALIGNMENT_TEXT_END;
        tv.setVisibility(View.VISIBLE);

        if (time != null) {
            if (displayMode == K.DISPLAY_TEXT) {
                s = time.formatText();
                align = View.TEXT_ALIGNMENT_CENTER;
            } else {
                s = time.toString();
            }
        } else {
            align = View.TEXT_ALIGNMENT_CENTER;
            s = "-";
        }

        if (bold) {
            tv.setTextColor(ContextCompat.getColor(tv.getContext(), R.color.primary_text_light));
//            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        }

        tv.setText(s);
//        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//        tv.setTextAlignment(align);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected static class MainActivityViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        ImageView actionButtonView;
        ImageView editButtonView;
        ConstraintLayout card;
        Activity activity;

        TextView textS1;
        TextView textS2;
        TextView textD1;
        TextView textD2;
        TextView textF1;
        TextView textF2;

        protected MainActivityViewHolder(View v) {
            super(v);
            nameView = (TextView) v.findViewById(R.id.name);
            actionButtonView = (ImageView) v.findViewById(R.id.actionButton);
            editButtonView = (ImageView) v.findViewById(R.id.editButton);
            card = (ConstraintLayout) v.findViewById(R.id.card);
            textS1 = (TextView) v.findViewById(R.id.textS1);
            textS2 = (TextView) v.findViewById(R.id.textS2);
            textD1 = (TextView) v.findViewById(R.id.textD1);
            textD2 = (TextView) v.findViewById(R.id.textD2);
            textF1 = (TextView) v.findViewById(R.id.textF1);
            textF2 = (TextView) v.findViewById(R.id.textF2);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCardClick();
                }
            });
            actionButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onActionClick();
                }
            });
            editButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onEditClick();
                }
            });
        }

        public void onCardClick() {
            //TODO implementar, de momento sustituye al edit
            Intent intent = new Intent(card.getContext(), ActivityEditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(AK.KEY_SERIALIZED_ACTIVITY, activity);

            intent.putExtras(bundle);
            card.getContext().startActivity(intent);
        }

        public void onActionClick() {
        }

        public void onEditClick() {
            Intent intent = new Intent(card.getContext(), ActivityEditActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(AK.KEY_SERIALIZED_ACTIVITY, activity);

            intent.putExtras(bundle);
            card.getContext().startActivity(intent);
        }
    }

}
