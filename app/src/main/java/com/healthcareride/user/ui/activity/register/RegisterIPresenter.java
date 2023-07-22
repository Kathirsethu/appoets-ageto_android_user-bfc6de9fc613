package com.healthcareride.user.ui.activity.register;

import com.healthcareride.user.base.MvpPresenter;

import java.util.HashMap;

public interface RegisterIPresenter<V extends RegisterIView> extends MvpPresenter<V> {

    void register(HashMap<String, Object> obj);

    void getSettings();

    void verifyEmail(String email);

    void verifyCredentials(String phoneNumber, String countryCode);

}
