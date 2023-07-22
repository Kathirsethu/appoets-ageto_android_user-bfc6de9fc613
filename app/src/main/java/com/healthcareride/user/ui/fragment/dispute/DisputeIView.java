package com.healthcareride.user.ui.fragment.dispute;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.DisputeResponse;

import java.util.List;

public interface DisputeIView extends MvpView {

    void onSuccess(Object object);

    void onSuccessDispute(List<DisputeResponse> responseList);

    void onError(Throwable e);
}
