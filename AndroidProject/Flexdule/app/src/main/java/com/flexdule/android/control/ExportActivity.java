package com.flexdule.android.control;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.flexdule.R;
import com.flexdule.android.util.AU;
import com.flexdule.android.util.Client;

public class ExportActivity extends AppCompatActivity {
    private static final String tag = ExportActivity.class.getSimpleName();

    EditText exportIp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        Log.i(tag, "==========[ BEGIN onCreate " + tag + " ]==========");

        exportIp = findViewById(R.id.exportIp);
        Log.i(tag, "==========[ END onCreate " + tag + " ]==========");
    }

    public void onClickExport(View view) {
        Log.i(tag, "BEGIN onClickExport()");
        Log.i(tag, "END onClickExport()");
    }

    public void onClickTestConnection(View view) {
        Log.i(tag, "BEGIN onClickTestConnection() exportIp= " + exportIp.getText());
        boolean ok = false;
        try {
            Client client = new Client(exportIp.getText() + "", 4400);
            client.helloRequest();
        } catch (Exception e) {
            Log.e(tag, "Error in onClickTestConnection(): " + e);
        }

        if (ok) {
            AU.toast("Conexión exitosa!", this, 1000);
        } else {
            AU.toast("No se ha podido conectar", this, 1000);
        }

        Log.i(tag, "END onClickTestConnection() ");
    }
}
