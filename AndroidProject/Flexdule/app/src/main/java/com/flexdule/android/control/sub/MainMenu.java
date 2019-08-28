package com.flexdule.android.control.sub;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.flexdule.R;
import com.flexdule.android.control.ExportActivity;

public class MainMenu {
    public static void show(AppCompatActivity activity, View v) {
        PopupMenu popup = new PopupMenu(activity, v);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.help:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/poiggi/flexdule"));
                        activity.startActivity(browserIntent);
                        return true;
                    case R.id.sync:
                        Intent intent = new Intent(activity, ExportActivity.class);
                        activity.startActivity(intent);
                        return true;
                    default:
                        return false;
                }
            }
        });
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.app_menu, popup.getMenu());
        popup.show();
    }
}
