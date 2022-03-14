package com.hevs.classroom_management_app.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hevs.classroom_management_app.BaseApp;
import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.repository.TeacherRepository;

import java.util.concurrent.atomic.AtomicBoolean;

public class SignUp extends AppCompatActivity {

    private TeacherRepository teacherRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        teacherRepository = ((BaseApp) getApplication()).getTeacherRepository();

        Button signUp = findViewById(R.id.signUp_button);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //reset both error fields
                ((TextView)findViewById(R.id.signUp_email_error_message)).setText("");
                ((TextView)findViewById(R.id.signUp_password_error_message)).setText("");

                //flag for compliance of all information
                AtomicBoolean areInformationCompliant = new AtomicBoolean(true);

                //check email format
                String email = ((EditText)findViewById(R.id.email_signUp)).getText().toString();
                if (!isEmailValid(email)){
                    ((TextView)findViewById(R.id.signUp_email_error_message)).setText("Invalid email format.");
                    areInformationCompliant.set(false);
                } else {
                    //check that email is not already used
                    teacherRepository.getByEmail(email, getApplication()).observe(SignUp.this, teacher -> {
                        if (teacher != null) {
                            ((TextView)findViewById(R.id.signUp_email_error_message)).setText("Email already used.");
                            areInformationCompliant.set(false);
                        }
                    });
                }

                //check password length
                String password = ((EditText) findViewById(R.id.password_signUp)).getText().toString();
                if (password.length() < 8){
                    ((TextView)findViewById(R.id.signUp_password_error_message)).setText("Password must be at least 8 characters long.");
                    areInformationCompliant.set(false);
                } else {
                    //check password correspondence
                    String passwordConfirmation = ((EditText) findViewById(R.id.password_confirm_signUp)).getText().toString();
                    if (!password.equals(passwordConfirmation)){
                        ((TextView)findViewById(R.id.signUp_password_error_message)).setText("Passwords do not match.");
                        areInformationCompliant.set(false);
                    }
                }

                //account creation
                if (areInformationCompliant.get()) {

                }
            }
        });
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
