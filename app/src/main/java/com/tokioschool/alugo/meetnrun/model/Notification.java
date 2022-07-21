package com.tokioschool.alugo.meetnrun.model;

public class Notification {
    int notification_id;
    int sender_id;
    int receiver_id;
    String message;
    boolean seen;
    Integer appointment_id = null;

    public int getSender_id() {
        return sender_id;
    }

    public int getReceiver_id() {
        return receiver_id;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSeen() {
        return seen;
    }

    public Type getType() {
        return type;
    }

    public Integer getAppointment_id() {
        return appointment_id;
    }

    public int getNotification_id() {
        return notification_id;
    }

    Type type;

    public enum Type {
        CREATED,
        NEED_CONFIRMATION,
        CONFIRMED,
        NEED_MODIFICATION,
        MODIFIED,
        CANCELLED
    }

    public Notification(int notification_id, int sender_id, int receiver_id, String message, boolean seen, Type type, Integer appointment_id) {
        this.notification_id = notification_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.message = message;
        this.seen = seen;
        this.type = type;
        this.appointment_id = appointment_id;
    }

}
