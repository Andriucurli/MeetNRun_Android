package com.tokioschool.alugo.meetnrun.model;

public class Appointment {

    public enum Status{
        REQUESTED,
        CONFIRMED,
        CANCELED,
        MODIFICATION_REQUESTED
    }


    int appointment_id;
    int professional_id;
    int user_id;
    int day;
    int hour;
    Status status;



}
