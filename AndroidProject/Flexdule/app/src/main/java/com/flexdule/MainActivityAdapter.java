package com.flexdule;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Handler;
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

import com.flexdule.Core.K;
import com.flexdule.Core.U;
import com.flexdule.model.Activity;

import java.time.Duration;
import java.util.ArrayList;

public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.MainActivityViewHolder> implements View.OnClickListener {

    private ArrayList<Activity> items;
    private AdapterView.OnItemClickListener onItemClickListener;

    public MainActivityAdapter(ArrayList<Activity> items) {
        this.items = items;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MainActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_card_activity, parent, false);
        v.setOnClickListener(this);
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

        // Nombre
        h.getNameView().setText(ac.getName());

        // Color
        h.getCardView().setCardBackgroundColor(ac.getColor());

        // Inicio
        bindDurationPairsToLayout(ctxt, ac.getFinalVars().getIn(), ac.getFinalVars().getIx(), h.getLeftLayout(), K.DISPLAY_HOUR);
        // Duración
        bindDurationPairsToLayout(ctxt, ac.getFinalVars().getDn(), ac.getFinalVars().getDx(), h.getMidLayout(), K.DISPLAY_STRING);
        // Finalización
        bindDurationPairsToLayout(ctxt, ac.getFinalVars().getFn(), ac.getFinalVars().getFx(), h.getRightLayout(), K.DISPLAY_HOUR);

    }

    public void bindDurationPairsToLayout(Context ctxt, Duration varN, Duration varX, LinearLayout layout, int displayMode) {

        if (varN != null || varX != null) {
            // Si las dos variables son iguales, se muestra solo una etiqueta
            if (varN != null && varN.equals(varX)) {
                bindDurationToLabel(ctxt, varN, layout, displayMode);
            } else {
                bindDurationToLabel(ctxt, varN, layout, displayMode);
                bindDurationToLabel(ctxt, varX, layout, displayMode);
            }
        } else {
            // vacío
            bindDurationToLabel(ctxt, null, layout, displayMode);
        }
    }

    public void bindDurationToLabel(Context ctxt, Duration dur, LinearLayout layout, int displayMode) {
        TextView tv = new TextView(ctxt);
        String s;
        int align = View.TEXT_ALIGNMENT_TEXT_END;
        if (dur != null) {
            if (displayMode == K.DISPLAY_STRING) {
                s = U.durToString(dur);
                align = View.TEXT_ALIGNMENT_CENTER;
            } else {
                s = U.durToHour(dur);
            }
        } else {
            s = "-";
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


    @Override
    public void onClick(final View v) {
        // Give some time to the ripple to finish the effect
        if (onItemClickListener != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

//                    onItemClickListener.onItemClick(v, (ViewModel) v.getTag());
                }
            }, 0);
        }
    }


    protected static class MainActivityViewHolder extends RecyclerView.ViewHolder {

        private TextView nameView;
        private ImageView actionButtonView;
        private ImageView editButtonView;
        private LinearLayout leftLayout, midLayout, rightLayout;
        private CardView cardView;

        public MainActivityViewHolder(View v) {
            super(v);
            nameView = (TextView) v.findViewById(R.id.name);
            actionButtonView = (ImageView) v.findViewById(R.id.actionButton);
            editButtonView = (ImageView) v.findViewById(R.id.editButton);
            leftLayout = (LinearLayout) v.findViewById(R.id.leftLayout);
            midLayout = (LinearLayout) v.findViewById(R.id.midLayout);
            rightLayout = (LinearLayout) v.findViewById(R.id.rightLayout);
            cardView = (CardView) v.findViewById(R.id.card);
        }

        public interface OnItemClickListener {
            void onItemClick(View view, ViewModel viewModel);
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
    }
}
