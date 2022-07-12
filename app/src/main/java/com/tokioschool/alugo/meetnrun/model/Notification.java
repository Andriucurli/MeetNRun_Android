package com.tokioschool.alugo.meetnrun.model;

public class Notification {
    User sender = null;
    User receiver = null;
    String message;
    boolean seen;
    NotificationType type;

    public enum NotificationType {
        NEED_CONFIRMATION,
        CONFIRMED,
        NEED_MODIFICATION,
        MODIFIED,
        CANCELLED
    }

    public Notification(User sender, User receiver, String message, boolean seen, NotificationType type) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.seen = seen;
        this.type = type;
    }

    public User getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
