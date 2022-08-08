package com.tokioschool.alugo.meetnrun.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.tokioschool.alugo.meetnrun.model.Contracts;
import com.tokioschool.alugo.meetnrun.model.Contracts.UserEntry;
import com.tokioschool.alugo.meetnrun.model.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UserController extends BaseController {

    public static final byte[] initialScheduleByDay = new byte[]{(byte) 0x00,(byte) 0xFF,(byte) 0xF8,
            (byte) 0x00,(byte) 0xFF,(byte) 0xF8,
            (byte) 0x00,(byte) 0xFF,(byte) 0xF8,
            (byte) 0x00,(byte) 0xFF,(byte) 0xF8,
            (byte) 0x00,(byte) 0xFF,(byte) 0xF8,
            (byte) 0x00,(byte) 0xFF,(byte) 0xF8,
            (byte) 0x00,(byte) 0x00,(byte) 0x00};

    public UserController(Context context) {
        super(context);
    }

    private User getUserFromCursor(Cursor cursor){
        User user = null;
        int professional_idi = cursor.getColumnIndex(UserEntry.PROFESSIONAL_ID);

        Integer professional_id = !cursor.isNull(professional_idi)? cursor.getInt(professional_idi) : null;
        int namei = cursor.getColumnIndex(UserEntry.NAME);
        int surnamei = cursor.getColumnIndex(UserEntry.SURNAME);
        int passwordi = cursor.getColumnIndex(UserEntry.PASSWORD);
        int idi = cursor.getColumnIndex(UserEntry.ID);
        int phonei = cursor.getColumnIndex(UserEntry.PHONE);
        int emaili = cursor.getColumnIndex(UserEntry.EMAIL);
        int photoi = cursor.getColumnIndex(UserEntry.PHOTO);
        int schedulei = cursor.getColumnIndex(UserEntry.SCHEDULE);


        user = new User(cursor.getString(namei),
                cursor.getString(surnamei),
                cursor.getString(passwordi),
                cursor.getInt(idi),
                cursor.getString(phonei),
                cursor.getString(emaili),
                cursor.getBlob(photoi), professional_id, cursor.getBlob(schedulei));

        return user;
    }

    public User getUser(int id){
        SQLiteDatabase db =  sqlHelper.getReadableDatabase();

        Cursor cursor = db.query(UserEntry.TABLE_NAME,
                UserEntry.Columns,
                String.format(COMPARATOR_STRING, UserEntry.ID),
                new String[]{String.valueOf(id)}, null, null, null);

        User user = null;

        while (cursor.moveToNext())
        {
            user = getUserFromCursor(cursor);
        }

        db.close();

        return user;
    }

    public User getUser(String name){

        SQLiteDatabase db =  sqlHelper.getReadableDatabase();

        Cursor cursor = db.query(UserEntry.TABLE_NAME,
                UserEntry.Columns,
                String.format(COMPARATOR_STRING, UserEntry.NAME),
                new String[]{name}, null, null, null);

        User user = null;

        while (cursor.moveToNext())
        {
            user = getUserFromCursor(cursor);
        }

        db.close();

        return user;
    }

    public List<User> getPacients(User professional){

        List<User> result = new ArrayList<>();
        SQLiteDatabase db = sqlHelper.getReadableDatabase();

        Cursor cursor = db.query(UserEntry.TABLE_NAME,
                UserEntry.Columns,
                String.format(COMPARATOR_STRING, UserEntry.PROFESSIONAL_ID),
                new String[]{String.valueOf(professional.getId())}, null, null, null);

        while (cursor.moveToNext()){
            result.add(getUserFromCursor(cursor));
        }

        return result;
    }

    public boolean updateUser(User user){

        if (user == null){
            return false;
        }

        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        int updated;
        ContentValues contentValues = new ContentValues();
        contentValues.put(UserEntry.SURNAME, user.getSurname());
        contentValues.put(UserEntry.EMAIL, user.getEmail());
        contentValues.put(UserEntry.PHONE, user.getPhone());

        try {
            updated = db.update(UserEntry.TABLE_NAME, contentValues,
                    String.format(COMPARATOR_STRING, UserEntry.ID),
                    new String[]{String.valueOf(user.getId())});
        } catch (Exception e){
            e.printStackTrace();
            updated = 0;
        } finally {
            db.close();
        }

        return updated == 1;
    }

    public long createProfessional(String name, String password, String surname){
        return createUser(name, password, surname, null, null);

    }

    public long createPacient(String name, String email, int professional_id){

        return createUser(name, name, name, email, professional_id);
    }
    public long createUser (String name, String password, String surname, String email, Integer professional_id){

        if (name == null || name.compareTo("") == 0 ||
                password == null || password.compareTo("") == 0 ||
                surname == null || surname.compareTo("") == 0){
            return -1;
        }

        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        long id;
        ContentValues values = new ContentValues();

        // Pares clave-valor
        values.put(UserEntry.NAME, name);
        values.put(UserEntry.PASSWORD, password);
        values.put(UserEntry.SURNAME, surname);
        values.put(UserEntry.EMAIL, email);
        values.put(UserEntry.PROFESSIONAL_ID, professional_id);
        if (professional_id == null){
            values.put(Contracts.UserEntry.SCHEDULE, initialScheduleByDay);
        }

        try {
            id = db.insert(UserEntry.TABLE_NAME, null, values);
        } catch (Exception e){
            e.printStackTrace();
            id = -1;
        } finally {
            db.close();
        }

        return id;
    }

    public boolean setUserPhoto(User user, Uri photo) {

        if (user == null || photo == null){
            return false;
        }
        int updated = 0;
        byte[] inputData;
        try {
            InputStream iStream =  context.getContentResolver().openInputStream(photo);
            inputData = getBytes(iStream);
        } catch (Exception e){
            e.printStackTrace();
            inputData = null;
        }

        if (inputData != null){
            SQLiteDatabase db = sqlHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(UserEntry.PHOTO, inputData);

            try {
                updated = db.update(UserEntry.TABLE_NAME,values,
                        String.format(COMPARATOR_STRING, UserEntry.ID),
                        new String[]{String.valueOf(user.getId())});
            } catch (Exception e){
                e.printStackTrace();
                updated = 0;
            } finally {
                db.close();
                user.setPhoto(inputData);
            }
        }

        return updated == 1;
    }

    public boolean setSchedule(User user, byte[] schedule){

        if (user == null || schedule == null){
            return false;
        }

        int updated;
        SQLiteDatabase db = sqlHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserEntry.SCHEDULE, schedule);
        try {
            updated = db.update(UserEntry.TABLE_NAME, values,
                    String.format(COMPARATOR_STRING, UserEntry.ID),
                    new String[]{String.valueOf(user.getId())});
            user.setSchedule(schedule);
        } catch (Exception e){
            e.printStackTrace();
            updated = 0;
        } finally {
            db.close();
        }

        return updated == 1;
    }


    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;

            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

        return byteBuffer.toByteArray();
    }
}
