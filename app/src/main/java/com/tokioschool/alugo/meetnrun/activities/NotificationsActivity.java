package com.tokioschool.alugo.meetnrun.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.activities.controllers.NotificationController;
import com.tokioschool.alugo.meetnrun.activities.controllers.UserController;
import com.tokioschool.alugo.meetnrun.adapters.NotificationViewAdapter;
import com.tokioschool.alugo.meetnrun.model.Notification;
import com.tokioschool.alugo.meetnrun.model.User;
import com.tokioschool.alugo.meetnrun.util.Preferences;

import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    RecyclerView notificationsRV;
    NotificationViewAdapter adapter;
    NotificationController nc;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        currentUser = new UserController(this).getUser(Preferences.get_user_id(this));

        if (currentUser == null){
            return;
        }
        nc = new NotificationController(this);

        notificationsRV = (RecyclerView) findViewById(R.id.notificationsRecyclerView);
        notificationsRV.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        notificationsRV.setLayoutManager(llm);

        List<Notification> notifications = nc.getNotificationsByUser(currentUser);

        adapter = new NotificationViewAdapter(notifications);

        notificationsRV.setAdapter(adapter);


    }

}