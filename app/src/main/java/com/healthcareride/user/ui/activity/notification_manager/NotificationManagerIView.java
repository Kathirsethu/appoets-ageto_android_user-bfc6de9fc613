package com.healthcareride.user.ui.activity.notification_manager;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.NotificationManager;

import java.util.List;

public interface NotificationManagerIView extends MvpView {

    void onSuccess(List<NotificationManager> notificationManager);

    void onError(Throwable e);

}