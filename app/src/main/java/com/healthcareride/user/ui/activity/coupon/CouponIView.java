package com.healthcareride.user.ui.activity.coupon;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.PromoResponse;

public interface CouponIView extends MvpView {
    void onSuccess(PromoResponse object);

    void onError(Throwable e);
}
