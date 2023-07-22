package com.healthcareride.user.ui.activity.forgot_password;


import com.healthcareride.user.base.MvpPresenter;

import java.util.HashMap;

public interface ForgotPasswordIPresenter<V extends ForgotPasswordIView> extends MvpPresenter<V> {
    void resetPassword(HashMap<String, Object> parms);
}
