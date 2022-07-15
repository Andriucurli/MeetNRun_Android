package com.tokioschool.alugo.meetnrun.activities.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tokioschool.alugo.meetnrun.model.Contracts;
import com.tokioschool.alugo.meetnrun.model.Contracts.NotificationEntry;
import com.tokioschool.alugo.meetnrun.model.Notification;
import com.tokioschool.alugo.meetnrun.model.User;

import java.util.ArrayList;
import java.util.List;

public class NotificationController extends BaseController {


    public NotificationController( Context context) {
        super(context);
    }

    public boolean createNotification(int sender_id, int receiver_id, String message, Notification.Type type){
        SQLiteDatabase db = sqlHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        // Pares clave-valor
        values.put(NotificationEntry.SENDER_ID, sender_id);
        values.put(NotificationEntry.RECEIVER_ID, receiver_id);
        values.put(NotificationEntry.MESSAGE, message);
        values.put(NotificationEntry.SEEN, 0);
        values.put(NotificationEntry.TYPE, type.ordinal());

        db.insert(NotificationEntry.TABLE_NAME, null, values);

        db.close();
        return true;
    }

    public List<Notification> getNotificationsByUser(User user){

        List<Notification> result = new ArrayList<>();

        SQLiteDatabase db = sqlHelper.getReadableDatabase();

        Cursor cursor = db.query(NotificationEntry.TABLE_NAME, NotificationEntry.Columns,
                NotificationEntry.RECEIVER_ID + " LIKE ?", new String[]{String.valueOf(user.getId())},
                null, null, null);

        while (cursor.moveToNext()){
            User sender = null;
            int senderI = cursor.getColumnIndex(NotificationEntry.SENDER_ID);
            int sender_id = cursor.getInt(senderI);

            int messageI = cursor.getColumnIndex(NotificationEntry.MESSAGE);
            int seenI = cursor.getColumnIndex(NotificationEntry.SEEN);
            int typeI = cursor.getColumnIndex(NotificationEntry.TYPE);

            result.add(new Notification(sender_id, user.getId(), cursor.getString(messageI),
                    cursor.getInt(seenI) == 1,
                    Notification.Type.valueOf(cursor.getString(typeI))));
        }

        cursor.close();
        db.close();
        return result;
    }


}
