package com.healthcareride.user.ui.fragment.past_trip;

import com.healthcareride.user.base.MvpPresenter;

public interface PastTripIPresenter<V extends PastTripIView> extends MvpPresenter<V> {
    void pastTrip();
}
