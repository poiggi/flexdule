package com.flexdule.android.manager;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import com.flexdule.android.model.sqlite.FlexduleDataBase;
import com.flexdule.android.model.sqlite.SqliteHelper;

public class AccessContext {
    private static final String tag = AccessContext.class.getSimpleName();

    private static FlexduleDataBase flexduleDataBase;

    public static void createDataBase(Context context) throws Exception {
        try {
            if (flexduleDataBase == null) {
                SqliteHelper helper = new SqliteHelper(context);
                flexduleDataBase = Room.databaseBuilder(context,
                        FlexduleDataBase.class, "flexdule-database").allowMainThreadQueries().build();
            }
        }catch (Exception e){
            Log.e(tag,"Error in createDataBase: "+e);
        }
    }

    public static FlexduleDataBase getDataBase() {
        return flexduleDataBase;
    }

}
