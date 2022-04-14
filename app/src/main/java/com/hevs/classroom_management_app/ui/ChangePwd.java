package com.hevs.classroom_management_app.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.entity.Teacher;
import com.hevs.classroom_management_app.database.repository.TeacherRepository;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;
import com.hevs.classroom_management_app.viewModel.TeacherViewModel;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChangePwd extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private Teacher teacher;
    private EditText oldPwdField, newPwdField1, newPwdField2;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pwd);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        mAuth = FirebaseAuth.getInstance();

        oldPwdField = findViewById(R.id.old_password_edit);

        TeacherRepository.getInstance().getById(sharedPref.getString(MainActivity.ID_TEACHER, null))
                .observe(this, teacher1 -> teacher = teacher1);

        Button changePwd = (Button) findViewById(R.id.change_pwd_btn2);
        changePwd.setOnClickListener(view -> changePwd());
    }

    private void changePwd(){
        // Re authenticate the teacher
        FirebaseUser user = mAuth.getCurrentUser();
        mAuth.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ChangePwd.this, "A reset password mail has been sent", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}