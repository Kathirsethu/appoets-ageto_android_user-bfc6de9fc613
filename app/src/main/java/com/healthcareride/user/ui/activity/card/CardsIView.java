package com.healthcareride.user.ui.activity.card;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.Card;

import java.util.List;

public interface CardsIView extends MvpView {
    void onSuccess(List<Card> cardList);

    void onError(Throwable e);
}
