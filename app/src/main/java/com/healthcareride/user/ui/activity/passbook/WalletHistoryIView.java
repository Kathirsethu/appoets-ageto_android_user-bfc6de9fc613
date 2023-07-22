package com.healthcareride.user.ui.activity.passbook;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.WalletResponse;

public interface WalletHistoryIView extends MvpView {
    void onSuccess(WalletResponse response);

    void onError(Throwable e);
}
