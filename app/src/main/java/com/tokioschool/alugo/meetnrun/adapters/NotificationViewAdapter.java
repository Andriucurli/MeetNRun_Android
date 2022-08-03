package com.tokioschool.alugo.meetnrun.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.controllers.AppointmentController;
import com.tokioschool.alugo.meetnrun.controllers.NotificationController;
import com.tokioschool.alugo.meetnrun.controllers.UserController;
import com.tokioschool.alugo.meetnrun.model.Appointment;
import com.tokioschool.alugo.meetnrun.model.Notification;
import com.tokioschool.alugo.meetnrun.model.User;

import java.util.ArrayList;
import java.util.List;

public class NotificationViewAdapter extends RecyclerView.Adapter<NotificationViewAdapter.NotificationViewHolder> {

    public void setData(List<Notification> data) {
        this.data = data;
    }

    public List<Notification> getData() {
        return data;
    }

    private List<Notification> data;
    private final Context context;
    private final UserController uc;

    public NotificationViewAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
        uc = new UserController(context);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_item  , parent, false);
        NotificationViewHolder vh = new NotificationViewHolder(view, context);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification elem = data.get(position);
        User sender = uc.getUser(elem.getSender_id());

        holder.loadNotification(elem, sender);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Notification notification;
        private Context context;
        private AppointmentController ac;
        private NotificationController nc;
        TextView sender;
        TextView message;

        ImageButton accept;
        ImageButton reject;

        public NotificationViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context = context;
            this.ac = new AppointmentController(context);
            this.nc = new NotificationController(context);
            sender = (TextView) itemView.findViewById(R.id.notificationSenderText);
            message = (TextView) itemView.findViewById(R.id.notificationMessageText);
            accept = (ImageButton) itemView.findViewById(R.id.acceptNotificationButton);
            reject = (ImageButton) itemView.findViewById(R.id.rejectNotificationButton);

            accept.setOnClickListener(this);
            reject.setOnClickListener(this);
        }

        public void loadNotification(Notification notification, User sender){
            this.notification = notification;
            this.sender.setText(sender.getSurname());
            this.message.setText(notification.getMessage());

            if (notification.getType() != Notification.Type.NEED_CONFIRMATION &&
            notification.getType() != Notification.Type.NEED_MODIFICATION){
                accept.setVisibility(View.INVISIBLE);
                reject.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            Integer appointment_id = notification.getAppointment_id();

            switch (v.getId()){
                case R.id.acceptNotificationButton:
                    if (appointment_id == null){
                        return;
                        //TODO control de errores
                    }

                    switch (notification.getType()){
                        case NEED_CONFIRMATION:
                            if (ac.confirmAppointment(appointment_id)){
                                Appointment appointment = ac.getAppointment(appointment_id);
                                nc.createNotification(appointment.getProfessional_id(), appointment.getUser_id(), context.getString(R.string.notification_text_appointment_confirmed), Notification.Type.CONFIRMED, appointment_id);
                            }
                            break;
                        case NEED_MODIFICATION:
                            if (ac.acceptModification(appointment_id)){
                                Appointment appointment = ac.getAppointment(appointment_id);
                                nc.createNotification(appointment.getProfessional_id(), appointment.getUser_id(),
                                        context.getString(R.string.notification_text_modification_accepted), Notification.Type.MODIFIED, appointment_id);
                            }
                            break;
                        default:
                            return;
                    }


                    break;
                case R.id.rejectNotificationButton:
                    if (appointment_id == null){
                        return;
                    }
                    switch (notification.getType()){
                        case NEED_CONFIRMATION:
                            if (ac.rejectAppointment(appointment_id)){
                                Appointment appointment = ac.getAppointment(appointment_id);
                                nc.createNotification(appointment.getProfessional_id(), appointment.getUser_id(), context.getString(R.string.notification_text_appointment_cancelled), Notification.Type.CANCELLED, appointment_id);
                            }
                            break;
                        case NEED_MODIFICATION:
                            if (ac.rejectModification(appointment_id)){
                                Appointment appointment = ac.getAppointment(appointment_id);
                                nc.createNotification(appointment.getProfessional_id(), appointment.getUser_id(), context.getString(R.string.notification_text_modification_rejected), Notification.Type.CANCELLED, appointment_id);
                            }
                            break;
                        default:
                            return;
                    }
                    break;
            }

            int notificationPos = data.indexOf(notification);
            removeAt(notificationPos);
            nc.markNotificationAsSeen(notification.getNotification_id());
        }

        public void removeAt(int position) {
            data.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, data.size());
        }
    }
}
