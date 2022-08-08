package com.tokioschool.alugo.meetnrun.model;

public class Appointment {

    public enum Status{
        REQUESTED,
        CONFIRMED,
        CANCELLED,
        MODIFICATION_REQUESTED
    }
    
    private int appointment_id;
    private int professional_id;
    private int user_id;
    private int day;
    private int hour;
    private Status status;


    public Appointment(int appointment_id, int professional_id, int user_id, int day, int hour, Status status) {
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

    public Status getStatus() {
        return status;
    }
}
