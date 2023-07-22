package com.healthcareride.user.ui.activity.login;

import com.healthcareride.user.base.MvpPresenter;

import java.util.HashMap;

public interface LoginIPresenter<V extends LoginIView> extends MvpPresenter<V> {
    void login(HashMap<String, Object> obj);

    void forgotPassword(String email);
}
