package com.healthcareride.user.ui.activity.past_trip_detail;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.Datum;

import java.util.List;

public interface PastTripDetailsIView extends MvpView {

    void onSuccess(List<Datum> pastTripDetails);

    void onError(Throwable e);
}
