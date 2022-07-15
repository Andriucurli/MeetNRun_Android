package com.tokioschool.alugo.meetnrun.activities.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.tokioschool.alugo.meetnrun.model.Contracts;
import com.tokioschool.alugo.meetnrun.model.Contracts.UserEntry;
import com.tokioschool.alugo.meetnrun.model.User;
import com.tokioschool.alugo.meetnrun.util.CustomSQLHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UserController extends BaseController {

    public static final byte[] initialScheduleByDay = new byte[]{(byte) 0x00,(byte) 0xFF,(byte) 0xF8,
            (byte) 0x00,(byte) 0xFF,(byte) 0xF8,
            (byte) 0x00,(byte) 0xFF,(byte) 0xF8,
            (byte) 0x00,(byte) 0xFF,(byte) 0xF8,
            (byte) 0x00,(byte) 0xFF,(byte) 0xF8,
            (byte) 0x00,(byte) 0xFF,(byte) 0xF8,
            (byte) 0x00,(byte) 0xFF,(byte) 0xF8};

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

        Cursor cursor = db.query(UserEntry.TABLE_NAME, UserEntry.Columns, UserEntry.ID + " LIKE ?", new String[]{String.valueOf(id)}, null, null, null);

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

        Cursor cursor = db.query(UserEntry.TABLE_NAME, UserEntry.Columns, UserEntry.NAME + " LIKE ?", new String[]{name}, null, null, null);

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

        Cursor cursor = db.query(UserEntry.TABLE_NAME, UserEntry.Columns, UserEntry.PROFESSIONAL_ID + " LIKE ?",
                new String[]{String.valueOf(professional.getId())}, null, null, null);

        while (cursor.moveToNext()){
            result.add(getUserFromCursor(cursor));
        }

        return result;
    }

    public void updateUser(User user){
        SQLiteDatabase db = sqlHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(UserEntry.SURNAME, user.getSurname());
        contentValues.put(UserEntry.EMAIL, user.getEmail());
        contentValues.put(UserEntry.PHONE, user.getPhone());
        db.update(UserEntry.TABLE_NAME, contentValues, String.format("%s = ?", UserEntry.ID), new String[]{String.valueOf(user.getId())});

        db.close();
    }

    public User createProfessional(String name, String password, String surname){
        User result;
        createUser(name, password, surname, null, null);

        return getUser(name);
    }

    public User createPacient(String name, String email, int professional_id){
        User result;
        createUser(name, name, name, email, professional_id);

        return getUser(name);
    }
    public boolean createUser (String name, String password, String surname, String email, Integer professional_id){

        SQLiteDatabase db = sqlHelper.getWritableDatabase();

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

        db.insert(UserEntry.TABLE_NAME, null, values);

        return true;
    }

    public void setUserPhoto(User user, Uri photo) throws IOException {
        InputStream iStream =   context.getContentResolver().openInputStream(photo);
        byte[] inputData = getBytes(iStream);

        SQLiteDatabase db = sqlHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserEntry.PHOTO, inputData);

        db.update(UserEntry.TABLE_NAME,values, UserEntry.ID + " LIKE ?", new String[]{String.valueOf(user.getId())});

        db.close();

    }

    public void setSchedule(User user, byte[] schedule){
        SQLiteDatabase db = sqlHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserEntry.SCHEDULE, schedule);

        db.update(UserEntry.TABLE_NAME, values, UserEntry.ID + " = ?", new String[]{String.valueOf(user.getId())});

        user.setSchedule(schedule);
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
