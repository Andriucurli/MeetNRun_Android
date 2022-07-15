package com.tokioschool.alugo.meetnrun.activities.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tokioschool.alugo.meetnrun.model.Appointment;
import com.tokioschool.alugo.meetnrun.model.Contracts;
import com.tokioschool.alugo.meetnrun.model.User;

public class AppointmentController extends BaseController {

    public AppointmentController(Context context) {
        super(context);
    }

    public boolean requestAppointment(User professional, User pacient, int day, int hour){
        return createAppointment(professional, pacient, day, hour, Appointment.Status.REQUESTED);
    }

    public boolean createAppointment(User professional, User pacient, int day, int hour){
        return createAppointment(professional, pacient, day, hour, Appointment.Status.CONFIRMED);
    }

    private boolean createAppointment(User professional, User pacient, int day, int hour, Appointment.Status status){

        SQLiteDatabase db = sqlHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        // Pares clave-valor
        values.put(Contracts.AppointmentEntry.PROFESSIONAL_ID, professional.getId());
        values.put(Contracts.AppointmentEntry.USER_ID, pacient.getId());
        values.put(Contracts.AppointmentEntry.DAY, day);
        values.put(Contracts.AppointmentEntry.HOUR, hour);
        values.put(Contracts.AppointmentEntry.STATUS, status.ordinal());

        db.insert(Contracts.AppointmentEntry.TABLE_NAME, null, values);

        return true;
    }



}
