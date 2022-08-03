package com.tokioschool.alugo.meetnrun.activities;

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

import java.util.UUID;

public class QRCodeActivity extends BaseActivity implements View.OnClickListener {

    private Button invitePatientButton;
    private EditText pacientSurname;
    private EditText pacientEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
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
            userController.createPacient(pacientSurname.getText().toString(), pacientEmail.getText().toString(), currentUser.getId());
            finish();
        }

    }
}