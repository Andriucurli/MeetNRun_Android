package com.tokioschool.alugo.meetnrun.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.activities.controllers.UserController;
import com.tokioschool.alugo.meetnrun.model.User;
import com.tokioschool.alugo.meetnrun.util.Preferences;

import java.util.UUID;

public class QRCodeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button invitePatientButton;
    private EditText pacientSurname;
    private EditText pacientEmail;
    private UserController uc;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        uc = new UserController(this);
        currentUser = new UserController(this).getUser(Preferences.get_user_id(this));

        if (currentUser == null){
            return;
        }
        UUID code = UUID.randomUUID();

        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(code.toString(), BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            ((ImageView) findViewById(R.id.qr_invite_imageView)).setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        invitePatientButton = (Button) findViewById(R.id.invitePatientButton);
        pacientSurname = (EditText) findViewById(R.id.pacientNameEditText);
        pacientEmail = (EditText) findViewById(R.id.pacientEmailEditText);

        invitePatientButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (pacientSurname.getText().toString().compareTo("") == 0 ||
        pacientEmail.getText().toString().compareTo("") == 0){

        } else {
            uc.createPacient(pacientSurname.getText().toString(), pacientEmail.getText().toString(), currentUser.getId());
            finish();
        }

    }
}