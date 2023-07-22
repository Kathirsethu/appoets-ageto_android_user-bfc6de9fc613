package com.healthcareride.user.ui.activity.notification_manager;

import com.healthcareride.user.base.MvpPresenter;

public interface NotificationManagerIPresenter<V extends NotificationManagerIView> extends MvpPresenter<V> {
    void getNotificationManager();
}
