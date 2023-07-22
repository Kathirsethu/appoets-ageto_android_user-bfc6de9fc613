package com.healthcareride.user.ui.activity.help;


import com.healthcareride.user.base.MvpPresenter;

public interface HelpIPresenter<V extends HelpIView> extends MvpPresenter<V> {
    void help();
}
