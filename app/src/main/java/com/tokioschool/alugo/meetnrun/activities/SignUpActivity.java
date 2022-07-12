package com.tokioschool.alugo.meetnrun.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.activities.controllers.UserController;
import com.tokioschool.alugo.meetnrun.model.User;
import com.tokioschool.alugo.meetnrun.util.AlertDialogHandler;

public class SignUpActivity extends AppCompatActivity {

    private Button cancelSignUpButton;
    private Button createProfessionalButton;
    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText passwordEditText;
    private EditText repeatPasswordEditText;

    private UserController uc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        uc = new UserController(this);

        cancelSignUpButton = (Button) findViewById(R.id.cancelSignupButton);
        createProfessionalButton = (Button) findViewById(R.id.createProfessionalButton);
        nameEditText = (EditText) findViewById(R.id.signup_usernameField);
        surnameEditText = (EditText) findViewById(R.id.signup_usernameField);
        passwordEditText = (EditText) findViewById(R.id.signup_passwordField);
        repeatPasswordEditText = (EditText) findViewById(R.id.signup_repeatPasswordField);

        SignUpOnClickListener listener = new SignUpOnClickListener(this);

        cancelSignUpButton.setOnClickListener(listener);
        createProfessionalButton.setOnClickListener(listener);
    }

    private class SignUpOnClickListener implements View.OnClickListener{


        private Activity activity;

        public SignUpOnClickListener(Activity activity){
            this.activity = activity;
        }

        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.createProfessionalButton){
                String name = nameEditText.getText().toString();
                String surname = surnameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String repeatedPassword = repeatPasswordEditText.getText().toString();

                if (name.compareTo("") == 0 || surname.compareTo("") == 0 ||
                        password.compareTo("") == 0 || repeatedPassword.compareTo("") == 0){
                    AlertDialog alertDialog = AlertDialogHandler.getErrorSignup(activity);
                    alertDialog.show();
                } else if (password.compareTo(repeatedPassword) != 0){
                    AlertDialog alertDialog = AlertDialogHandler.getErrorPasswordNotEquals(activity);
                    alertDialog.show();
                } else {
                    User existingUser = uc.getUser(name);

                    if (existingUser != null){
                        AlertDialog alertDialog = AlertDialogHandler.getErrorExistingUser(activity);
                        alertDialog.show();
                    } else {
                        uc.createUser(name, password, surname);
                        activity.setResult(Activity.RESULT_OK);
                        activity.finish();
                    }
                }
            } else {
                activity.setResult(Activity.RESULT_CANCELED);
                activity.finish();
            }

        }
    }
}