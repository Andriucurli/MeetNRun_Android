package com.tokioschool.alugo.meetnrun.controllers;

import android.content.Context;

import com.tokioschool.alugo.meetnrun.util.CustomSQLHelper;

public abstract class BaseController{

    protected CustomSQLHelper sqlHelper;
    protected Context context;
    protected static String COMPARATOR_STRING = "%s = ?";

    public BaseController(Context context) {
        this.sqlHelper = new CustomSQLHelper(context);
        this.context = context;
    }


}
