package com.healthcareride.user.ui.activity.location_pick;

import com.healthcareride.user.base.MvpPresenter;

public interface LocationPickIPresenter<V extends LocationPickIView> extends MvpPresenter<V> {
    void address();
}
