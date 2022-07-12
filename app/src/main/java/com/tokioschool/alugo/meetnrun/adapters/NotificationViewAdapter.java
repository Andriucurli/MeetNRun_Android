package com.tokioschool.alugo.meetnrun.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.model.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationViewAdapter extends RecyclerView.Adapter<NotificationViewAdapter.NotificationViewHolder> {

    List<Notification> data = new ArrayList<>();

    public NotificationViewAdapter(List<Notification> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_item  , parent, false);
        NotificationViewHolder vh = new NotificationViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification elem = data.get(position);

        holder.sender.setText(elem.getSender().getName());
        holder.message.setText(elem.getMessage());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder{

        TextView sender;
        TextView message;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            sender = (TextView) itemView.findViewById(R.id.notificationSenderText);
            message = (TextView) itemView.findViewById(R.id.notificationMessageText);
        }
    }

}
