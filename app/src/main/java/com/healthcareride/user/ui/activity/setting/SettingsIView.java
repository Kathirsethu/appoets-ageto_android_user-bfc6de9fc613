package com.healthcareride.user.ui.activity.setting;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.AddressResponse;

public interface SettingsIView extends MvpView {

    void onSuccessAddress(Object object);

    void onLanguageChanged(Object object);

    void onSuccess(AddressResponse address);

    void onError(Throwable e);
}
