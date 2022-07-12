package com.tokioschool.alugo.meetnrun.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.activities.controllers.UserController;
import com.tokioschool.alugo.meetnrun.model.User;
import com.tokioschool.alugo.meetnrun.util.AlertDialogHandler;
import com.tokioschool.alugo.meetnrun.util.CustomSQLHelper;
import com.tokioschool.alugo.meetnrun.util.Preferences;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button loginButton;
    Button signUpButton;
    EditText userText;
    EditText pwdText;
    CustomSQLHelper sqlHelper = new CustomSQLHelper(this);
    ActivityResultLauncher<Intent> signUpResultLauncher;
    private Button signUpPacient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginButton);
        signUpButton = (Button) findViewById(R.id.signUpProfessionalButton);
        signUpPacient = (Button) findViewById(R.id.signUpPacientButton);
        userText = (EditText) findViewById(R.id.usernameTextField);
        pwdText = (EditText) findViewById(R.id.passwordTextField);

        loginButton.setOnClickListener(this);
        signUpPacient.setOnClickListener(this);
        signUpButton.setOnClickListener(this);


        signUpResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            showToastInfoUserCreated();
                        }
                    }
                });

    }

    private void showToastInfoUserCreated(){
        Toast toast = AlertDialogHandler.getInfoUserCreated(this);
        toast.show();
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.loginButton){
            String user = userText.getText().toString();

            UserController uc = new UserController(getApplicationContext());

            User currentUser = uc.getUser(user);
            AlertDialog alert = null;
            if (currentUser == null){
                alert = AlertDialogHandler.getErrorLogin(this);
            } else if (currentUser.getPwd().compareTo(pwdText.getText().toString()) != 0){
                alert = AlertDialogHandler.getErrorLogin(this);
            } else {
                Preferences.set_user_id(getApplicationContext(), currentUser.getId());
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }

            if (alert != null){
                alert.show();
            }
        } else if (v.getId() == R.id.signUpProfessionalButton){

            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            signUpResultLauncher.launch(intent);
        } else if (v.getId() == R.id.signUpPacientButton){
            Intent intent = new Intent(getApplicationContext(), SignUpPacientActivity.class);
            signUpResultLauncher.launch(intent);
        }
    }
}