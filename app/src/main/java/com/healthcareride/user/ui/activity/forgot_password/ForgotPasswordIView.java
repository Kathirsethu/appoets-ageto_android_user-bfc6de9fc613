package com.healthcareride.user.ui.activity.forgot_password;

import com.healthcareride.user.base.MvpView;

public interface ForgotPasswordIView extends MvpView {
    void onSuccess(Object object);

    void onError(Throwable e);
}
