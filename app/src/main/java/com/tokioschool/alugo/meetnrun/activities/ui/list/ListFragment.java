package com.tokioschool.alugo.meetnrun.activities.ui.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.activities.HomeActivity;
import com.tokioschool.alugo.meetnrun.activities.SettingsActivity;
import com.tokioschool.alugo.meetnrun.controllers.AppointmentController;
import com.tokioschool.alugo.meetnrun.adapters.AppointmentViewAdapter;
import com.tokioschool.alugo.meetnrun.databinding.FragmentListBinding;

import java.util.List;

public class ListFragment extends Fragment {

    private FragmentListBinding binding;
    private HomeActivity homeActivity;
    private AppointmentController ac;
    private RecyclerView appointmentsRV;
    private AppointmentViewAdapter adapter;
    private ActivityResultLauncher<Intent> settingsLauncher;
    private TextView noAppointmentsTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ListViewModel listViewModel =
                new ViewModelProvider(this).get(ListViewModel.class);

        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        homeActivity = (HomeActivity) getActivity();
        setHasOptionsMenu(true);
        homeActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        settingsLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        return;
                    }
                });


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        ac = new AppointmentController(getContext());

        noAppointmentsTextView = (TextView) getView().findViewById(R.id.noAppointments_TextView);
        appointmentsRV = (RecyclerView) getView().findViewById(R.id.appointmentRecyclerView);
        appointmentsRV.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        appointmentsRV.setLayoutManager(llm);

        adapter = new AppointmentViewAdapter(getContext(), homeActivity.getCurrentUser());

        appointmentsRV.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        List data = ac.getActiveAppointments(homeActivity.getCurrentUser());

        if (data.isEmpty()){
            noAppointmentsTextView.setVisibility(View.VISIBLE);
            appointmentsRV.setVisibility(View.GONE);
        } else {
            noAppointmentsTextView.setVisibility(View.GONE);
            appointmentsRV.setVisibility(View.VISIBLE);
            adapter.setData(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();
        inflater.inflate(R.menu.top_tab_menu_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.settings_menu_button:
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                settingsLauncher.launch(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}