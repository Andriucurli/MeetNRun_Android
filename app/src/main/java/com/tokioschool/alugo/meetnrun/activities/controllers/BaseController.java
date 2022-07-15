package com.tokioschool.alugo.meetnrun.activities.controllers;

import android.content.Context;

import com.tokioschool.alugo.meetnrun.util.CustomSQLHelper;

public abstract class BaseController{

    protected CustomSQLHelper sqlHelper;
    protected Context context;

    public BaseController(Context context) {
        this.sqlHelper = new CustomSQLHelper(context);
        this.context = context;
    }
}
