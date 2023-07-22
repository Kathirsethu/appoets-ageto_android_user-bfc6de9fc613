package com.healthcareride.user.ui.activity.passbook;

import com.healthcareride.user.base.MvpPresenter;

public interface WalletHistoryIPresenter<V extends WalletHistoryIView> extends MvpPresenter<V> {
    void wallet();
}
