package com.tokioschool.alugo.meetnrun.activities.ui.addAppointment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.activities.HomeActivity;
import com.tokioschool.alugo.meetnrun.activities.controllers.AppointmentController;
import com.tokioschool.alugo.meetnrun.activities.controllers.UserController;
import com.tokioschool.alugo.meetnrun.adapters.CustomSpinnerAdapter;
import com.tokioschool.alugo.meetnrun.databinding.FragmentAddAppointmentBinding;
import com.tokioschool.alugo.meetnrun.model.User;
import com.tokioschool.alugo.meetnrun.util.Utils;

import java.util.ArrayList;
import java.util.List;


public class AddAppointmentFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentAddAppointmentBinding binding;
    private HomeActivity mainActivity;
    private UserController uc;
    private User user;

    private TextView anythingTextView;
    private Spinner anythingSpinner;
    private Spinner daySpinner;
    private Button addAppointmentButton;
    private Spinner hourSpinner;

    private List<User> pacients;
    private Integer pacientPos = null;
    private Integer day = null;
    private Integer hour = null;


    private void loadUIelems(){

        uc = new UserController(getContext());
        mainActivity = (HomeActivity) getActivity();
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainActivity.setTitle("");
        user = mainActivity.currentUser;

        anythingTextView = (TextView) getView().findViewById(R.id.anythingTextField);
        anythingSpinner = (Spinner) getView().findViewById(R.id.anythingSpinner);
        daySpinner = (Spinner) getView().findViewById(R.id.daySpinner);
        hourSpinner = (Spinner) getView().findViewById(R.id.hourSpinner);
        addAppointmentButton = (Button) getView().findViewById(R.id.addAppointmentButton);

        anythingSpinner.setOnItemSelectedListener(this);
        daySpinner.setOnItemSelectedListener(this);
        hourSpinner.setOnItemSelectedListener(this);
        addAppointmentButton.setOnClickListener(new AddAppointmentListener(getContext()));

        anythingSpinner.setEnabled(false);
        daySpinner.setEnabled(false);
        hourSpinner.setEnabled(false);

        List<String> days = new ArrayList<String>();
        for (Utils.Day d: Utils.Day.values()) {
            days.add(d.name());
        }

        CustomSpinnerAdapter dayAdapter = new CustomSpinnerAdapter(getContext(), days);
        daySpinner.setAdapter(dayAdapter);

    }

    private void loadHourSpinner(int day){
        List<String> hours = new ArrayList<>();
        byte[] schedule = user.getSchedule();

        //los dias estan divididos en 3 bytes. 3 bytes = 3*8 bits = 24 horas

        for (int i = 0, hour = 0; i < 3; i++){
            byte bytei = schedule[day*3+i];

            for (int j = 7; j >= 0; j--,hour++){
                if (Utils.isBitSet(bytei, j)){
                    hours.add(String.format("%02d:00", hour));
                }
            }
        }

        CustomSpinnerAdapter hourAdapter = new CustomSpinnerAdapter(getContext(), hours);
        hourSpinner.setEnabled(true);
        hourSpinner.setAdapter(hourAdapter);
    }

    private void initForm(){

        if (user.isProfessional()){
            anythingTextView.setText("Pacient");
            pacients = uc.getPacients(user);
            List<String> names = new ArrayList<>();
            for (User pacient:
                    pacients) {
                names.add(pacient.getName());
            }

            CustomSpinnerAdapter pacientAdapter = new CustomSpinnerAdapter(getContext(), names);

            anythingSpinner.setEnabled(true);
            anythingSpinner.setAdapter(pacientAdapter);

        } else {
            anythingTextView.setVisibility(View.INVISIBLE);
            anythingSpinner.setVisibility(View.INVISIBLE);

            daySpinner.setEnabled(true);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddAppointmentViewModel addAppointmentViewModel =
                new ViewModelProvider(this).get(AddAppointmentViewModel.class);

        binding = FragmentAddAppointmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadUIelems();
    }

    @Override
    public void onResume() {
        super.onResume();
        initForm();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.anythingSpinner:
                if (position == 0) {
                    return;
                } else {
                    pacientPos = position - 1;
                    daySpinner.setEnabled(true);
                }
                break;
            case R.id.daySpinner:
                if (position == 0) {
                    return;
                } else {
                    hourSpinner.setEnabled(true);
                    day = position - 1;
                    loadHourSpinner(position-1);
                }
                break;
            default:
                if (position == 0){
                    return;
                } else {

                    String hourStr = (String) hourSpinner.getAdapter().getItem(position-1);
                    hour = Integer.parseInt(hourStr.split(":")[0]);
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        switch (parent.getId()){
            case R.id.anythingSpinner:
                daySpinner.setEnabled(false);
                hourSpinner.setEnabled(false);
                pacientPos = null;
                day = null;
                hour = null;
                break;
            case R.id.daySpinner:
                hourSpinner.setEnabled(false);
                day = null;
                hour = null;
                break;
            default:
                hour = null;
                break;
        }
    }


    public class AddAppointmentListener implements View.OnClickListener {

        private final Context context;
        private final AppointmentController ac;
        private final UserController uc;

        public AddAppointmentListener(Context context) {
            this.context = context;
            this.ac = new AppointmentController(context);
            this.uc = new UserController(context);
        }

        @Override
        public void onClick(View v) {

            if (hour == null || day == null ||
                    (!user.isProfessional() && pacientPos == null )){
                //TODO toast
                return;
            }

            if (user.isProfessional()){
                User pacient = uc.getUser(pacientPos);
                ac.createAppointment(user, pacient, day, hour);

            } else {
                User professional = uc.getUser(user.getProfessional_id());
                ac.requestAppointment(professional, user, day, hour);
            }
        }
    }
}