package com.tokioschool.alugo.meetnrun.activities.controllers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tokioschool.alugo.meetnrun.model.Contracts.NotificationEntry;
import com.tokioschool.alugo.meetnrun.model.Notification;
import com.tokioschool.alugo.meetnrun.model.User;
import com.tokioschool.alugo.meetnrun.util.CustomSQLHelper;

import java.util.ArrayList;
import java.util.List;

public class NotificationController {

    private final CustomSQLHelper sqlHelper;
    private final Context context;

    public NotificationController(Context context){
        sqlHelper = new CustomSQLHelper(context);
        this.context = context;
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

            if (sender_id != 0){
                UserController uc = new UserController(context);
                sender = uc.getUser(sender_id);
            }

            int messageI = cursor.getColumnIndex(NotificationEntry.MESSAGE);
            int seenI = cursor.getColumnIndex(NotificationEntry.SEEN);
            int typeI = cursor.getColumnIndex(NotificationEntry.TYPE);

            result.add(new Notification(sender, user, cursor.getString(messageI),
                    cursor.getInt(seenI) == 1,
                    Notification.NotificationType.valueOf(cursor.getString(typeI))));
        }

        cursor.close();
        db.close();
        return result;
    }


}
