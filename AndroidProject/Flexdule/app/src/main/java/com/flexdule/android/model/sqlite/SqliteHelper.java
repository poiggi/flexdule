package com.flexdule.android.model.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteHelper extends SQLiteOpenHelper {
    private static final String tag = SqliteHelper.class.getName();

    private static final String DATABASE_NAME = "FlexduleDB";
    private static final int DATABASE_VERSION_1 = 1;

    private static final String CREATE_TABLE_SCHEDULE = "CREATE TABLE Schedule (\n" +
            "  idSchedule INTEGER  NOT NULL  ,\n" +
            "  name TEXT  NOT NULL  ,\n" +
            "  color TEXT      ,\n" +
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
            "  name TEXT    ,\n" +
            "  color TEXT    ,\n" +
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

    private static final String INSERT_SCHEDULE_DUMMY = "INSERT INTO Schedule (idSchedule, name, color) VALUES (1,'Horario diario ejemplo','0000FF');" +
            "INSERT INTO Activity (idActivity, Schedule_idSchedule, name, color, sn, sx, dn, dx, fn, fx, positionInSchedule) VAULES (1,1,'Desayunar','FF0000','PT8H','PT9H','PT10M','PT45M',null,null,1);"+
            "INSERT INTO Activity (idActivity, Schedule_idSchedule, name, color, sn, sx, dn, dx, fn, fx, positionInSchedule) VAULES (2,1,'Trabajo','0FF00','PT11H','PT11H',null,null,'PT15H','PT15H45M',2);"+
            "INSERT INTO Activity (idActivity, Schedule_idSchedule, name, color, sn, sx, dn, dx, fn, fx, positionInSchedule) VAULES (2,1,'Comer','00FF00',null,null,'PT30M','PT2H30M',null,null,3);";



    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION_1);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.i(tag,"BEGIN onCreate");
        String create =
                CREATE_TABLE_SCHEDULE +
                CREATE_TABLE_COOKIE +
                CREATE_TABLE_ACTIVITY +
                INSERT_SCHEDULE_DUMMY;
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
