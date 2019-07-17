package com.flexdule;

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

import com.flexdule.Core.CU;
import com.flexdule.Core.CK;
import com.flexdule.model.dtos.Activity;
import com.flexdule.model.dtos.NX;

import java.time.Duration;
import java.util.ArrayList;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.MainActivityViewHolder> {

    private ArrayList<Activity> items;
    private AdapterView.OnItemClickListener onItemClickListener;

    public MainActivityAdapter(ArrayList<Activity> items) {
        this.items = items;
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
        Context ctxt = h.getCardView().getContext();
        Activity ac = items.get(position);

        // Dto de Actividad
        h.setActivity(ac);

        // Nombre
        h.getNameView().setText(ac.getName());

        // Color
        h.getCardView().setCardBackgroundColor(Color.parseColor(ac.getColor()));

        // Inicio
        bindDurationPairsToLayout(ctxt, ac.getFinalVars().getS(), ac.getConfigVars().getS(), h.getLeftLayout(), CK.DISPLAY_HOUR);
        // Duración
        bindDurationPairsToLayout(ctxt, ac.getFinalVars().getD(), ac.getConfigVars().getD(), h.getMidLayout(), CK.DISPLAY_STRING);
        // Finalización
        bindDurationPairsToLayout(ctxt, ac.getFinalVars().getF(), ac.getConfigVars().getF(), h.getRightLayout(), CK.DISPLAY_HOUR);

    }

    public void bindDurationPairsToLayout(Context ctxt, NX fin, NX conf, LinearLayout layout, int displayMode) {

        boolean bold = false;

        if (fin.getN() != null || fin.getX() != null) {
            // Si las dos variables son iguales, se muestra solo una etiqueta
            if (fin.getN() != null && fin.getN().equals(fin.getX())) {

                if (fin.getN().equals(conf.getN()) && fin.getX().equals(conf.getX())) bold = true;
                bindDurationToLabel(ctxt, fin.getN(), layout, displayMode, bold);

            } else {

                if (fin.getN()!=null && fin.getN().equals(conf.getN())) bold = true;
                else bold = false;
                bindDurationToLabel(ctxt, fin.getN(), layout, displayMode, bold);
                if (fin.getX() != null && fin.getX().equals(conf.getX())) bold = true;
                else bold = false;
                bindDurationToLabel(ctxt, fin.getX(), layout, displayMode, bold);

            }
        } else {
            // vacío
            bold = true;
            bindDurationToLabel(ctxt, null, layout, displayMode, bold);
        }
    }

    public void bindDurationToLabel(Context ctxt, Duration dur, LinearLayout layout, int displayMode, boolean bold) {
        TextView tv = new TextView(ctxt);
        String s;
        int align = View.TEXT_ALIGNMENT_TEXT_END;
        if (dur != null) {
            if (displayMode == CK.DISPLAY_STRING) {
                s = CU.durToString(dur);
                align = View.TEXT_ALIGNMENT_CENTER;
            } else {
                s = CU.durToHour(dur);
            }
        } else {
            s = "-";
        }

        if (bold) {
            tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
        }

        tv.setText(s);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        tv.setTextAlignment(align);
        layout.addView(tv);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected static class MainActivityViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;
        private ImageView actionButtonView;
        private ImageView editButtonView;
        private LinearLayout leftLayout, midLayout, rightLayout;
        private CardView cardView;
        private Activity activity;

        public MainActivityViewHolder(View v) {
            super(v);
            nameView = (TextView) v.findViewById(R.id.name);
            actionButtonView = (ImageView) v.findViewById(R.id.actionButton);
            editButtonView = (ImageView) v.findViewById(R.id.editButton);
            leftLayout = (LinearLayout) v.findViewById(R.id.leftLayout);
            midLayout = (LinearLayout) v.findViewById(R.id.midLayout);
            rightLayout = (LinearLayout) v.findViewById(R.id.rightLayout);
            cardView = (CardView) v.findViewById(R.id.card);

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


        public TextView getNameView() {
            return nameView;
        }

        public ImageView getActionButtonView() {
            return actionButtonView;
        }

        public ImageView getEditButtonView() {
            return editButtonView;
        }

        public LinearLayout getLeftLayout() {
            return leftLayout;
        }

        public LinearLayout getMidLayout() {
            return midLayout;
        }

        public LinearLayout getRightLayout() {
            return rightLayout;
        }

        public CardView getCardView() {
            return cardView;
        }

        public Activity getActivity() {
            return activity;
        }

        public void setActivity(Activity activity) {
            this.activity = activity;
        }
    }

}
