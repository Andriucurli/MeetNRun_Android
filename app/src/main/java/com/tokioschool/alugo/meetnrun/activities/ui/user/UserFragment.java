package com.tokioschool.alugo.meetnrun.activities.ui.user;

import static com.tokioschool.alugo.meetnrun.util.Utils.isBitSet;
import static com.tokioschool.alugo.meetnrun.util.Utils.setBit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.core.content.IntentCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.tokioschool.alugo.meetnrun.BuildConfig;
import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.activities.HomeActivity;
import com.tokioschool.alugo.meetnrun.activities.QRCodeActivity;
import com.tokioschool.alugo.meetnrun.activities.controllers.AppointmentController;
import com.tokioschool.alugo.meetnrun.activities.controllers.UserController;
import com.tokioschool.alugo.meetnrun.databinding.FragmentUserBinding;
import com.tokioschool.alugo.meetnrun.model.User;
import com.tokioschool.alugo.meetnrun.util.AlertHandler;
import com.tokioschool.alugo.meetnrun.util.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class UserFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private FragmentUserBinding binding;
    private EditText userTextField;
    private EditText surnameTextField;
    private EditText phoneTextField;
    private EditText emailTextField;
    private ImageView photoView;
    private Button changePhotoButton;
    private ActivityResultLauncher<Uri> changePhotolauncher;
    private ActivityResultLauncher<Intent> addUserLauncher;
    private Uri photoUri = null;

    private UserController uc;
    private AppointmentController ac;
    HomeActivity activity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserViewModel userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        setHasOptionsMenu(true);
        activity = (HomeActivity) getActivity();
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //final TextView textView = binding.textNotifications;
        //userViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        uc = new UserController(getContext());
        ac = new AppointmentController(getContext());
        addUserLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        return;
                    }
                });
        changePhotolauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean result) {
                        if (!result){
                            return;
                        }
                        Bitmap bitmap = null;
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                        photoView.setImageBitmap(bitmap);
                        try {
                            uc.setUserPhoto(activity.currentUser, photoUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.clear();
        inflater.inflate(R.menu.top_tab_menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.adduser_button:
                Intent intent = new Intent(getContext(), QRCodeActivity.class);
                addUserLauncher.launch(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        HomeActivity activity =  (HomeActivity) getActivity();
        activity.setTitle("");

        userTextField = (EditText) getView().findViewById(R.id.nameTextField);
        surnameTextField = (EditText) getView().findViewById(R.id.surnameTextField);
        phoneTextField = (EditText) getView().findViewById(R.id.phoneField);
        emailTextField = (EditText) getView().findViewById(R.id.emailTextField);

        surnameTextField.addTextChangedListener(new FormTextWatcher(R.id.surnameTextField, uc, activity.currentUser));
        phoneTextField.addTextChangedListener(new FormTextWatcher(R.id.phoneField, uc, activity.currentUser));
        emailTextField.addTextChangedListener(new FormTextWatcher(R.id.emailTextField, uc, activity.currentUser));

        photoView = (ImageView) getView().findViewById(R.id.userPhotoView);
        changePhotoButton = (Button) getView().findViewById(R.id.changePhotoButton);
        changePhotoButton.setOnClickListener(new ChangePhotoButtonListener());

        initForm();
    }


    private void initForm(){

        HomeActivity activity =  (HomeActivity) getActivity();

        User user = activity.currentUser;

        userTextField.setText(user.getName());
        surnameTextField.setText(user.getSurname());
        phoneTextField.setText(user.getPhone());
        emailTextField.setText(user.getEmail());

        if (user.getPhoto() != null){
            Bitmap bmp = BitmapFactory.decodeByteArray(user.getPhoto(), 0, user.getPhoto().length);
            photoView.setImageBitmap(Bitmap.createScaledBitmap(bmp, photoView.getWidth(), photoView.getHeight(), false));
        }

        byte[] scheduleBytes = user.getSchedule();

        int lastDay = 0;
        int hour = 0;
        for (int i = 0; i < scheduleBytes.length; i++){
            int dayId = i/3;
            if (lastDay != dayId){
                hour = 0;
                lastDay = dayId;
            }
            for(int j = 0; j < 8; j++,hour++){
                Utils.Day day = Utils.getDayByInt(dayId);
                int boxId = getResources().getIdentifier(String.format("%s_%02d_box",day.name(),hour), "id", getActivity().getPackageName());
                CheckBox box = (CheckBox) getView().findViewById(boxId);
                if (isBitSet(scheduleBytes[i], 7-j)){
                    box.setChecked(true);
                }
                box.setOnCheckedChangeListener(this);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String boxName = getResources().getResourceName(buttonView.getId());
        String[] elems = boxName.split("/")[1].split("_");
        Utils.Day day = Utils.Day.valueOf(elems[0]);

        byte[] schedule = activity.currentUser.getSchedule();

        int hour = Integer.parseInt(elems[1]);

        if (!isChecked && !ac.checkAppointment(activity.currentUser, day.ordinal(), hour)){
            buttonView.setChecked(true);
            Toast toast = AlertHandler.getWarningHourWithExistingAppointment(getContext());
            toast.show();
            return;
        }

        int bytei = hour/8;

        byte segmentByte = schedule[day.ordinal() * 3 + bytei];

        int biti = hour%8;

        schedule[day.ordinal() * 3 + bytei] = setBit(segmentByte, 7-biti, isChecked);

        uc.setSchedule(activity.currentUser, schedule);
    }

    private class FormTextWatcher implements TextWatcher {

        private final User user;
        private final int viewId;
        private final UserController userController;
        public FormTextWatcher(int view, UserController userController, User user){
            this.viewId = view;
            this.userController = userController;
            this.user = user;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            changeValue(s.toString());
        }

        private void changeValue(String s){
            switch (viewId){
                case R.id.surnameTextField:
                    user.setSurname(s);
                    break;
                case R.id.emailTextField:
                    user.setEmail(s);
                    break;
                case R.id.phoneField:
                    user.setPhone(s);
                    break;
                default:
                    return;
            }
            uc.updateUser(user);
        }

    }

    private class ChangePhotoButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = null;

            try {
                image = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            if (image != null) {
                photoUri = FileProvider.getUriForFile(getContext(),
                        BuildConfig.APPLICATION_ID + ".provider",
                        image);
            }

            changePhotolauncher.launch(photoUri);
        }
    }

}