package com.flexdule.model.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteHelper extends SQLiteOpenHelper {
    private static final String TAG = SqliteHelper.class.getName();

    private static final String DATABASE_NAME = "FlexduleDB";
    private static final int DATABASE_VERSION_1 = 1;

    private static final String CREATE_TABLE_SCHEDULE = "CREATE TABLE Schedule (\n" +
            "  idSchedule INTEGER  NOT NULL  ,\n" +
            "  Name TEXT  NOT NULL  ,\n" +
            "  Color TEXT      ,\n" +
            "PRIMARY KEY(idSchedule));";

    private static final String CREATE_TABLE_COOKIE = "CREATE TABLE Cookie (\n" +
            "  idCookie INTEGER  NOT NULL  ,\n" +
            "  label TEXT    ,\n" +
            "  name TEXT  NOT NULL  ,\n" +
            "  value TEXT      ,\n" +
            "PRIMARY KEY(idCookie)  );";

    private static final String CREATE_TABLE_ACTIVITY = "CREATE TABLE Activity (\n" +
            "  idActivity INTEGER  NOT NULL  ,\n" +
            "  Schedule_idSchedule INTEGER  NOT NULL  ,\n" +
            "  Name TEXT    ,\n" +
            "  Color TEXT    ,\n" +
            "  sn TEXT    ,\n" +
            "  sx TEXT    ,\n" +
            "  dn TEXT    ,\n" +
            "  dx TEXT    ,\n" +
            "  fn TEXT    ,\n" +
            "  fx TEXT    ,\n" +
            "  positionInSchedule INTEGER      ,\n" +
            "PRIMARY KEY(idActivity)  ,\n" +
            "  FOREIGN KEY(Schedule_idSchedule)\n" +
            "    REFERENCES Schedule(idSchedule)\n" +
            "      ON DELETE SET DEFAULT\n" +
            "      ON UPDATE SET DEFAULT);";


    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION_1);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        String create =
                CREATE_TABLE_SCHEDULE +
                        CREATE_TABLE_COOKIE +
                        CREATE_TABLE_ACTIVITY;
        database.execSQL(create);
    }

    // Method is called during an upgrade of the database,
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(SqliteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        onCreate(database);
    }
}
