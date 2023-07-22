package com.healthcareride.user.ui.activity.social;

import com.healthcareride.user.base.MvpPresenter;

import java.util.HashMap;

public interface SocialIPresenter<V extends SocialIView> extends MvpPresenter<V> {
    void loginGoogle(HashMap<String, Object> obj);

    void loginFacebook(HashMap<String, Object> obj);
}
