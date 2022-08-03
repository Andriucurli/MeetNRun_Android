package com.tokioschool.alugo.meetnrun.activities;

import static com.tokioschool.alugo.meetnrun.util.Utils.CALL_REQUEST_CODE;
import static com.tokioschool.alugo.meetnrun.util.Utils.WRITE_CALENDAR_CODE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tokioschool.alugo.meetnrun.R;
import com.tokioschool.alugo.meetnrun.adapters.CustomSpinnerAdapter;
import com.tokioschool.alugo.meetnrun.controllers.AppointmentController;
import com.tokioschool.alugo.meetnrun.controllers.NotificationController;
import com.tokioschool.alugo.meetnrun.model.Appointment;
import com.tokioschool.alugo.meetnrun.model.Contracts;
import com.tokioschool.alugo.meetnrun.model.Notification;
import com.tokioschool.alugo.meetnrun.model.User;
import com.tokioschool.alugo.meetnrun.util.AlertHandler;
import com.tokioschool.alugo.meetnrun.util.Utils;

import java.util.Calendar;
import java.util.TimeZone;

public class AppointmentActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Appointment appointment;
    private TextView appointmentAnythingTextView;
    private TextView appointmentTimeTextView;
    private ImageView appointmentAnythingImageView;
    private User anything;

    private Spinner daySpinner;
    private Spinner hourSpinner;

    AlertDialog modifyAlert = null;
    Integer day = null;
    Integer hour = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        appointmentAnythingTextView = findViewById(R.id.appointmentAnythingTextView);
        appointmentTimeTextView =  findViewById(R.id.appointmentTimeTextView);
        appointmentAnythingImageView =  findViewById(R.id.appointmentAnythingImageView);

        ImageButton callButton =  findViewById(R.id.appointmentCallButton);
        ImageButton addRecordatoryButton =  findViewById(R.id.appointmentAddRecordatoryButton);
        ImageButton mailButton =  findViewById(R.id.appointmentMailButton);

        Button cancelButton =  findViewById(R.id.cancelAppointmentButton);
        Button modifyButton =  findViewById(R.id.modifyAppointmentButton);

        callButton.setOnClickListener(this);
        addRecordatoryButton.setOnClickListener(this);
        mailButton.setOnClickListener(this);

        cancelButton.setOnClickListener(this);
        modifyButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent requestIntent = getIntent();
        int appointment_id = requestIntent.getIntExtra(Contracts.AppointmentEntry.ID, -1);

        if (appointment_id == -1){
            return;
        }

        if (currentUser == null){
            return;
        }
        AppointmentController appointmentController = new AppointmentController(this);
        appointment = appointmentController.getAppointment(appointment_id);

        if (appointment != null){
            anything = userController.getUser(currentUser.isProfessional()? appointment.getUser_id() : appointment.getProfessional_id());
            setTitle(String.format(getString(R.string.appointment_with), anything.getName()));
            if (anything.getPhoto() != null){
                Bitmap bmp = BitmapFactory.decodeByteArray(anything.getPhoto(), 0, anything.getPhoto().length);
                appointmentAnythingImageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, 120, 120, false));
            }
            appointmentAnythingTextView.setText(anything.getSurname());
            Utils.Day day = Utils.getDayByInt(appointment.getDay());
            int dayNameId = getResources().getIdentifier(day.name(), "string", this.getPackageName());
            appointmentTimeTextView.setText(String.format("%s, %02d:00", getString(dayNameId) , appointment.getHour()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_REQUEST_CODE){
            for (int i = 0; i < permissions.length; i++){
                if (permissions[i].equals(Manifest.permission.CALL_PHONE) &&
                        grantResults[i] == PackageManager.PERMISSION_GRANTED){
                }
            }
        }
    }

    private void modifyAppointment(){

        AlertDialog.Builder modifyBuilder = new AlertDialog.Builder(this);
        modifyBuilder.setMessage("Select new schedule");

        //inflar layout
        View alertView = getLayoutInflater().inflate(R.layout.alert_spinners_view, null);
        //Crear Spinner de dia
        daySpinner = alertView.findViewById(R.id.modificationDaySpinner);
        CustomSpinnerAdapter dayAdapter = CustomSpinnerAdapter.getDaySpinnerAdapter(this);
        daySpinner.setAdapter(dayAdapter);

        //Crear Spinner de hora
        hourSpinner = alertView.findViewById(R.id.modificationHourSpinner);
        hourSpinner.setEnabled(false);

        daySpinner.setOnItemSelectedListener(this);
        hourSpinner.setOnItemSelectedListener(this);

        //Crear botón de modificación

        Button confirmAppointmentModificationButton = alertView.findViewById(R.id.confirmAppointmentModificationButton);
        confirmAppointmentModificationButton.setOnClickListener(this);

        modifyBuilder.setView(alertView);
        modifyAlert = modifyBuilder.create();
        modifyAlert.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.modificationDaySpinner:
                if (position == 0){
                    return;
                }
                day = position - 1;
                CustomSpinnerAdapter hourAdapter = CustomSpinnerAdapter.getHourSpinnerAdapter(this, currentUser, day);
                hourSpinner.setAdapter(hourAdapter);
                hourAdapter.notifyDataSetChanged();
                hourSpinner.setEnabled(true);
                break;
            case R.id.modificationHourSpinner:
                if (position == 0){
                    return;
                }
                String hourStr = (String) hourSpinner.getAdapter().getItem(position-1);
                hour = Integer.parseInt(hourStr.split(":")[0]);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        switch (parent.getId()){
            case R.id.modificationDaySpinner:
                hourSpinner.setEnabled(false);
                day = null;
                hour = null;
                break;
            default:
                hour = null;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        AppointmentController appointmentController;
        NotificationController notificationController;
        switch (v.getId()){
            case R.id.confirmAppointmentModificationButton:
                if (hour == null || day == null){
                    Toast toast = AlertHandler.getWarningEmptyFields(this);
                    toast.show();
                }
                appointmentController = new AppointmentController(this);
                notificationController = new NotificationController(this);
                if (currentUser.isProfessional()){
                    appointmentController.modifyAppointment(appointment.getAppointment_id(), day, hour);
                    Utils.Day dayAux = Utils.getDayByInt(day);
                    int dayStringId = getResources().getIdentifier(dayAux.name(), "string", getPackageName());
                    notificationController.createNotification(appointment.getProfessional_id(),
                            appointment.getUser_id(), String.format("The appointment with %s was changed to %s, %02d:00", currentUser.getSurname(), getString(dayStringId), hour),
                            Notification.Type.MODIFIED, appointment.getAppointment_id());
                } else {
                    int newAppointment_id = (int) appointmentController.requestAppointment(appointment.getProfessional_id(), appointment.getUser_id(), day, hour);

                    if (newAppointment_id == -1)
                    {
                        return;
                    }
                    appointmentController.requestModification(appointment.getAppointment_id());
                    Utils.Day dayAux = Utils.getDayByInt(day);
                    int dayStringId = getResources().getIdentifier(dayAux.name(), "string", getPackageName());
                    notificationController.createNotification(appointment.getUser_id(),
                            appointment.getProfessional_id(), String.format("The appointment with %s was changed to %s, %02d:00", currentUser.getSurname(), getString(dayStringId), hour),
                            Notification.Type.NEED_MODIFICATION, newAppointment_id);
                }
                modifyAlert.dismiss();
                finishActivity(0);
                break;
            case R.id.modifyAppointmentButton:
                modifyAppointment();
                break;
            case R.id.cancelAppointmentButton:
                appointmentController = new AppointmentController(this);
                notificationController = new NotificationController(this);

                AlertDialog.Builder cancelBuilder = new AlertDialog.Builder(this);
                cancelBuilder.setMessage(R.string.are_you_sure);
                cancelBuilder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
                cancelBuilder.setPositiveButton(R.string.confirm, ((dialog, which) -> {
                    if (appointmentController.cancelAppointment(appointment.getAppointment_id())){
                        if (currentUser.isProfessional()){
                            notificationController.createNotification(appointment.getProfessional_id(), appointment.getUser_id(),
                                    String.format(getString(R.string.description_notification_cancel), currentUser.getName()), Notification.Type.CANCELLED, appointment.getAppointment_id());
                        } else {
                            notificationController.createNotification(appointment.getUser_id(), appointment.getProfessional_id(),
                                    String.format(getString(R.string.description_notification_cancel), currentUser.getName()), Notification.Type.CANCELLED, appointment.getAppointment_id());
                        }
                        finish();
                    }
                }));
                AlertDialog alert = cancelBuilder.create();
                alert.show();
                break;
            case R.id.appointmentCallButton:
                Intent call = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + anything.getPhone()));
                if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, CALL_REQUEST_CODE);
                } else {
                    startActivity(call);
                }
                break;
            case R.id.appointmentAddRecordatoryButton:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, WRITE_CALENDAR_CODE);
                } else {
                    Calendar calendar = Utils.nextDayOfWeek(Utils.getCalendarDay(appointment.getDay()));
                    calendar.set(Calendar.HOUR, appointment.getHour());
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.setTimeZone(TimeZone.getDefault());

                    long start = calendar.getTimeInMillis();
                    long end = calendar.getTimeInMillis() + 60 * 60 * 1000;

                    Intent intent = new Intent(Intent.ACTION_INSERT)
                            .setData(CalendarContract.Events.CONTENT_URI)
                            .setType("vnd.android.cursor.item/event")
                            .putExtra(CalendarContract.Events.TITLE, String.format("Appointment with %s", anything.getSurname()))
                            .putExtra(CalendarContract.Events.RRULE, "FREQ=WEEKLY;BYDAY="+Utils.getDayByInt(appointment.getDay()).name().toUpperCase()+";")
                            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start)
                            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);

                    startActivity(intent);
                }
                break;
            case R.id.appointmentMailButton:
                Intent emailSend = new Intent(Intent.ACTION_SENDTO);
                emailSend.setData(Uri.parse("mailto:")); // only email apps should handle this
                emailSend.putExtra(Intent.EXTRA_EMAIL, new String[]{anything.getEmail()});
                if (emailSend.resolveActivity(getPackageManager()) != null){
                    startActivity(emailSend);
                }
                break;
        }
    }
}