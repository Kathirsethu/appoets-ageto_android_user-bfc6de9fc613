package com.healthcareride.user.ui.activity.invite_friend;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.User;

public interface InviteFriendIView extends MvpView {

    void onSuccess(User user);

    void onError(Throwable e);

}
