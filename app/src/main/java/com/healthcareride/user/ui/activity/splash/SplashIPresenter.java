package com.healthcareride.user.ui.activity.splash;

import com.healthcareride.user.base.MvpPresenter;

import java.util.HashMap;

public interface SplashIPresenter<V extends SplashIView> extends MvpPresenter<V> {

    void services();

    void profile();

    void checkVersion(HashMap<String, Object> map);
}
