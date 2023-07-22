package com.healthcareride.user.ui.activity.splash;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.CheckVersion;
import com.healthcareride.user.data.network.model.Service;
import com.healthcareride.user.data.network.model.User;

import java.util.List;

public interface SplashIView extends MvpView {

    void onSuccess(List<Service> serviceList);

    void onSuccess(User user);

    void onError(Throwable e);

    void onSuccess(CheckVersion checkVersion);
}
