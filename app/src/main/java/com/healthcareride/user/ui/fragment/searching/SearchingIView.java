package com.healthcareride.user.ui.fragment.searching;

import com.healthcareride.user.base.MvpView;

public interface SearchingIView extends MvpView {
    void onSuccess(Object object);

    void onError(Throwable e);
}
