package com.tokioschool.alugo.meetnrun.activities.ui.addAppointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.activities.HomeActivity;
import com.tokioschool.alugo.meetnrun.activities.controllers.UserController;
import com.tokioschool.alugo.meetnrun.databinding.FragmentAddAppointmentBinding;
import com.tokioschool.alugo.meetnrun.model.User;
import com.tokioschool.alugo.meetnrun.util.Utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class AddAppointmentFragment extends Fragment {

    private FragmentAddAppointmentBinding binding;
    private HomeActivity mainActivity;
    private boolean isProfessional = false;
    private UserController uc;
    private User user;

    private TextView anythingTextView;
    private Spinner anythingSpinner;
    private Spinner daySpinner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AddAppointmentViewModel addAppointmentViewModel =
                new ViewModelProvider(this).get(AddAppointmentViewModel.class);

        binding = FragmentAddAppointmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        uc = new UserController(getContext());
        mainActivity = (HomeActivity) getActivity();
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainActivity.setTitle("");

        anythingTextView = (TextView) getView().findViewById(R.id.anythingTextField);
        anythingSpinner = (Spinner) getView().findViewById(R.id.anythingSpinner);
        daySpinner = (Spinner) getView().findViewById(R.id.daySpinner);

        user = mainActivity.currentUser;

        isProfessional = user.getProfessional_id() == null;
        loadForm();

    }

    private void loadForm(){


        List<String> days = new ArrayList<String>();
        days.add("");
        for (Utils.Day d: Utils.Day.values()) {
            days.add(d.name());
        }

        ArrayAdapter dayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, days);
        daySpinner.setAdapter(dayAdapter);

        if (isProfessional){
            anythingTextView.setText("Pacient");
            List<User> pacients = uc.getPacients(user);

            List<String> names = new ArrayList<>();

            for (User pacient:
                 pacients) {
                names.add(pacient.getName());
            }

            ArrayAdapter pacientAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, names);

            anythingSpinner.setAdapter(pacientAdapter);

        } else {
            anythingTextView.setVisibility(View.INVISIBLE);
            anythingSpinner.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}