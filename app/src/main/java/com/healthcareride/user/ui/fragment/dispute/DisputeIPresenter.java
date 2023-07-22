package com.healthcareride.user.ui.fragment.dispute;

import com.healthcareride.user.base.MvpPresenter;

import java.util.HashMap;

public interface DisputeIPresenter<V extends DisputeIView> extends MvpPresenter<V> {

    void dispute(HashMap<String, Object> obj);

    void getDispute();
}
