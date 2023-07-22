package com.healthcareride.user.ui.activity.add_card;

import com.healthcareride.user.base.MvpView;

public interface AddCardIView extends MvpView {
    void onSuccess(Object card);

    void onError(Throwable e);
}
