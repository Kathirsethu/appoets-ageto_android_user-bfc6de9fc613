package com.healthcareride.user.ui.fragment.schedule;

import com.healthcareride.user.base.MvpPresenter;

import java.util.HashMap;

public interface ScheduleIPresenter<V extends ScheduleIView> extends MvpPresenter<V> {
    void sendRequest(HashMap<String, Object> obj);
}
