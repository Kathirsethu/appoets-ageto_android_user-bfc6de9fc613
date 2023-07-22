package com.healthcareride.user.ui.activity.help;

import com.healthcareride.user.base.MvpView;
import com.healthcareride.user.data.network.model.Help;

public interface HelpIView extends MvpView {

    void onSuccess(Help help);

    void onError(Throwable e);
}
