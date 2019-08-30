package com.flexdule.android.control.sub;

import android.os.AsyncTask;
import android.util.Log;

import com.flexdule.android.connection.Client;

public class HelloRequestTask extends AsyncTask<String, Void, Boolean> {
    private static final String tag = HelloRequestTask.class.getSimpleName();

    HelloRequestTaskListener listener;

    public HelloRequestTask(HelloRequestTaskListener listener) {
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(String... ips) {
        Log.i(tag, "BEGIN doInBackground().");
        boolean ok = false;
        try {
            Client client = new Client(ips[0], 4400);
            ok = client.helloRequest();
        } catch (Exception e) {
            Log.e(tag, "Error in doInBackground(): " + e);
        }
        Log.i(tag, "END doInBackground(). ok= " + ok);
        return ok;
    }

    protected void onPostExecute(Boolean result) {
        Log.i(tag, "onPostExecute(). result=" + result);
        if (listener != null) listener.onHelloRequest(result);
    }

    //
    public interface HelloRequestTaskListener {
        public void onHelloRequest(Boolean result);
    }
}