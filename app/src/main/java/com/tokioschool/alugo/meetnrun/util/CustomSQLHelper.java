package com.tokioschool.alugo.meetnrun.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tokioschool.alugo.meetnrun.activities.controllers.UserController;
import com.tokioschool.alugo.meetnrun.model.Contracts;

public class CustomSQLHelper extends SQLiteOpenHelper {

    static String name = "meetnrun.db";
    static int DB_Version = 1;

    public CustomSQLHelper(Context context){
        super(context, name, null, DB_Version);
    }


    /*version 1*/

    String userCreationStr = "CREATE TABLE User (user_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "name TEXT, surname TEXT, password TEXT, phone TEXT, email TEXT, photo BLOB, professional_id INTEGER, schedule BLOB," +
            "FOREIGN KEY (professional_id) REFERENCES User (user_id));";
    String groupCreationStr = "CREATE TABLE UserGroup (group_id INTEGER PRIMARY KEY  AUTOINCREMENT NOT NULL, " +
            "name TEXT, description TEXT, photo BLOB, professional_id INTEGER, FOREIGN KEY (professional_id) REFERENCES User (user_id) );";
    String groupUserCreationStr = "CREATE TABLE GroupUser (group_id INTEGER , " +
            "user_id INTEGER, FOREIGN KEY (group_id) REFERENCES UserGroup(group_id), FOREIGN KEY (user_id) REFERENCES User (user_id));";
    String availablePeriodCreationStr = "CREATE TABLE AvailablePeriod (period_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "day INTEGER, beginTime REAL, endTime REAL, user_id INTEGER, FOREIGN KEY (user_id) REFERENCES User (user_id));";
    String AppointmentCreationStr = "CREATE TABLE Appointment (appointment_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "professional_id INTEGER, user_id INTEGER, period_id INTEGER, status INTEGER, FOREIGN KEY (professional_id) REFERENCES User(user_id)," +
            "FOREIGN KEY (user_id) REFERENCES User(user_id), FOREIGN KEY (period_id) REFERENCES AvailablePeriod (period_id));";

    String NotificationCreationStr = "CREATE TABLE Notification (notification_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "sender_id INTEGER, receiver_id INTEGER, message TEXT, seen INTEGER, type TEXT, appointment_id INTEGER," +
            " FOREIGN KEY (sender_id) REFERENCES User(user_id), FOREIGN KEY (receiver_id) REFERENCES User(user_id)," +
            "FOREIGN KEY (appointment_id) REFERENCES Appointment (appointment_id));";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(userCreationStr);
        db.execSQL(groupCreationStr);
        db.execSQL(groupUserCreationStr);
        db.execSQL(availablePeriodCreationStr);
        db.execSQL(AppointmentCreationStr);
        db.execSQL(NotificationCreationStr);

        ContentValues values = new ContentValues();

        // Pares clave-valor
        values.put(Contracts.UserEntry.NAME, "sa");
        values.put(Contracts.UserEntry.PASSWORD, "sa");
            values.put(Contracts.UserEntry.SURNAME, "sa");
            values.put(Contracts.UserEntry.PHONE, "sa");
            values.put(Contracts.UserEntry.EMAIL, "a@a.com");
            values.put(Contracts.UserEntry.SCHEDULE, UserController.initialScheduleByDay);

        db.insert(Contracts.UserEntry.TABLE_NAME, null, values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = oldVersion; oldVersion<newVersion;i++){
            int versionToUpdate = i+1;
            switch (versionToUpdate){
                default:
                    return;
            }
        }
    }

}
