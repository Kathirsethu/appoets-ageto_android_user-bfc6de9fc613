package com.healthcareride.user.ui.activity.location_pick;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.AddressResponse;

public interface LocationPickIView extends MvpView {

    void onSuccess(AddressResponse address);

    void onError(Throwable e);
}
