package com.healthcareride.user.ui.activity.wallet;

import com.appoets.paytmpayment.PaytmObject;
import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.AddWallet;
import com.healthcareride.user.data.network.model.BrainTreeResponse;

public interface WalletIView extends MvpView {
    void onSuccess(AddWallet object);

    void onSuccess(PaytmObject object);

    void onSuccess(BrainTreeResponse response);
    void onError(Throwable e);
}
