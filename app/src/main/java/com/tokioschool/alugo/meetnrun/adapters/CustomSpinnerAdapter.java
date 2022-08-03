package com.tokioschool.alugo.meetnrun.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.controllers.AppointmentController;
import com.tokioschool.alugo.meetnrun.controllers.UserController;
import com.tokioschool.alugo.meetnrun.model.User;
import com.tokioschool.alugo.meetnrun.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class CustomSpinnerAdapter extends ArrayAdapter<Pair<Integer,String>> {

    public CustomSpinnerAdapter(Context context, List<Pair<Integer,String>> items) {
        super(context, R.layout.simple_spinner_item, items);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return initialSelection(true);
        }
        return getCustomView(position, convertView, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (position == 0) {
            return initialSelection(false);
        }
        return getCustomView(position, convertView, parent);
    }


    @Override
    public int getCount() {
        return super.getCount() + 1; // Adjust for initial selection item
    }

    private View initialSelection(boolean dropdown) {
        // Just an example using a simple TextView. Create whatever default view
        // to suit your needs, inflating a separate layout if it's cleaner.
        TextView view = new TextView(getContext());
        view.setText(R.string.spinner_select_one);
        //int spacing = getContext().getResources().getDimensionPixelSize(R.dimen.);
        //view.setPadding(0, spacing, 0, spacing);

        if (dropdown) { // Hidden when the dropdown is opened
            view.setHeight(0);
        }

        return view;
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        // Distinguish "real" spinner items (that can be reused) from initial selection item
        View row = convertView != null && !(convertView instanceof TextView)
                ? convertView :
                LayoutInflater.from(getContext()).inflate(R.layout.simple_spinner_item, parent, false);

        position = position - 1; // Adjust for initial selection item
        Pair<Integer,String> item = getItem(position);

        TextView valueView = (TextView) row.findViewById(R.id.spinner_item_value);
        valueView.setText(item.second);
        // ... Resolve views & populate with data ...
        return row;
    }

    public static CustomSpinnerAdapter getDaySpinnerAdapter(Context context){
        List<Pair<Integer,String>> days = new ArrayList<>();
        for (Utils.Day d: Utils.Day.values()) {
            int dayNameId = context.getResources().getIdentifier(d.name(), "string", context.getPackageName());
            days.add(new Pair<>(d.ordinal(), context.getString(dayNameId)));
        }

        return new CustomSpinnerAdapter(context, days);
    }

    public static CustomSpinnerAdapter getHourSpinnerAdapter(Context context, User user, int day){
            List<Pair<Integer,String>> hours = new ArrayList<>();
            byte[] schedule;
            if (user.isProfessional()){
                schedule = user.getSchedule();
            } else {
                User professional = new UserController(context).getUser(user.getProfessional_id());
                schedule = professional.getSchedule();
            }

            //los dias estan divididos en 3 bytes. 3 bytes = 3*8 bits = 24 horas
        AppointmentController appointmentController = new AppointmentController(context);
            for (int i = 0, hour = 0; i < 3; i++){
                byte bytei = schedule[day*3+i];

                for (int j = 7; j >= 0; j--,hour++){
                    if (Utils.isBitSet(bytei, j) && appointmentController.checkAppointment(user, day, hour)){
                        hours.add(new Pair<>(hour, String.format("%02d:00", hour)));
                    }
                }
            }
            return  new CustomSpinnerAdapter(context, hours);
    }

}