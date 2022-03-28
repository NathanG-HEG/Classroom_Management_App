package com.hevs.classroom_management_app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hevs.classroom_management_app.database.entity.Teacher;
import com.hevs.classroom_management_app.database.repository.TeacherRepository;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;

public class TeacherViewModel extends AndroidViewModel {

    private TeacherRepository teacherRepository;
    //private Context applicationContext;
    private Application application;
    private final MediatorLiveData<Teacher> observableTeacher;

    public TeacherViewModel(@NonNull Application application, TeacherRepository teacherRepository, final long id) {
        super(application);

        this.teacherRepository = teacherRepository;

        this.application = application;

        observableTeacher = new MediatorLiveData<>();
        observableTeacher.setValue(null);

        LiveData<Teacher> teacher = teacherRepository.getById(id, application);

        // observe the changes of the client entity from the database and forward them
        observableTeacher.addSource(teacher, observableTeacher::setValue);

    }



    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final long id;

        private final TeacherRepository repository;

        public Factory(@NonNull Application application, long id) {
            this.application = application;
            this.id = id;
            repository = TeacherRepository.getInstance();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new TeacherViewModel(application, repository, id);
        }
    }

    public LiveData<Teacher> getTeacher() {
        return observableTeacher;
    }

    public void createClient(Teacher teacher, OnAsyncEventListener callback) {
        teacherRepository.insert(teacher, callback, application);
    }

    public void updateClient(Teacher teacher, OnAsyncEventListener callback) {
        teacherRepository.update(teacher, callback, application);
    }

    public void deleteClient(Teacher teacher, OnAsyncEventListener callback) {
        teacherRepository.delete(teacher, callback, application);
    }

}
