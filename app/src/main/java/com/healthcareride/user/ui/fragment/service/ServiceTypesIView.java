package com.healthcareride.user.ui.fragment.service;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.Service;

import java.util.List;

public interface ServiceTypesIView extends MvpView {

    void onSuccess(List<Service> serviceList);

    void onError(Throwable e);

    void onSuccess(Object object);
}
