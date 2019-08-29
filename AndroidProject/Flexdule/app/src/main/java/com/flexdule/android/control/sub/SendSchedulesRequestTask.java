package com.flexdule.android.control.sub;

import android.os.AsyncTask;
import android.util.Log;

import com.flexdule.android.util.Client;

import java.util.List;

import flexdule.core.dtos.ScheduleWithActivities;

public class SendSchedulesRequestTask extends AsyncTask<String, Void, Boolean> {
    private static final String tag = SendSchedulesRequestTask.class.getSimpleName();

    SendSchedulesRequestTaskListener listener;
    List<ScheduleWithActivities> schedules;

    public SendSchedulesRequestTask(SendSchedulesRequestTaskListener listener, List<ScheduleWithActivities> schedules) {
        this.listener = listener;
        this.schedules = schedules;
    }

    @Override
    protected Boolean doInBackground(String... ips) {
        Log.i(tag, "BEGIN doInBackground().");
        boolean finish = false;
        try {
            Client client = new Client(ips[0], 4400);
            client.sendSchedulesRequest(schedules);
            finish = true;
        } catch (Exception e) {
            Log.e(tag, "Error in doInBackground(): " + e);
        }
        Log.i(tag, "END doInBackground(). finish= " + finish);
        return finish;
    }

    protected void onPostExecute(Boolean result) {
        Log.i(tag, "onPostExecute(). result=" + result);
        if (listener != null) listener.onSendSchedulesRequest(result);
    }

    public interface SendSchedulesRequestTaskListener {
        public void onSendSchedulesRequest(Boolean result);
    }
}