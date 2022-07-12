package com.tokioschool.alugo.meetnrun.model;

import java.sql.Time;

public class AvailablePeriod {

    public AvailablePeriod(int day, String begin, String end, int user_id) {
        this.day = day;
        this.begin = Time.valueOf(begin);
        this.end = Time.valueOf(end);
        this.user_id = user_id;
    }

    int day;
    Time begin;
    Time end;
    int user_id;
}
