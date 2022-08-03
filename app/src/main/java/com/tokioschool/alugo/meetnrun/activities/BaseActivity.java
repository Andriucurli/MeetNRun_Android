package com.tokioschool.alugo.meetnrun.activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tokioschool.alugo.meetnrun.controllers.UserController;
import com.tokioschool.alugo.meetnrun.model.User;
import com.tokioschool.alugo.meetnrun.util.Preferences;

public abstract class BaseActivity extends AppCompatActivity {

    protected User currentUser = null;
    protected UserController userController = null;

    public User getCurrentUser() {
        return currentUser;
    }

    public UserController getUserController(){return  userController;}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userController = new UserController(this);
        currentUser = userController.getUser(Preferences.get_user_id(this));
    }
}
