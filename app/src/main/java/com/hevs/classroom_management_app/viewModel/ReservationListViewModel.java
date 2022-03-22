package com.hevs.classroom_management_app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hevs.classroom_management_app.BaseApp;
import com.hevs.classroom_management_app.database.dao.ReservationDao;
import com.hevs.classroom_management_app.database.pojo.ReservationWithTeacher;
import com.hevs.classroom_management_app.database.repository.ReservationRepository;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;

import java.util.List;

public class ReservationListViewModel extends AndroidViewModel {
    private Application application;
    private ReservationRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<ReservationWithTeacher>> observableReservationWithTeacher;

    public ReservationListViewModel(@NonNull Application application, final long classroomId, ReservationRepository reservationRepository) {
        super(application);

        this.application = application;

        repository = reservationRepository;

        observableReservationWithTeacher = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableReservationWithTeacher.setValue(null);

        LiveData<List<ReservationWithTeacher>> teacherReservations =
                reservationRepository.getReservationsByClassID(classroomId, application);

        // observe the changes of the entities from the database and forward them
        observableReservationWithTeacher.addSource(teacherReservations, observableReservationWithTeacher::setValue);
    }


    /**
     * A creator is used to inject the account id into the ViewModel
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final long classRoomId;

        private final ReservationRepository reservationRepository;

        public Factory(@NonNull Application application, long classRoomId) {
            this.application = application;
            this.classRoomId = classRoomId;
            reservationRepository = ((BaseApp) application).getReservationRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new ReservationListViewModel(application, classRoomId, reservationRepository);
        }
    }


    /**
     * Expose the LiveData ClientAccounts query so the UI can observe it.
     */
    public LiveData<List<ReservationWithTeacher>> getReservationWithTeachers() {
        return observableReservationWithTeacher;
    }

    public void deleteReservation(ReservationWithTeacher reservationWithTeacher, OnAsyncEventListener callback) {
        repository.delete(reservationWithTeacher.reservation, callback, application);
    }


    /*
    public void executeTransaction(final AccountEntity sender, final AccountEntity recipient,
                                   OnAsyncEventListener callback) {
        repository.transaction(sender, recipient, callback, application);
    }*/
}
