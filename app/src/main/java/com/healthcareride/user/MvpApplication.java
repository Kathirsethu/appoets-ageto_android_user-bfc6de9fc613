package com.healthcareride.user;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.healthcareride.user.common.ConnectivityReceiver;
import com.healthcareride.user.common.LocaleHelper;
import com.healthcareride.user.data.network.model.Datum;

import java.util.HashMap;

public class MvpApplication extends Application {

    public static boolean canGoToChatScreen;
    public static boolean isChatScreenOpen;
    public static boolean isCash = true;
    public static boolean isCard = true;
    public static boolean isPayumoney;
    public static boolean isPaypal;
    public static boolean isPaytm;
    public static boolean isPaypalAdaptive;
    public static boolean isBraintree;
    public static boolean openChatFromNotification = true;
    public static HashMap<String, Object> RIDE_REQUEST = new HashMap<>();
    public static Datum DATUM = null;
    public static boolean showOTP = true;
    public static String helpNumber;
    private static MvpApplication mInstance;

    public static synchronized MvpApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        DATUM = new Datum();

        if (BuildConfig.DEBUG)
            Stetho.initializeWithDefaults(this);

        MultiDex.install(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase, "en"));
        MultiDex.install(newBase);
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
