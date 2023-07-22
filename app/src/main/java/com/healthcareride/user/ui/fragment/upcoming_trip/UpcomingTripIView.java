package com.healthcareride.user.ui.fragment.upcoming_trip;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.Datum;

import java.util.List;

public interface UpcomingTripIView extends MvpView {
    void onSuccess(List<Datum> datumList);

    void onSuccess(Object object);

    void onError(Throwable e);
}
