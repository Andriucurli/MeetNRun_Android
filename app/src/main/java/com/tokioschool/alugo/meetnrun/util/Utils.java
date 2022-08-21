package com.tokioschool.alugo.meetnrun.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.CalendarContract;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tokioschool.alugo.meetnrun.model.Appointment;
import com.tokioschool.alugo.meetnrun.model.User;

import java.util.Calendar;
import java.util.TimeZone;

public class Utils {

    public static final int CALL_REQUEST_CODE = 112233;
    public static final int WRITE_CALENDAR_CODE = 445566;
    public static final int CAMERA_CODE = 778899;

    public static Boolean isBitSet(byte b, int bit)
    {
        return (b & (1 << bit)) != 0;
    }

    public static byte setBit(byte b, int bit, boolean value)
    {
        byte result;

        if (value){
            result = (byte) (b | (1 << bit));
        } else {
            result = (byte) (b & ~(1 << bit));
        }

        return result;
    }

    public enum Day{
        mo,
        tu,
        we,
        th,
        fr,
        sa,
        su
    }

    public static Calendar nextDayOfWeek(int dow) {
        Calendar date = Calendar.getInstance();
        int diff = dow - date.get(Calendar.DAY_OF_WEEK);
        if (diff <= 0) {
            diff += 7;
        }
        date.add(Calendar.DAY_OF_MONTH, diff);
        return date;
    }

    public static int getCalendarDay(int i){
        switch (i){
            case 0:
                return Calendar.MONDAY;
            case 1:
                return Calendar.TUESDAY;
            case 2:
                return Calendar.WEDNESDAY;
            case 3:
                return Calendar.THURSDAY;
            case 4:
                return Calendar.FRIDAY;
            case 5:
                return Calendar.SATURDAY;
            case 6:
                return Calendar.SUNDAY;
        }
        return -1;
    }

    public static Day getDayByInt(int i){
        Day day;
        switch (i){
            case 0:
                day = Utils.Day.mo;
                break;
            case 1:
                day = Utils.Day.tu;
                break;
            case 2:
                day = Utils.Day.we;
                break;
            case 3:
                day = Utils.Day.th;
                break;
            case 4:
                day = Day.fr;
                break;
            case 5:
                day = Day.sa;
                break;
            default:
                day = Day.su;
                break;
        }
        return day;
    }

    public static @Appointment.Status int getAppointmentStatusByInt(int i){
        @Appointment.Status int status;
        switch (i){
            case 0:
                status = Appointment.Status.REQUESTED;
                break;
            case 1:
                status = Appointment.Status.CONFIRMED;
                break;
            case 2:
                status = Appointment.Status.CANCELLED;
                break;
            default:
                status = Appointment.Status.MODIFICATION_REQUESTED;
                break;
        }
        return status;
    }


    public static Intent generateRecordatoryIntent(Appointment appointment, User user){

            Calendar calendar = Utils.nextDayOfWeek(Utils.getCalendarDay(appointment.getDay()));
            calendar.set(Calendar.HOUR, appointment.getHour());
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.setTimeZone(TimeZone.getDefault());

            long start = calendar.getTimeInMillis();
            long end = calendar.getTimeInMillis() + 60 * 60 * 1000;

            Intent intent = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .setType("vnd.android.cursor.item/event")
                    .putExtra(CalendarContract.Events.TITLE, String.format("Appointment with %s", user.getSurname()))
                    .putExtra(CalendarContract.Events.RRULE, "FREQ=WEEKLY;BYDAY="+Utils.getDayByInt(appointment.getDay()).name().toUpperCase()+";")
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start)
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);

            return intent;
    }
}
