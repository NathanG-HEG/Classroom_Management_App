package com.hevs.classroom_management_app.ui;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.database.repository.ClassroomRepository;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;
import com.hevs.classroom_management_app.viewModel.ClassroomViewModel;


public class EditClassroom extends AppCompatActivity {

    private ClassroomRepository repo;
    private ClassroomViewModel classroomViewModel;
    private final long DEFAULT_CLASSROOM_ID = 1L;
    private long classroomId;
    private EditText classroomNameEt;
    private EditText classroomCapacityEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_classroom);

        classroomId = getIntent().getExtras().getLong(ClassroomDetails.ID_CLASSROOM, DEFAULT_CLASSROOM_ID);
        repo = ClassroomRepository.getInstance();

        viewInitialize();

        FloatingActionButton deleteBtn = ((FloatingActionButton) findViewById(R.id.deleteClassroomButton));
        deleteBtn.setOnClickListener(view -> deleteBtnAction());

        FloatingActionButton saveBtn = ((FloatingActionButton) findViewById(R.id.saveCreateClassroomButton));
        saveBtn.setOnClickListener(view -> saveBtnAction());
    }

    private void viewInitialize() {
        classroomNameEt = ((EditText) findViewById(R.id.classroomNameCreateEt));
        classroomCapacityEt = ((EditText) findViewById(R.id.maxParticipantsCreateEt));

        repo.getById(classroomId, getApplication()).observe(this, classroom -> {
            classroomNameEt.setText(classroom.getName());
            classroomCapacityEt.setText(Integer.toString(classroom.getCapacity()));
        });
    }

    private void saveBtnAction() {
        // Initializes the viewModel
        ClassroomViewModel.Factory factory = new ClassroomViewModel.Factory(getApplication(), classroomId);
        classroomViewModel = ViewModelProviders.of(this, factory).get(ClassroomViewModel.class);

        // Gets input data
        String newCapacity_s = classroomCapacityEt.getText().toString();
        // Preliminary check
        if (newCapacity_s.equals("")) {
            classroomCapacityEt.setError("Field is mandatory");
            return;
        }
        int newCapacity = Integer.parseInt(newCapacity_s);
        String newClassroomName = classroomNameEt.getText().toString();

        // Checks data validity
        if (newCapacity < 1) {
            classroomCapacityEt.setError("Capacity must be at least 1");
            return;
        }
        int size = newClassroomName.length();
        if (size < 3 || size > 15) {
            classroomNameEt.setError("Name must be between 3 and 40 characters long");
            return;
        }
        // Updates the classroom and adds it to the DB
        Classroom newClassroom = new Classroom();
        newClassroom.setName(newClassroomName);
        newClassroom.setCapacity(newCapacity);
        newClassroom.setId(classroomId);
        classroomViewModel.updateClassroom(newClassroom, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Toast toast = Toast.makeText(EditClassroom.this, getString(R.string.saved_successfully), Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onFailure(Exception e) {
                //Will throw an unique index exception
                if(e.getClass() == SQLiteConstraintException.class){
                    classroomNameEt.setError("Name is already taken. Try another one");
                    return;
                }
                Toast toast = Toast.makeText(EditClassroom.this, getString(R.string.unexpected_error), Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    private void deleteBtnAction() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.MyAlertDialogTheme).create();
        alertDialog.setTitle(R.string.deleteClassroomConfirm);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.confirm), (dialog, which) -> {
            Classroom classroomToDelete = new Classroom();
            classroomToDelete.setId(classroomId);
            repo.delete(classroomToDelete, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Toast toast = Toast.makeText(EditClassroom.this, getString(R.string.deleted_successfully), Toast.LENGTH_LONG);
                    toast.show();
                    Intent i = new Intent(EditClassroom.this, ClassroomListActivity.class);
                    startActivity(i);
                }

                @Override
                public void onFailure(Exception e) {
                    Toast toast = Toast.makeText(EditClassroom.this, getString(R.string.unexpected_error), Toast.LENGTH_LONG);
                    toast.show();
                }
            }, getApplication());
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> alertDialog.dismiss());
        alertDialog.show();
    }
}
