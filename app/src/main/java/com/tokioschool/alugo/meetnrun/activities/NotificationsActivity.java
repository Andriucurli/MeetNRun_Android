package com.tokioschool.alugo.meetnrun.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.controllers.NotificationController;
import com.tokioschool.alugo.meetnrun.adapters.NotificationViewAdapter;
import com.tokioschool.alugo.meetnrun.model.Notification;

import java.util.List;

public class NotificationsActivity extends BaseActivity {

    RecyclerView notificationsRV;
    NotificationViewAdapter adapter;
    NotificationController nc;
    private TextView noNotificationsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        if (currentUser == null){
            return;
        }
        nc = new NotificationController(this);

        noNotificationsTextView = (TextView) findViewById(R.id.no_notifications_textView);
        notificationsRV = (RecyclerView) findViewById(R.id.notificationsRecyclerView);
        notificationsRV.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        notificationsRV.setLayoutManager(llm);

        List<Notification> notifications = nc.getActiveNotificationsByUser(currentUser);

        adapter = new NotificationViewAdapter(this);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                loadUI(adapter.getData());
            }
        });
        notificationsRV.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        List<Notification> data = nc.getActiveNotificationsByUser(currentUser);
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    private void loadUI(List<Notification> data){
        if (data.isEmpty()){
            noNotificationsTextView.setVisibility(View.VISIBLE);
            notificationsRV.setVisibility(View.GONE);
        } else {
            noNotificationsTextView.setVisibility(View.GONE);
            notificationsRV.setVisibility(View.VISIBLE);
        }
    }
}