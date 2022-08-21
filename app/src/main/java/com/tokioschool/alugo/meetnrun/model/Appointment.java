package com.tokioschool.alugo.meetnrun.model;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Appointment {

    @IntDef({Status.REQUESTED, Status.CONFIRMED, Status.CANCELLED, Status.MODIFICATION_REQUESTED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status {
        public int REQUESTED = 0;
        int CONFIRMED = 1;
        int CANCELLED = 2;
        int MODIFICATION_REQUESTED = 3;
    }

    public static String getStatusString(@Status int status){
        switch (status){
            case Status.REQUESTED:
                return "REQUESTED";
            case Status.CONFIRMED:
                return "CONFIRMED";
            case Status.CANCELLED:
                return "CANCELLED";
            case Status.MODIFICATION_REQUESTED:
                return "MODIFICATION_REQUESTED";
            default:
                return null;
        }
    }
    
    private int appointment_id;
    private int professional_id;
    private int user_id;
    private int day;
    private int hour;
    @Status
    private int status;


    public Appointment(int appointment_id, int professional_id, int user_id, int day, int hour, @Status int status) {
        this.appointment_id = appointment_id;
        this.professional_id = professional_id;
        this.user_id = user_id;
        this.day = day;
        this.hour = hour;
        this.status = status;
    }

    public int getAppointment_id() {
        return appointment_id;
    }

    public int getProfessional_id() {
        return professional_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public @Status int getStatus() {
        return status;
    }
}
