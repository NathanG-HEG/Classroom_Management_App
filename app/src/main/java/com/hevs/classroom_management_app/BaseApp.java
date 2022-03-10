package com.hevs.classroom_management_app;

import android.app.Application;

import com.hevs.classroom_management_app.database.AppDatabase;
import com.hevs.classroom_management_app.database.repository.ClassroomRepository;
import com.hevs.classroom_management_app.database.repository.ReservationRepository;
import com.hevs.classroom_management_app.database.repository.TeacherRepository;

public class BaseApp extends Application {

        @Override
        public void onCreate() {
            super.onCreate();
        }

        public AppDatabase getDatabase() {
            return AppDatabase.getInstance(this);
        }

        public TeacherRepository getTeacherRepository() {
            return TeacherRepository.getInstance();
        }

        public ClassroomRepository getClassroomRepository() {return ClassroomRepository.getInstance();}

        public ReservationRepository getReservationRepository(){return ReservationRepository.getInstance();}
}
