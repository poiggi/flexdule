package com.flexdule.android.control;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flexdule.R;
import com.flexdule.core.util.CK;
import com.flexdule.core.util.CU;
import com.flexdule.core.dtos.Activity;
import com.flexdule.core.dtos.NX;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.MainActivityViewHolder> {

    private List<Activity> items;
    private AdapterView.OnItemClickListener onItemClickListener;

    public MainActivityAdapter(List<Activity> items) {

        if (items != null) {
            this.items = items;
        } else {
            items = new ArrayList<>();
        }
    }

    @Override
    public MainActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card_activity, parent, false);
        return new MainActivityViewHolder(v);
    }

    public void updateData(ArrayList<Activity> viewModels) {
        items.clear();
        items.addAll(viewModels);
        notifyDataSetChanged();
    }

    public void addItem(int position, Activity viewModel) {
        items.add(position, viewModel);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(MainActivityViewHolder h, int position) {
        Context ctxt = h.cardView.getContext();
        Activity ac = items.get(position);

        // Dto de Actividad
        h.activity = ac;

        // Nombre
        h.nameView.setText(ac.getName());

        // Color
        h.cardView.setCardBackgroundColor(Color.parseColor("#"+ac.getColor()));

        // Inicio
        bindDurationPairsToLayout(ctxt, ac.getFinalVars().getS(), ac.getConfigVars().getS(), h.textS1, h.textS2, CK.DISPLAY_HOUR);
        // Duración
        bindDurationPairsToLayout(ctxt, ac.getFinalVars().getD(), ac.getConfigVars().getD(), h.textD1, h.textD2, CK.DISPLAY_STRING);
        // Finalización
        bindDurationPairsToLayout(ctxt, ac.getFinalVars().getF(), ac.getConfigVars().getF(), h.textF1, h.textF2, CK.DISPLAY_HOUR);

    }

    public void bindDurationPairsToLayout(Context ctxt, NX fin, NX conf, TextView tv1, TextView tv2, int displayMode) {

        boolean bold = false;

        if (fin.getN() != null || fin.getX() != null) {

            if (fin.getN() != null && fin.getN().equals(fin.getX())) {
                // Si las dos variables son iguales, se muestra solo una etiqueta

                if (fin.getN().equals(conf.getN()) && fin.getX().equals(conf.getX())) bold = true;
                bindDurationToLabel(fin.getN(), tv1, displayMode, bold);
                tv2.setVisibility(View.GONE);

            } else {
                // Se muestran las dos etiquetas

                if (fin.getN() != null && fin.getN().equals(conf.getN())) bold = true;
                else bold = false;
                bindDurationToLabel(fin.getN(), tv1, displayMode, bold);

                if (fin.getX() != null && fin.getX().equals(conf.getX())) bold = true;
                else bold = false;
                bindDurationToLabel(fin.getX(), tv2, displayMode, bold);

            }
        } else {
            // Solo una etiqueta vacía
            bold = true;
            bindDurationToLabel(null, tv1, displayMode, bold);
            tv2.setVisibility(View.GONE);
        }
    }

    public void bindDurationToLabel(Duration dur, TextView tv, int displayMode, boolean bold) {
        String s;
        int align = View.TEXT_ALIGNMENT_TEXT_END;
        tv.setVisibility(View.VISIBLE);

        if (dur != null) {
            if (displayMode == CK.DISPLAY_STRING) {
                s = CU.durToString(dur);
                align = View.TEXT_ALIGNMENT_CENTER;
            } else {
                s = CU.durToHour(dur);
            }
        } else {
            align = View.TEXT_ALIGNMENT_CENTER;
            s = "-";
        }

        if (bold) {
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        }

        tv.setText(s);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setTextAlignment(align);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected static class MainActivityViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        ImageView actionButtonView;
        ImageView editButtonView;
        CardView cardView;
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
            cardView = (CardView) v.findViewById(R.id.card);
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
            Toast.makeText(cardView.getContext(), "clic en card " + getAdapterPosition() + " de " + activity.getName(), Toast.LENGTH_SHORT).show();
        }

        public void onActionClick() {
            Toast.makeText(cardView.getContext(), "clic en action " + getAdapterPosition() + " de " + activity.getName(), Toast.LENGTH_SHORT).show();
        }

        public void onEditClick() {
            Toast.makeText(cardView.getContext(), "clic en edit " + getAdapterPosition() + " de " + activity.getName(), Toast.LENGTH_SHORT).show();
        }

    }

}
