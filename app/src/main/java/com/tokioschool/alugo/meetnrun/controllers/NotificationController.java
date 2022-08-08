package com.tokioschool.alugo.meetnrun.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tokioschool.alugo.meetnrun.model.Contracts.NotificationEntry;
import com.tokioschool.alugo.meetnrun.model.Notification;
import com.tokioschool.alugo.meetnrun.model.User;

import java.util.ArrayList;
import java.util.List;

public class NotificationController extends BaseController {


    public NotificationController( Context context) {
        super(context);
    }

    public long createNotification(int sender_id, int receiver_id, String message, Notification.Type type, Integer appointment_id){

        if (sender_id == -1 ||
        receiver_id == -1 ||
        type == null){
            return -1;
        }

        long id;
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Pares clave-valor
        values.put(NotificationEntry.SENDER_ID, sender_id);
        values.put(NotificationEntry.RECEIVER_ID, receiver_id);
        values.put(NotificationEntry.MESSAGE, message);
        values.put(NotificationEntry.SEEN, 0);
        values.put(NotificationEntry.TYPE, type.name());

        if (appointment_id != null){
            values.put(NotificationEntry.APPOINTMENT_ID, appointment_id);
        }

        try {
            id = db.insert(NotificationEntry.TABLE_NAME, null, values);
        } catch (Exception e){
            e.printStackTrace();
            id = -1;
        } finally {
            db.close();
        }

        return id;
    }

    public List<Notification> getActiveNotificationsByUser(User user){

        List<Notification> result = new ArrayList<>();

        SQLiteDatabase db = sqlHelper.getReadableDatabase();

        Cursor cursor = db.query(NotificationEntry.TABLE_NAME, NotificationEntry.Columns,
                String.format("%s LIKE ? AND %s = 0", NotificationEntry.RECEIVER_ID, NotificationEntry.SEEN),
                new String[]{String.valueOf(user.getId())},
                null, null, null);

        while (cursor.moveToNext()){

            int senderI = cursor.getColumnIndex(NotificationEntry.SENDER_ID);
            int sender_id = cursor.getInt(senderI);

            int messageI = cursor.getColumnIndex(NotificationEntry.MESSAGE);
            int seenI = cursor.getColumnIndex(NotificationEntry.SEEN);
            int typeI = cursor.getColumnIndex(NotificationEntry.TYPE);
            int appointment_idi = cursor.getColumnIndex(NotificationEntry.APPOINTMENT_ID);
            int id = cursor.getColumnIndex(NotificationEntry.ID);

            Integer appointment_id = !cursor.isNull(appointment_idi)? cursor.getInt(appointment_idi) : null;

            result.add(new Notification(cursor.getInt(id),sender_id, user.getId(), cursor.getString(messageI),
                    cursor.getInt(seenI) == 1,
                    Notification.Type.valueOf(cursor.getString(typeI)),
                    appointment_id));
        }

        cursor.close();
        db.close();
        return result;
    }

    public boolean markNotificationAsSeen(int notification_id){

        if (notification_id == -1){
            return false;
        }

        int updated;
            SQLiteDatabase db = sqlHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(NotificationEntry.SEEN, 1);

            try {
                updated = db.update(NotificationEntry.TABLE_NAME,contentValues,
                        String.format(COMPARATOR_STRING, NotificationEntry.ID),
                        new String[]{String.valueOf(notification_id)});
            } catch (Exception e){
                e.printStackTrace();
                updated = 0;
            } finally {
                db.close();
            }

        return updated == 1;
    }
}
