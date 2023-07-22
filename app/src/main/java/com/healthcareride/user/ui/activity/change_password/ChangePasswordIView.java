package com.healthcareride.user.ui.activity.change_password;

import com.healthcareride.user.base.MvpView;

public interface ChangePasswordIView extends MvpView {
    void onSuccess(Object object);

    void onError(Throwable e);
}
