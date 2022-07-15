package com.tokioschool.alugo.meetnrun.model;

public class Notification {
    int sender_id;
    int receiver_id;
    String message;
    boolean seen;
    Type type;

    public enum Type {
        CREATED,
        NEED_CONFIRMATION,
        CONFIRMED,
        NEED_MODIFICATION,
        MODIFIED,
        CANCELLED
    }

    public Notification(int sender_id, int receiver_id, String message, boolean seen, Type type) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.message = message;
        this.seen = seen;
        this.type = type;
    }
}
