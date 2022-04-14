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
import com.hevs.classroom_management_app.database.firebase.ReservationsListLiveData;
import com.hevs.classroom_management_app.database.repository.ReservationRepository;
import com.hevs.classroom_management_app.util.OnAsyncEventListener;

import java.util.List;

public class ReservationListViewModel extends AndroidViewModel {
    private Application application;
    private ReservationRepository repository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<Reservation>> observableReservations;

    public ReservationListViewModel(@NonNull Application application, final String classroomId, ReservationRepository reservationRepository) {
        super(application);

        this.application = application;

        repository = reservationRepository;

        observableReservations = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        observableReservations.setValue(null);

        ReservationsListLiveData teacherReservations =
                reservationRepository.getReservationsByClassId(classroomId);

        // observe the changes of the entities from the database and forward them
        observableReservations.addSource(teacherReservations, observableReservations::setValue);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application application;

        private final String classRoomId;

        private final ReservationRepository reservationRepository;

        public Factory(@NonNull Application application, String classRoomId) {
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

    public LiveData<List<Reservation>> getReservations() {
        return observableReservations;
    }

    public void deleteReservation(Reservation reservation, OnAsyncEventListener callback) {
        repository.delete(reservation, callback);
    }


}
