package com.healthcareride.user.ui.activity.upcoming_trip_detail;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.Datum;

import java.util.List;

public interface UpcomingTripDetailsIView extends MvpView {

    void onSuccess(List<Datum> upcomingTripDetails);

    void onError(Throwable e);
}
