package com.healthcareride.user.ui.activity.add_card;

import com.healthcareride.user.base.MvpPresenter;

interface AddCardIPresenter<V extends AddCardIView> extends MvpPresenter<V> {
    void card(String stripeToken);
}
