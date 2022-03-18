package com.hevs.classroom_management_app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hevs.classroom_management_app.BaseApp;
import com.hevs.classroom_management_app.database.entity.Classroom;
import com.hevs.classroom_management_app.database.repository.ClassroomRepository;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;

public class ClassroomViewModel extends AndroidViewModel {

    private Application application;
    private ClassroomRepository repo;
    private final MediatorLiveData<Classroom> observableClassroom;

    public ClassroomViewModel(@NonNull Application application, final long classroomId, ClassroomRepository classroomRepository) {
        super(application);
        this.application = application;
        this.repo = classroomRepository;
        observableClassroom = new MediatorLiveData<>();
        observableClassroom.setValue(null);
        LiveData<Classroom> classroom = repo.getById(classroomId, application);
        // observe the changes of the account entity from the database and forward them
        observableClassroom.addSource(classroom, observableClassroom::setValue);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application application;
        private final long classroomId;
        private final ClassroomRepository repo;

        public Factory(@NonNull Application application, long classroomId){
            this.application = application;
            this.classroomId = classroomId;
            repo = ((BaseApp) application).getClassroomRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new ClassroomViewModel(application, classroomId, repo);
        }
    }

    public LiveData<Classroom> getClassroom(){
        return observableClassroom;
    }

    public void createClassroom(Classroom classroom, OnAsyncEventListener callback){
        repo.insert(classroom, callback, application);
    }

    public void updateClassroom(Classroom classroom, OnAsyncEventListener callback){
        repo.update(classroom, callback, application);
    }
}
