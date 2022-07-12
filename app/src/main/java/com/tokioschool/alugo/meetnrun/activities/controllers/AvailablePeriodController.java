package com.tokioschool.alugo.meetnrun.activities.controllers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tokioschool.alugo.meetnrun.model.AvailablePeriod;
import com.tokioschool.alugo.meetnrun.model.Contracts.AvailablePeriodEntry;
import com.tokioschool.alugo.meetnrun.model.User;
import com.tokioschool.alugo.meetnrun.util.CustomSQLHelper;

import java.util.ArrayList;
import java.util.List;

public class AvailablePeriodController {

    private final CustomSQLHelper sqlHelper;

    public AvailablePeriodController(Context context){
        sqlHelper = new CustomSQLHelper(context);
    }

    public List<AvailablePeriod> getAvailablePeriod(User user, int day){

        List<AvailablePeriod> result = new ArrayList<>();
        SQLiteDatabase db =  sqlHelper.getReadableDatabase();

        Cursor cursor = db.query(AvailablePeriodEntry.TABLE_NAME, AvailablePeriodEntry.Columns,
                AvailablePeriodEntry.USER_ID + " LIKE ? AND " + AvailablePeriodEntry.DAY + " LIKE ?", new String[]{String.valueOf(user.getId()), String.valueOf(day)},
                null, null, null);

        while (cursor.moveToNext()){
            int dayI = cursor.getColumnIndex(AvailablePeriodEntry.DAY);
            int beginI = cursor.getColumnIndex(AvailablePeriodEntry.BEGIN_TIME);
            int endI = cursor.getColumnIndex(AvailablePeriodEntry.END_TIME);
            int userI = cursor.getColumnIndex(AvailablePeriodEntry.USER_ID);

            result.add(new AvailablePeriod(cursor.getInt(dayI),
                    cursor.getString(beginI),
                    cursor.getString(endI),
                    cursor.getInt(userI)));
        }

        cursor.close();
        db.close();
        return result;
    }

}
