package com.tokioschool.alugo.meetnrun.model;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Notification {
    private int notification_id;
    private int sender_id;
    private int receiver_id;
    private String message;
    private boolean seen;
    private Integer appointment_id = null;

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

    public @Type int getType() {
        return type;
    }

    public Integer getAppointment_id() {
        return appointment_id;
    }

    public int getNotification_id() {
        return notification_id;
    }

    @Type int type;


    @IntDef({Type.CREATED, Type.NEED_CONFIRMATION, Type.CONFIRMED, Type.NEED_MODIFICATION, Type.MODIFIED, Type.CANCELLED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
        public int CREATED = 0;
        int NEED_CONFIRMATION = 1;
        int CONFIRMED = 2;
        int NEED_MODIFICATION = 3;
        int MODIFIED = 4;
        int CANCELLED = 5;
    }

    public Notification(int notification_id, int sender_id, int receiver_id, String message, boolean seen, @Type int type, Integer appointment_id) {
        this.notification_id = notification_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.message = message;
        this.seen = seen;
        this.type = type;
        this.appointment_id = appointment_id;
    }

}
