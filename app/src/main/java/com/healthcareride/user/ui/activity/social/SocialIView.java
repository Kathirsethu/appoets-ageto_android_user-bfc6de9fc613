package com.healthcareride.user.ui.activity.social;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.Token;

public interface SocialIView extends MvpView {
    void onSuccess(Token token);

    void onError(Throwable e);
}
