package com.tokioschool.alugo.meetnrun.util;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class AlertHandler {

    public static AlertDialog.Builder builder;

    public static AlertDialog getErrorLogin(Context context){

        builder = new AlertDialog.Builder(context);
        builder.setMessage("Cannot log in. Please, check your username and password or try it later.");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    public static AlertDialog getErrorSignup(Context context){

        builder = new AlertDialog.Builder(context);
        builder.setMessage("Cannot sign up. Please, check all the fields or try it later.");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    public static AlertDialog getErrorPasswordNotEquals(Context context){

        builder = new AlertDialog.Builder(context);
        builder.setMessage("Cannot sign up, the passwords are different.");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    public static AlertDialog getErrorExistingUser(Context context){

        builder = new AlertDialog.Builder(context);
        builder.setMessage("Cannot sign up, the username is already on use.");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder.create();
    }

    public static Toast getInfoUserCreated(Context context){
        return Toast.makeText(context, "User created successfully", Toast.LENGTH_LONG);
    }

}
