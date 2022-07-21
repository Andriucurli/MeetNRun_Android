package com.tokioschool.alugo.meetnrun.util;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.tokioschool.alugo.meetnrun.R;

public class AlertHandler {

    public static AlertDialog.Builder builder;

    public static AlertDialog getErrorLogin(Context context){

        builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.message_error_login);
        builder.setNeutralButton(R.string.ok, (dialog, which) -> dialog.cancel());
        return builder.create();
    }

    public static AlertDialog getErrorSignup(Context context){

        builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.message_error_signup);
        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    public static AlertDialog getErrorPasswordNotEquals(Context context){

        builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.message_error_passwordNotEquals);
        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    public static AlertDialog getErrorExistingUser(Context context){

        builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.message_error_existingUser);
        builder.setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    public static Toast getInfoUserCreated(Context context){
        return Toast.makeText(context, R.string.message_info_userCreated, Toast.LENGTH_LONG);
    }

    public static Toast getWarningEmptyFields(Context context){
        return Toast.makeText(context, R.string.message_warning_emptyFields, Toast.LENGTH_LONG);
    }
    public static Toast getWarningHourWithExistingAppointment(Context context){
        return Toast.makeText(context, R.string.message_warning_hourWithExistingAppointment, Toast.LENGTH_LONG);
    }

    public static Toast getInfoAppointmentCreated(Context context){
        return Toast.makeText(context, R.string.message_info_appointmentCreated, Toast.LENGTH_LONG);
    }

}
