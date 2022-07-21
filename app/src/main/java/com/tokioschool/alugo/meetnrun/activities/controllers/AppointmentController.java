package com.tokioschool.alugo.meetnrun.activities.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.model.Appointment;
import com.tokioschool.alugo.meetnrun.model.Contracts;
import com.tokioschool.alugo.meetnrun.model.Notification;
import com.tokioschool.alugo.meetnrun.model.User;
import com.tokioschool.alugo.meetnrun.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class AppointmentController extends BaseController {

    private final NotificationController nc;

    public AppointmentController(Context context) {
        super(context);
        nc = new NotificationController(context);
    }

    public boolean rejectAppointment(int appointment_id){
       return changeAppointmentStatus(appointment_id, Appointment.Status.CANCELLED);
    }

    public boolean confirmAppointment(int appointment_id){
        return changeAppointmentStatus(appointment_id, Appointment.Status.CONFIRMED);
    }

    private boolean changeAppointmentStatus(int appointment_id, Appointment.Status status){

        if (appointment_id == -1 || status == null){
            return false;
        }

        SQLiteDatabase db = sqlHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Contracts.AppointmentEntry.STATUS, status.ordinal());
        int updated = db.update(Contracts.AppointmentEntry.TABLE_NAME, contentValues,
                String.format("%s = ?", Contracts.AppointmentEntry.ID), new String[]{String.valueOf(appointment_id)});

        if (updated == 0){
            db.close();
            return false;
        }

        db.close();
        return true;
    }


    public long requestAppointment(User professional, User pacient, int day, int hour){
        long appointment_id = createAppointment(professional, pacient, day, hour, Appointment.Status.REQUESTED);
        if (appointment_id == -1){
            return -1;
        }
        
        nc.createNotification(pacient.getId(), professional.getId(),
                String.format(context.getString(R.string.description_notification_needsConfirmation), pacient.getName(), Utils.getDayByInt(day).name(), hour), Notification.Type.NEED_CONFIRMATION, (int) appointment_id);
        return appointment_id;
    }

    public long createAppointment(User professional, User pacient, int day, int hour){

        long appointment_id = createAppointment(professional, pacient, day, hour, Appointment.Status.CONFIRMED);
        if (appointment_id == -1){
            return -1;
        }
        nc.createNotification(professional.getId(), pacient.getId(),
                String.format(context.getString(R.string.description_notification_created), professional.getName(), Utils.getDayByInt(day).name(), hour), Notification.Type.CREATED, (int) appointment_id);
        return  appointment_id;
    }

    private long createAppointment(User professional, User pacient, int day, int hour, Appointment.Status status){

        if (professional == null ||
        pacient == null ||
        day == -1 ||
        hour == -1 ||
        status == null){
            return -1;
        }

        SQLiteDatabase db = sqlHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        // Pares clave-valor
        values.put(Contracts.AppointmentEntry.PROFESSIONAL_ID, professional.getId());
        values.put(Contracts.AppointmentEntry.USER_ID, pacient.getId());
        values.put(Contracts.AppointmentEntry.DAY, day);
        values.put(Contracts.AppointmentEntry.HOUR, hour);
        values.put(Contracts.AppointmentEntry.STATUS, status.ordinal());

        long id = db.insert(Contracts.AppointmentEntry.TABLE_NAME, null, values);

        db.close();
        return id;
    }

    public Appointment getAppointment(int appointment_id){

        Appointment result = null;
        if (appointment_id == -1){
            return result;
        }

        SQLiteDatabase db = sqlHelper.getReadableDatabase();


        Cursor cursor = db.query(Contracts.AppointmentEntry.TABLE_NAME, Contracts.AppointmentEntry.Columns,
                Contracts.AppointmentEntry.ID + " LIKE ?", new String[]{String.valueOf(appointment_id)},
                null, null, null);

        if (cursor.moveToNext()){

            int idi = cursor.getColumnIndex(Contracts.AppointmentEntry.ID);
            int user_idi = cursor.getColumnIndex(Contracts.AppointmentEntry.USER_ID);
            int professional_idi = cursor.getColumnIndex(Contracts.AppointmentEntry.PROFESSIONAL_ID);
            int dayi = cursor.getColumnIndex(Contracts.AppointmentEntry.DAY);
            int houri = cursor.getColumnIndex(Contracts.AppointmentEntry.HOUR);
            int statusi = cursor.getColumnIndex(Contracts.AppointmentEntry.STATUS);

            int id = cursor.getInt(idi);
            int user_id = cursor.getInt(user_idi);
            int professional_id = cursor.getInt(professional_idi);
            int day = cursor.getInt(dayi);
            int hour = cursor.getInt(houri);
            int status = cursor.getInt(statusi);

            result = new Appointment(id, professional_id, user_id, day, hour, Utils.getAppointmentStatusByInt(status));
        }

        db.close();
        return result;
    }


    public List<Appointment> getAppointments(User user){
        List<Appointment> result = new ArrayList<>();
        Cursor cursor = null;
        if (user == null){
            return result;
        }

        SQLiteDatabase db = sqlHelper.getReadableDatabase();

        if (user.isProfessional()){
            cursor = db.query(Contracts.AppointmentEntry.TABLE_NAME, Contracts.AppointmentEntry.Columns,
                    Contracts.AppointmentEntry.PROFESSIONAL_ID + " LIKE ?", new String[]{String.valueOf(user.getId())},
                    null, null, null);
        } else {
            cursor = db.query(Contracts.AppointmentEntry.TABLE_NAME, Contracts.AppointmentEntry.Columns,
                    Contracts.AppointmentEntry.USER_ID + " LIKE ?", new String[]{String.valueOf(user.getId())},
                    null, null, null);
        }

        while (cursor.moveToNext()){

            int idi = cursor.getColumnIndex(Contracts.AppointmentEntry.ID);
            int user_idi = cursor.getColumnIndex(Contracts.AppointmentEntry.USER_ID);
            int professional_idi = cursor.getColumnIndex(Contracts.AppointmentEntry.PROFESSIONAL_ID);
            int dayi = cursor.getColumnIndex(Contracts.AppointmentEntry.DAY);
            int houri = cursor.getColumnIndex(Contracts.AppointmentEntry.HOUR);
            int statusi = cursor.getColumnIndex(Contracts.AppointmentEntry.STATUS);

            int id = cursor.getInt(idi);
            int user_id = cursor.getInt(user_idi);
            int professional_id = cursor.getInt(professional_idi);
            int day = cursor.getInt(dayi);
            int hour = cursor.getInt(houri);
            int status = cursor.getInt(statusi);

            Appointment elem = new Appointment(id, professional_id, user_id, day, hour, Utils.getAppointmentStatusByInt(status));

            result.add(elem);
        }

        cursor.close();
        db.close();

        return result;
    }

    public boolean checkAppointment(User user, int day, int hour){

        SQLiteDatabase db = sqlHelper.getReadableDatabase();

        //Comprobamos si existe el appointment del paciente

        if (user.isProfessional()){

            Cursor cursor = db.query(Contracts.AppointmentEntry.TABLE_NAME,
                    Contracts.AppointmentEntry.Columns,
                    String.format("%s = ? AND %s = ? AND %s = ? AND %s = ?", Contracts.AppointmentEntry.PROFESSIONAL_ID, Contracts.AppointmentEntry.DAY, Contracts.AppointmentEntry.HOUR, Contracts.AppointmentEntry.STATUS),
                    new String[]{String.valueOf(user.getId()), String.valueOf(day),String.valueOf(hour), String.valueOf(Appointment.Status.CONFIRMED.ordinal())},
                    null,null,null);

            if (cursor.getCount() != 0){
                db.close();
                return false;
            }
        } else {

            Cursor cursor = db.query(Contracts.AppointmentEntry.TABLE_NAME,
                    Contracts.AppointmentEntry.Columns,
                    String.format("%s = ? AND %s = ? AND %s = ? AND %s = ?", Contracts.AppointmentEntry.USER_ID, Contracts.AppointmentEntry.DAY, Contracts.AppointmentEntry.HOUR, Contracts.AppointmentEntry.STATUS),
                    new String[]{String.valueOf(user.getId()), String.valueOf(day),String.valueOf(hour), Appointment.Status.CONFIRMED.name()},
                    null,null,null);

            if (cursor.getCount() != 0){
                db.close();
                return false;
            }

            //Comprobamos si existe el appointment del paciente

            cursor = db.query(Contracts.AppointmentEntry.TABLE_NAME,
                    Contracts.AppointmentEntry.Columns,
                    String.format("%s = ? AND %s = ? AND %s = ? AND %s = ?", Contracts.AppointmentEntry.PROFESSIONAL_ID, Contracts.AppointmentEntry.DAY, Contracts.AppointmentEntry.HOUR, Contracts.AppointmentEntry.STATUS),
                    new String[]{String.valueOf(user.getProfessional_id()), String.valueOf(day),String.valueOf(hour), Appointment.Status.CONFIRMED.name()},
                    null,null,null);

            if (cursor.getCount() != 0){
                db.close();
                return false;
            }
        }

        db.close();
        return true;
    }


}
