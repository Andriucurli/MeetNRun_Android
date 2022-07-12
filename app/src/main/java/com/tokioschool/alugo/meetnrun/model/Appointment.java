package com.tokioschool.alugo.meetnrun.model;

public class Appointment {

    User user = null;
    User professional = null;
    AvailablePeriod period = null;
    AppointmentStatus status;

    enum AppointmentStatus {
        SUBMITTED,
        ACCEPTED,
        REJECTED
    }
}
