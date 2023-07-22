package com.healthcareride.user.ui.activity.profile;

import com.healthcareride.user.base.MvpPresenter;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;

public interface ProfileIPresenter<V extends ProfileIView> extends MvpPresenter<V> {
    void update(HashMap<String, RequestBody> obj, @Part MultipartBody.Part filename);

    void profile();

    void verifyCredentials(String number, String phoneNumber);
}
