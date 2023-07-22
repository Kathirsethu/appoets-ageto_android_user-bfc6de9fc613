package com.healthcareride.user.ui.fragment.lost_item;

import com.healthcareride.user.base.MvpView;

public interface LostIView extends MvpView{
    void onSuccess(Object object);
    void onError(Throwable e);
}
