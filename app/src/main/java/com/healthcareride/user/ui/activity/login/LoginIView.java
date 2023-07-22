package com.healthcareride.user.ui.activity.login;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.ForgotResponse;
import com.healthcareride.user.data.network.model.Token;

public interface LoginIView extends MvpView {
    void onSuccess(Token token);

    void onSuccess(ForgotResponse object);

    void onError(Throwable e);
}
