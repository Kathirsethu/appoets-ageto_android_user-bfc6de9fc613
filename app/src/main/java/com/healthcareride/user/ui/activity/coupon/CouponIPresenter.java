package com.healthcareride.user.ui.activity.coupon;

import com.healthcareride.user.base.MvpPresenter;

public interface CouponIPresenter<V extends CouponIView> extends MvpPresenter<V> {
    void coupon();
}
