package com.hevs.classroom_management_app.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hevs.classroom_management_app.BaseApp;
import com.hevs.classroom_management_app.database.entity.Reservation;
import com.hevs.classroom_management_app.database.repository.ReservationRepository;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;

public class ReservationViewModel extends AndroidViewModel {

    private ReservationRepository repo;
    private final MediatorLiveData<Reservation> observableReservation;

    public ReservationViewModel(@NonNull Application application, final String reservationId, final String classroomId, ReservationRepository reservationRepository) {
        super(application);
        this.repo = reservationRepository;
        observableReservation = new MediatorLiveData<>();
        observableReservation.setValue(null);
        LiveData<Reservation> reservation = repo.getById(reservationId, classroomId);
        // observe the changes of the account entity from the database and forward them
        observableReservation.addSource(reservation, observableReservation::setValue);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {
        @NonNull
        private final Application application;
        private final String reservationId;
        private final String classroomId;
        private final ReservationRepository repo;

        public Factory(@NonNull Application application, String reservationId, String classroomId){
            this.application = application;
            this.reservationId = reservationId;
            this.classroomId = classroomId;
            repo = ((BaseApp) application).getReservationRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            return (T) new ReservationViewModel(application, reservationId, classroomId, repo);
        }
    }

    public LiveData<Reservation> getReservation(){
        return observableReservation;
    }

    public void createReservation(Reservation reservation, OnAsyncEventListener callback){
        repo.insert(reservation, callback);
    }

    public void updateReservation(Reservation reservation, OnAsyncEventListener callback){
        repo.update(reservation, callback);
    }


    public void deleteReservation(Reservation reservation, OnAsyncEventListener callback) {
        repo.delete(reservation, callback);
    }
}
