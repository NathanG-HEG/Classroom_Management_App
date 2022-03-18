package com.hevs.classroom_management_app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import com.hevs.classroom_management_app.BaseApp;
import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.async.teacher.CreateTeacher;
import com.hevs.classroom_management_app.database.entity.Teacher;
import com.hevs.classroom_management_app.database.repository.TeacherRepository;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;
import com.hevs.classroom_management_app.viewModel.TeacherViewModel;

import java.util.concurrent.atomic.AtomicBoolean;

public class SignUp extends AppCompatActivity {

    private TeacherRepository teacherRepository;

    //flag for compliance of all information
    private AtomicBoolean areInformationCompliant = new AtomicBoolean(true);

    //fields
    private EditText firstNameEt;
    private EditText lastNameEt;
    private EditText emailEt;
    private EditText passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        teacherRepository = ((BaseApp) getApplication()).getTeacherRepository();

        Button signUp = findViewById(R.id.signUp_button);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //set the flag for each try
                areInformationCompliant.set(true);

                //check entered information
                checkFirstName();
                checkLastName();
                checkEmail();
                checkPassword();

                //account creation
                if (areInformationCompliant.get()) {
                    Teacher teacher = new Teacher(lastNameEt.getText().toString(), firstNameEt.getText().toString(),
                            emailEt.getText().toString(), passwordEt.getText().toString());
                    teacherRepository = ((BaseApp) getApplication()).getTeacherRepository();
                    teacherRepository.insert(teacher, new OnAsyncEventListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getApplication().getApplicationContext(), "Welcome " + teacher.getFirstname(), Toast.LENGTH_LONG).show();
                            Intent i = new Intent(SignUp.this, CreateClassroomActivity.class);
                            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putLong(MainActivity.ID_TEACHER, teacher.getId());
                            editor.commit();
                            startActivity(i);
                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(getApplication().getApplicationContext(), "Internal error occurred, account was not created.", Toast.LENGTH_LONG).show();
                        }
                    }, getApplication());
                }
            }
        });
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void checkFirstName() {
        firstNameEt = findViewById(R.id.first_name_sign_up);
        if (firstNameEt.getText().toString().equals("")) {
            firstNameEt.setError("First name is required.");
            firstNameEt.requestFocus();
            areInformationCompliant.set(false);
        }
    }

    private void checkLastName() {
        //check last name
        lastNameEt = findViewById(R.id.last_name_sign_up);
        if (lastNameEt.getText().toString().equals("")) {
            lastNameEt.setError("Last name is required.");
            lastNameEt.requestFocus();
            areInformationCompliant.set(false);
        }
    }

    private void checkEmail() {
        //check email format
        emailEt = (EditText) findViewById(R.id.email_signUp);
        String email = emailEt.getText().toString();
        if (!isEmailValid(email)) {
            emailEt.setError("Invalid email format.");
            emailEt.requestFocus();
            areInformationCompliant.set(false);
        } else {
            //check that email is not already used
            teacherRepository.getByEmail(email, getApplication()).observe(SignUp.this, teacher -> {
                if (teacher != null) {
                    emailEt.setError("Email already used.");
                    emailEt.requestFocus();
                    areInformationCompliant.set(false);
                }
            });
        }
    }

    private void checkPassword() {
        //check password length
        passwordEt = (EditText) findViewById(R.id.password_signUp);
        String password = passwordEt.getText().toString();
        if (password.length() < 8) {
            passwordEt.setError("Password must be at least 8 characters long.");
            passwordEt.requestFocus();
            areInformationCompliant.set(false);
        } else {
            //check password correspondence
            String passwordConfirmation = ((EditText) findViewById(R.id.password_confirm_signUp)).getText().toString();
            if (!password.equals(passwordConfirmation)) {
                passwordEt.setError("Passwords do not match.");
                passwordEt.requestFocus();
                areInformationCompliant.set(false);
            }
        }
    }


}
