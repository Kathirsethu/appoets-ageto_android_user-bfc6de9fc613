package com.healthcareride.user.ui.fragment.past_trip;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.Datum;

import java.util.List;

public interface PastTripIView extends MvpView {
    void onSuccess(List<Datum> datumList);

    void onError(Throwable e);
}
