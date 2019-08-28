package com.flexdule.android.control.sub;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flexdule.R;
import com.flexdule.android.control.MainActivity;
import com.flexdule.android.control.ScheduleEditActivity;
import com.flexdule.android.manager.AndroidCookieAccessManager;
import com.flexdule.android.util.AK;
import com.flexdule.core.dtos.Cookie;
import com.flexdule.core.dtos.Schedule;
import com.flexdule.core.manager.CookieAccesManager;
import com.flexdule.core.util.K;

import java.util.ArrayList;
import java.util.List;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleListViewHolder> {
    private static final String tag = ScheduleListAdapter.class.getSimpleName();

    private List<Schedule> items;

    public ScheduleListAdapter(List<Schedule> items) {
        if (items != null) {
            this.items = items;
        } else {
            items = new ArrayList<>();
        }
    }

    @Override
    public ScheduleListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_schedule_list,
                parent, false);
        return new ScheduleListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleListViewHolder h, int position) {
        Schedule schedule = items.get(position);

        // Dto
        h.schedule = schedule;

        // Nombre
        h.nameView.setText(schedule.getName());

        // Color
        GradientDrawable gradientDrawable = (GradientDrawable) h.card.getBackground().mutate();
        gradientDrawable.setColor(Color.parseColor("#" + schedule.getColor()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    protected static class ScheduleListViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        ImageView editButtonView;
        ConstraintLayout card;
        Schedule schedule;

        protected ScheduleListViewHolder(View v) {
            super(v);
            nameView = v.findViewById(R.id.name);
            editButtonView = v.findViewById(R.id.editButton);
            card = v.findViewById(R.id.card);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCardClick();
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
            Log.i(tag, "DONE onCardClick(). schedule= " + schedule);

            try {
                AndroidCookieAccessManager cooM = new AndroidCookieAccessManager(card.getContext());

                cooM.saveCooUsingSchedule(schedule.getIdSchedule());
                Intent intent = new Intent(card.getContext(), MainActivity.class);
                card.getContext().startActivity(intent);

            } catch (Exception e) {
                Log.e(tag, "Error in onCardClick: " + e);
                e.printStackTrace();
            }
        }

        public void onEditClick() {
            Log.i(tag, "BEGIN onClickEdit()");

            Intent intent = new Intent(card.getContext(), ScheduleEditActivity.class);
            intent.putExtra(AK.KEY_ID_SCHEDULE, schedule.getIdSchedule());
            card.getContext().startActivity(intent);
        }
    }
}
