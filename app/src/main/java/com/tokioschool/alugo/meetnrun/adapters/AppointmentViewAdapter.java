package com.tokioschool.alugo.meetnrun.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.activities.controllers.UserController;
import com.tokioschool.alugo.meetnrun.model.Appointment;
import com.tokioschool.alugo.meetnrun.model.Notification;
import com.tokioschool.alugo.meetnrun.model.User;
import com.tokioschool.alugo.meetnrun.util.Utils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class AppointmentViewAdapter extends RecyclerView.Adapter<AppointmentViewAdapter.AppointmentViewHolder> {

    public void setData(List<Appointment> data) {
        this.data = data;
    }

    private List<Appointment> data;
    private final Context context;
    private final UserController uc;
    private  final User user;

    public AppointmentViewAdapter(Context context, User user){
        this.data = new ArrayList<>();
        this.context = context;
        uc = new UserController(context);
        this.user = user;
    }


    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_list_item  , parent, false);
        AppointmentViewHolder vh = new AppointmentViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment elem = data.get(position);

        holder.loadAppointment(elem);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class AppointmentViewHolder extends RecyclerView.ViewHolder{

        TextView hour;
        TextView day;
        TextView username;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            hour = (TextView) itemView.findViewById(R.id.appointmentSubtitleTextView);
            day = (TextView) itemView.findViewById(R.id.appointmentTitleTextView);
            username = (TextView) itemView.findViewById(R.id.appointmentDetailTextView);
        }

        public void loadAppointment(Appointment appointment){

            day.setText(Utils.getDayByInt(appointment.getDay()).name());
            hour.setText(String.format("%02d:00", appointment.getHour()));
            if (user.isProfessional()){
                User pacient = uc.getUser(appointment.getUser_id());
                username.setText(pacient.getName());
            } else {
                User professional = uc.getUser(appointment.getProfessional_id());
                username.setText(professional.getName());
            }

        }
    }
}
