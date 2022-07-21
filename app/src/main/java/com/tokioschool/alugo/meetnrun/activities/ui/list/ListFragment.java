package com.tokioschool.alugo.meetnrun.activities.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.activities.HomeActivity;
import com.tokioschool.alugo.meetnrun.activities.controllers.AppointmentController;
import com.tokioschool.alugo.meetnrun.adapters.AppointmentViewAdapter;
import com.tokioschool.alugo.meetnrun.adapters.NotificationViewAdapter;
import com.tokioschool.alugo.meetnrun.databinding.FragmentListBinding;
import com.tokioschool.alugo.meetnrun.model.Notification;

import java.util.List;

public class ListFragment extends Fragment {

    private FragmentListBinding binding;
    private HomeActivity homeActivity;
    private AppointmentController ac;
    private RecyclerView appointmentsRV;
    private AppointmentViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ListViewModel listViewModel =
                new ViewModelProvider(this).get(ListViewModel.class);

        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        homeActivity = (HomeActivity) getActivity();
        homeActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //final TextView textView = binding.textDashboard;
        //listViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        ac = new AppointmentController(getContext());

        appointmentsRV = (RecyclerView) getView().findViewById(R.id.appointmentRecyclerView);
        appointmentsRV.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        appointmentsRV.setLayoutManager(llm);

        adapter = new AppointmentViewAdapter(getContext(), homeActivity.currentUser);

        appointmentsRV.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        List data = ac.getAppointments(homeActivity.currentUser);
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}