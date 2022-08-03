package com.tokioschool.alugo.meetnrun.activities.ui.addAppointment;

import android.content.Context;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.activities.HomeActivity;
import com.tokioschool.alugo.meetnrun.controllers.AppointmentController;
import com.tokioschool.alugo.meetnrun.controllers.NotificationController;
import com.tokioschool.alugo.meetnrun.controllers.UserController;
import com.tokioschool.alugo.meetnrun.adapters.CustomSpinnerAdapter;
import com.tokioschool.alugo.meetnrun.databinding.FragmentAddAppointmentBinding;
import com.tokioschool.alugo.meetnrun.model.Notification;
import com.tokioschool.alugo.meetnrun.model.User;
import com.tokioschool.alugo.meetnrun.util.AlertHandler;
import com.tokioschool.alugo.meetnrun.util.Utils;

import java.util.ArrayList;
import java.util.List;


public class AddAppointmentFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentAddAppointmentBinding binding;
    private HomeActivity mainActivity;
    private AppointmentController ac;

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

        ac = new AppointmentController(getContext());
        mainActivity = (HomeActivity) getActivity();
        mainActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainActivity.setTitle("");

        anythingTextView = (TextView) getView().findViewById(R.id.anythingTextField);
        anythingSpinner = (Spinner) getView().findViewById(R.id.anythingSpinner);
        daySpinner = (Spinner) getView().findViewById(R.id.daySpinner);
        hourSpinner = (Spinner) getView().findViewById(R.id.hourSpinner);
        addAppointmentButton = (Button) getView().findViewById(R.id.requestCreateButton);

        anythingSpinner.setOnItemSelectedListener(this);
        daySpinner.setOnItemSelectedListener(this);
        hourSpinner.setOnItemSelectedListener(this);
        addAppointmentButton.setOnClickListener(new AddAppointmentListener(getContext()));

        anythingSpinner.setEnabled(false);
        daySpinner.setEnabled(false);
        hourSpinner.setEnabled(false);

        daySpinner.setAdapter(CustomSpinnerAdapter.getDaySpinnerAdapter(getContext()));

    }

    private void initForm(){

        if (mainActivity.getCurrentUser().isProfessional()){
            anythingTextView.setText(R.string.pacient);
            addAppointmentButton.setText(R.string.create_appointment);
            pacients = mainActivity.getUserController().getPacients(mainActivity.getCurrentUser());
            List<Pair<Integer, String>> names = new ArrayList<>();
            for (User pacient:
                    pacients) {
                names.add(new Pair<>(pacient.getId(), pacient.getSurname()));
            }

            CustomSpinnerAdapter pacientAdapter = new CustomSpinnerAdapter(getContext(), names);

            anythingSpinner.setEnabled(true);
            anythingSpinner.setAdapter(pacientAdapter);

        } else {
            addAppointmentButton.setText(R.string.request_appointment);
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
                    CustomSpinnerAdapter hourAdapter = CustomSpinnerAdapter.getHourSpinnerAdapter(getContext(), mainActivity.getCurrentUser(), day);
                    hourSpinner.setEnabled(true);
                    hourSpinner.setAdapter(hourAdapter);
                }
                break;
            default:
                if (position == 0){
                    return;
                } else {

                    Pair<Integer, String> hourPair = (Pair<Integer, String>) hourSpinner.getAdapter().getItem(position - 1);
                    hour = hourPair.first;
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
        private final NotificationController nc;

        public AddAppointmentListener(Context context) {
            this.context = context;
            this.ac = new AppointmentController(context);
            this.uc = new UserController(context);
            this.nc = new NotificationController(context);
        }

        @Override
        public void onClick(View v) {

            if (hour == null || day == null ||
                    (mainActivity.getCurrentUser().isProfessional() && pacientPos == null )){
                Toast toast = AlertHandler.getWarningEmptyFields(getContext());
                toast.show();
                return;
            } else {
                if (mainActivity.getCurrentUser().isProfessional()){
                    User pacient = pacients.get(pacientPos);
                    long appointment_id = ac.createAppointment(mainActivity.getCurrentUser().getId(), pacient.getId(), day, hour);

                    if (appointment_id == -1){
                        return;
                    }
                    Utils.Day dayAux = Utils.getDayByInt(day);
                    int dayNameId = context.getResources().getIdentifier(dayAux.name(), "string", context.getPackageName());
                    nc.createNotification(mainActivity.getCurrentUser().getId(), pacient.getId(),
                            String.format(context.getString(R.string.description_notification_created), mainActivity.getCurrentUser().getName(), context.getString(dayNameId) , hour), Notification.Type.CREATED, (int) appointment_id);

                } else {
                    long appointment_id = ac.requestAppointment(mainActivity.getCurrentUser().getProfessional_id(), mainActivity.getCurrentUser().getId(), day, hour);
                    if (appointment_id == -1){
                        return;
                    }
                    Utils.Day dayAux = Utils.getDayByInt(day);
                    int dayNameId = context.getResources().getIdentifier(dayAux.name(), "string", context.getPackageName());
                    nc.createNotification(mainActivity.getCurrentUser().getId(), mainActivity.getCurrentUser().getProfessional_id(),
                            String.format(context.getString(R.string.description_notification_needsConfirmation), mainActivity.getCurrentUser().getName(), context.getString(dayNameId), hour), Notification.Type.NEED_CONFIRMATION, (int) appointment_id);
                }
                Toast toast = AlertHandler.getInfoAppointmentCreated(getContext());
                toast.show();
            }
        }
    }
}