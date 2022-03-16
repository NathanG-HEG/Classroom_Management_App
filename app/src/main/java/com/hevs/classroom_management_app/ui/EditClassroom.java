package com.hevs.classroom_management_app.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.database.repository.ClassroomRepository;

public class EditClassroom extends AppCompatActivity {

    private ClassroomRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_classroom);

        repo = ClassroomRepository.getInstance();

        FloatingActionButton deleteBtn = ((FloatingActionButton) findViewById(R.id.deleteClassroomButton));
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBtnAction();
            }
        });

        FloatingActionButton saveBtn = ((FloatingActionButton) findViewById(R.id.saveEditClassroomButton));
        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                saveBtnAction();
            }
        });
    }

    private void saveBtnAction(){
        EditText classroomNameEt = ((EditText) findViewById(R.id.classroomNameEditEt));
        EditText classroomCapacityEt = ((EditText) findViewById(R.id.maxParticipantsEt));

        // Gets input data
        int newCapacity = Integer.parseInt(classroomCapacityEt.getText().toString());
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

        //Create the updated Classroom in memory
        Classroom newClassroom = new Classroom(newClassroomName, newCapacity);

    }

    private void deleteBtnAction(){
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.deleteClassroomConfirm);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.confirm),(dialog, which) -> {
            //TODO: Delete classroom from Room DB
            System.out.println("DELETED!");
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), (dialog, which) -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }
}
