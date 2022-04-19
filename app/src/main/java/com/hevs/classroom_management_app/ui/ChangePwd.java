package com.hevs.classroom_management_app.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hevs.classroom_management_app.R;

public class ChangePwd extends AppCompatActivity {

    FirebaseAuth mAuth;
    private EditText oldPwdField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pwd);

        mAuth = FirebaseAuth.getInstance();

        oldPwdField = findViewById(R.id.old_password_edit);

        Button changePwd = (Button) findViewById(R.id.change_pwd_btn2);
        changePwd.setOnClickListener(view -> changePwd());
    }

    private void changePwd() {
        // Re authenticate the teacher
        FirebaseUser user = mAuth.getCurrentUser();
        // Get auth credentials from the user for re-authentication.
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), oldPwdField.getText().toString());

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sendEmail(user);
                    } else {
                        Toast.makeText(ChangePwd.this, "Incorrect password.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendEmail(FirebaseUser user) {
        mAuth.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ChangePwd.this, "A reset password mail has been sent", Toast.LENGTH_LONG).show();
            }
        });
    }

}