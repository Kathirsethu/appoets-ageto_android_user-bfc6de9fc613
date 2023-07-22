package com.healthcareride.user.ui.fragment.schedule;

import com.healthcareride.user.base.MvpView;

public interface ScheduleIView extends MvpView {
    void onSuccess(Object object);

    void onError(Throwable e);
}
