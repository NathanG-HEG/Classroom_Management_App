package com.hevs.classroom_management_app.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.ViewModelProviders;

import com.hevs.classroom_management_app.R;
import com.hevs.classroom_management_app.database.entity.Teacher;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;
import com.hevs.classroom_management_app.viewModel.TeacherViewModel;

public class ChangePwd extends AppCompatActivity {

    private SharedPreferences sharedPref;
        private Teacher teacher;
    private TeacherViewModel teacherViewModel;
    private EditText oldPwdField, newPwdField1, newPwdField2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pwd);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        oldPwdField = findViewById(R.id.old_password_edit);
        newPwdField1 = findViewById(R.id.password_edit1);
        newPwdField2 = findViewById(R.id.password_edit2);


        TeacherViewModel.Factory factory = new TeacherViewModel.Factory(getApplication(), sharedPref.getLong(MainActivity.ID_TEACHER, 0L));
        teacherViewModel = ViewModelProviders.of(this, factory).get(TeacherViewModel.class);
        teacherViewModel.getTeacher().observe(this, teacher1 -> teacher = teacher1);

        Button changePwd = (Button) findViewById(R.id.change_pwd_btn2);
        changePwd.setOnClickListener(view -> changePwd());
    }

    private void changePwd(){
        if(!oldPwdField.getText().toString().equals(teacher.getPassword())){
            oldPwdField.setError(getString(R.string.incorrect_password));
            return;
        }
        String newPwd1 = newPwdField1.getText().toString();
        String newPwd2 = newPwdField2.getText().toString();

        if(!newPwd1.equals(newPwd2)){
            newPwdField2.setError(getString(R.string.password_no_match));
            return;
        }

        if(newPwd1.length()<8){
            newPwdField1.setError(getString(R.string.password_criteria));
            return;
        }

        teacher.setPassword(newPwd1);

        teacherViewModel.updateClient(teacher, new OnAsyncEventListener() {
            @Override
            public void onSuccess() {
                Toast toast = Toast.makeText(getApplication(), R.string.saved_successfully, Toast.LENGTH_SHORT);
                toast.show();
                NavUtils.navigateUpFromSameTask(ChangePwd.this);
            }

            @Override
            public void onFailure(Exception e) {
                Toast toast = Toast.makeText(getApplication(), R.string.unexpected_error, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }


}
