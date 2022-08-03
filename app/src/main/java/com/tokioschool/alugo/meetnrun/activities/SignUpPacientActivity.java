package com.tokioschool.alugo.meetnrun.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.zxing.integration.android.IntentIntegrator;
import com.tokioschool.alugo.meetnrun.R;

public class SignUpPacientActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_pacient);
        findViewById(R.id.usernameTextView);
        ImageButton qrButton = (ImageButton) findViewById(R.id.qr_button);
        qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQr();
            }
        });
    }

    private void scanQr(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                .initiateScan();
    }
}