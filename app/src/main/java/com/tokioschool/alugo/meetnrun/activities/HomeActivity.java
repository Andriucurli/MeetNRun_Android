package com.tokioschool.alugo.meetnrun.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.controllers.NotificationController;
import com.tokioschool.alugo.meetnrun.databinding.ActivityHomeBinding;
import com.tokioschool.alugo.meetnrun.util.NotificationHandler;
import com.tokioschool.alugo.meetnrun.util.Preferences;

public class HomeActivity extends BaseActivity {

    private ActivityHomeBinding binding;
    private BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (currentUser == null){
            return;
        }
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navView = (BottomNavigationView) findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_add_appointment, R.id.nav_list, R.id.nav_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.notifications_48px);
        }

        NotificationController nc = new NotificationController(this);
        if (!nc.getActiveNotificationsByUser(currentUser).isEmpty() && Preferences.get_notifications_enabled(this)){
            NotificationManager notificationManager = NotificationHandler.getNotificationManager(this);
            Notification notification = NotificationHandler.generateSimpleNotification(this);
            notificationManager.notify(1, notification);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navView.setSelectedItemId(R.id.nav_list);
    }

    public void resetActionBar()
    {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.notifications_48px);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(this, NotificationsActivity.class);
                startActivity(intent);
                break;
            case R.id.adduser_button:

                break;
            default:
               return false;
        }
        return super.onOptionsItemSelected(item);
    }
}