package com.tokioschool.alugo.meetnrun.model;

import android.provider.BaseColumns;

public class Contracts {

    public static abstract class UserEntry implements BaseColumns {

        public static final String TABLE_NAME ="User";

        public static final String ID = "user_id";
        public static final String NAME = "name";
        public static final String SURNAME = "surname";
        public static final String PASSWORD = "password";
        public static final String PHONE = "phone";
        public static final String EMAIL = "email";
        public static final String PHOTO = "photo";
        public static final String PROFESSIONAL_ID = "professional_id";
        public static final String SCHEDULE = "schedule";


        public static final String[] Columns = {ID, NAME, SURNAME, PASSWORD, PHONE, EMAIL, PHOTO, PROFESSIONAL_ID, SCHEDULE};

    }

    public static abstract class GroupEntry implements BaseColumns {

        public static final String TABLE_NAME ="UserGroup";

        public static final String ID = "group_id";
        public static final String NAME = "name";
        public static final String DESCRIPTION = "description";
        public static final String PHOTO = "photo";
        public static final String PROFESSIONAL_ID = "professional_id";

        public static final String[] Columns = {ID, NAME, DESCRIPTION, PHOTO, PROFESSIONAL_ID};
    }

    public static abstract class AvailablePeriodEntry implements BaseColumns {

        public static final String TABLE_NAME ="AvailablePeriod";

        public static final String ID = "period_id";
        public static final String DAY = "day";
        public static final String BEGIN_TIME = "beginTime";
        public static final String END_TIME = "endTime";
        public static final String USER_ID = "user_id";

        public static final String[] Columns = {ID, DAY, BEGIN_TIME, END_TIME, USER_ID};
    }

    public static abstract class AppointmentEntry implements BaseColumns {

        public static final String TABLE_NAME ="Appointment";

        public static final String ID = "appointment_id";
        public static final String PROFESSIONAL_ID = "professional_id";
        public static final String USER_ID = "user_id";
        public static final String PERIOD_ID = "period_id";
        public static final String STATUS = "status";

        public static final String[] Columns = {ID, PROFESSIONAL_ID, USER_ID, PERIOD_ID, STATUS};
    }

    public static abstract class NotificationEntry implements BaseColumns {

        public static final String TABLE_NAME ="Notification";

        public static final String ID = "notification_id";
        public static final String SENDER_ID = "sender_id";
        public static final String RECEIVER_ID = "receiver_id";
        public static final String MESSAGE = "message";
        public static final String SEEN = "seen";
        public static final String TYPE = "type";

        public static final String[] Columns = {ID, SENDER_ID, RECEIVER_ID, MESSAGE, SEEN, TYPE};
    }


}
