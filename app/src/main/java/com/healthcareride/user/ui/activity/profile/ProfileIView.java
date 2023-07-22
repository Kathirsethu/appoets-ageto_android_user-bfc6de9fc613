package com.healthcareride.user.ui.activity.profile;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.User;

public interface ProfileIView extends MvpView {

    void onSuccess(User user);

    void onUpdateSuccess(User user);

    void onError(Throwable e);

    void onSuccessPhoneNumber(Object object);

    void onVerifyPhoneNumberError(Throwable e);
}
