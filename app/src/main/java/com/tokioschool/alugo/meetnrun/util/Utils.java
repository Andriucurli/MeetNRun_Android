package com.tokioschool.alugo.meetnrun.util;

public class Utils {

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
        mon,
        tu,
        wed,
        th,
        fri,
        sat,
        sun
    }


    public static Day getDayByInt(int i){
        Day day;
        switch (i){
            case 0:
                day = Utils.Day.mon;
                break;
            case 1:
                day = Utils.Day.tu;
                break;
            case 2:
                day = Utils.Day.wed;
                break;
            case 3:
                day = Utils.Day.th;
                break;
            case 4:
                day = Day.fri;
                break;
            case 5:
                day = Day.sat;
                break;
            default:
                day = Day.sun;
                break;
        }
        return day;
    }
}
