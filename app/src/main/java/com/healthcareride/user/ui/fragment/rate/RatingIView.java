package com.healthcareride.user.ui.fragment.rate;

import com.healthcareride.user.base.MvpView;

public interface RatingIView extends MvpView {
    void onSuccess(Object object);

    void onError(Throwable e);
}
