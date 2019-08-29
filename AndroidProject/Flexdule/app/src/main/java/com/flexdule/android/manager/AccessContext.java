package com.flexdule.android.manager;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import com.flexdule.android.model.sqlite.FlexduleDataBase;

public class AccessContext {
    private static final String tag = AccessContext.class.getSimpleName();

    private static FlexduleDataBase flexduleDataBase;

    public static void createDataBaseIfNotExists(Context context) throws Exception {
        try {
            if (flexduleDataBase == null) {
                Log.i(tag, "Creating new Room Database instance.");
                flexduleDataBase = Room.databaseBuilder(context, FlexduleDataBase.class, "flexdule-database").allowMainThreadQueries().build();
            }
        } catch (Exception e) {
            Log.e(tag, "Error in createDataBaseIfNotExists: " + e);
        }
    }

    public static FlexduleDataBase getDataBase() {
        return flexduleDataBase;
    }

}
