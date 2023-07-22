package com.healthcareride.user.ui.activity.card;

import com.healthcareride.user.base.MvpPresenter;


public interface CarsIPresenter<V extends CardsIView> extends MvpPresenter<V> {
    void card();
}
