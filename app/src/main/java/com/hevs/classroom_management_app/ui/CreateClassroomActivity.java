package com.hevs.classroom_management_app.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;
import com.hevs.classroom_management_app.viewModel.ClassroomViewModel;

public class CreateClassroomActivity extends AppCompatActivity {

    private ClassroomViewModel classroomViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_classroom);

        FloatingActionButton saveBtn = ((FloatingActionButton) findViewById(R.id.saveCreateClassroomButton));
        saveBtn.setOnClickListener(view -> createBtn());

        FloatingActionButton cancelBtn = ((FloatingActionButton) findViewById(R.id.cancelCreateClassButton));
        cancelBtn.setOnClickListener(view -> cancelBtn());
    }

    private void createBtn() {
        //Initialize the viewModel
        ClassroomViewModel.Factory factory = new ClassroomViewModel.Factory(getApplication(), 0L);
        classroomViewModel = ViewModelProviders.of(this, factory).get(ClassroomViewModel.class);

        EditText classroomNameEt = ((EditText) findViewById(R.id.classroomNameCreateEt));
        EditText classroomCapacityEt = ((EditText) findViewById(R.id.maxParticipantsCreateEt));

        // Gets input data
        String newCapacity_s = classroomCapacityEt.getText().toString();
        // Preliminary check
        if (newCapacity_s.equals("")) {
            classroomCapacityEt.setError("Field is mandatory");
            return;
        }
        int newCapacity;
        String newClassroomName = classroomNameEt.getText().toString();

        try {
            newCapacity = Integer.parseInt(newCapacity_s);
        } catch (NumberFormatException nfe) {
            classroomCapacityEt.setError("Capacity is invalid.");
            return;
        }
        // Checks data validity
        if (newCapacity < 1) {
            classroomCapacityEt.setError("Capacity must be at least 1");
            return;
        }
        int size = newClassroomName.length();
        if (size < 3 || size > 40) {
            classroomNameEt.setError("Name must be between 3 and 40 characters long");
            return;
        }
        // Creates the classroom and adds it to the DB
        Classroom newClassroom = new Classroom();
        newClassroom.setName(newClassroomName);
        newClassroom.setCapacity(newCapacity);
        classroomViewModel.createClassroom(newClassroom, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Toast toast = Toast.makeText(CreateClassroomActivity.this, getString(R.string.saved_successfully), Toast.LENGTH_SHORT);
                toast.show();
                NavUtils.navigateUpFromSameTask(CreateClassroomActivity.this);
            }
            @Override
            public void onFailure(Exception e) {
                Toast toast = Toast.makeText(CreateClassroomActivity.this, getString(R.string.unexpected_error), Toast.LENGTH_LONG);
                toast.show();
            }
        });

    }

    private void cancelBtn() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme).create();
        alertDialog.setTitle(R.string.deleteClassroomConfirm);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.confirm), (dialog, which) -> NavUtils.navigateUpFromSameTask(this));
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }
}
