package com.healthcareride.user.ui.fragment.book_ride;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.PromoResponse;


public interface BookRideIView extends MvpView {
    void onSuccess(Object object);

    void onError(Throwable e);

    void onSuccessCoupon(PromoResponse promoResponse);
}
