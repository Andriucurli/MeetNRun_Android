package com.tokioschool.alugo.meetnrun.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.util.Preferences;

public class SettingsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Switch notificationsSwitch;
    private Switch recordatoriesSwitch;
    private Button aboutUsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        notificationsSwitch = (Switch) findViewById(R.id.notificationsSwitch);
        recordatoriesSwitch = (Switch) findViewById(R.id.createRecordatorySwitch);
        aboutUsButton = (Button) findViewById(R.id.aboutUsButton);
    }

    @Override
    protected void onResume() {
        super.onResume();

        notificationsSwitch.setChecked(Preferences.get_notifications_enabled(this));
        recordatoriesSwitch.setChecked(Preferences.get_recordatories_auto(this));

        notificationsSwitch.setOnCheckedChangeListener(this);
        recordatoriesSwitch.setOnCheckedChangeListener(this);

        aboutUsButton.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Preferences.set_preferences(this, notificationsSwitch.isChecked(), recordatoriesSwitch.isChecked());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.aboutUsButton){
            Intent intent = new Intent(this, AboutUsActivity.class);
            startActivity(intent);
        }
    }
}