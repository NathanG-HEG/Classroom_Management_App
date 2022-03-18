package com.hevs.classroom_management_app.ui;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.database.repository.ClassroomRepository;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;

public class EditClassroom extends AppCompatActivity {

    private ClassroomRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_classroom);

        FloatingActionButton deleteBtn = ((FloatingActionButton) findViewById(R.id.deleteClassroomButton));
        deleteBtn.setOnClickListener(view -> deleteBtnAction());

        FloatingActionButton saveBtn = ((FloatingActionButton) findViewById(R.id.saveCreateClassroomButton));
        saveBtn.setOnClickListener(view -> saveBtnAction());
    }

    private void saveBtnAction(){
        EditText classroomNameEt = ((EditText) findViewById(R.id.classroomNameCreateEt));
        EditText classroomCapacityEt = ((EditText) findViewById(R.id.maxParticipantsCreateEt));

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
        if(size<3 || size > 40){
            classroomNameEt.setError("Name must be between 3 and 40 characters long");
            return;
        }
        // Updates the classroom and adds it to the DB
        long classroomId = getIntent().getExtras().getLong(ClassroomDetails.ID_CLASSROOM,1L);
        repo = ClassroomRepository.getInstance();
        Classroom newClassroom = new Classroom(newClassroomName, newCapacity);
        newClassroom.setId(classroomId);
        repo.update(newClassroom, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Toast toast = Toast.makeText(EditClassroom.this, getString(R.string.saved_successfully), Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast toast = Toast.makeText(EditClassroom.this, getString(R.string.unexpected_error), Toast.LENGTH_SHORT);
                toast.show();
            }
        }, getApplication());

    }

    private void deleteBtnAction(){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.deleteClassroomConfirm);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.confirm),(dialog, which) -> {
            // Updates the classroom and adds it to the DB
            long classroomId = getIntent().getExtras().getLong(ClassroomDetails.ID_CLASSROOM,1L);
            repo = ClassroomRepository.getInstance();
            Classroom classroom = new Classroom("", 0);
            classroom.setId(classroomId);
            repo.delete(classroom, new OnAsyncEventListener() {
                @Override
                public void onSuccess() {
                    Toast toast = Toast.makeText(EditClassroom.this, getString(R.string.deleted_successfully), Toast.LENGTH_SHORT);
                    toast.show();
                }

                @Override
                public void onFailure(Exception e) {
                    Toast toast = Toast.makeText(EditClassroom.this, getString(R.string.unexpected_error), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }, getApplication());
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }
}
