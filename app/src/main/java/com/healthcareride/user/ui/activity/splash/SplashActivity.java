package com.healthcareride.user.ui.activity.splash;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.healthcareride.user.BuildConfig;
import com.healthcareride.user.MvpApplication;
import com.healthcareride.user.R;
import com.healthcareride.user.base.BaseActivity;
import com.healthcareride.user.data.SharedHelper;
import com.healthcareride.user.data.network.model.CheckVersion;
import com.healthcareride.user.data.network.model.Service;
import com.healthcareride.user.data.network.model.User;
import com.healthcareride.user.ui.activity.OnBoardActivity;
import com.healthcareride.user.ui.activity.main.MainActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.healthcareride.user.data.SharedHelper.putKey;

public class SplashActivity extends BaseActivity implements SplashIView {

    @BindView(R.id.note)
    TextView note;

    private SplashPresenter<SplashActivity> presenter = new SplashPresenter<>();

    @Override
    public int getLayoutId() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);

        note.setText(getString(R.string.version,
                String.valueOf(BuildConfig.VERSION_CODE)));

        try {
            final MessageDigest md = MessageDigest.getInstance("SHA");
            if (Build.VERSION.SDK_INT >= 28) {
                PackageInfo packageInfo = getPackageManager().getPackageInfo
                        (getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
                Signature[] signatures = packageInfo.signingInfo.getApkContentsSigners();
                for (Signature signature : signatures) {
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", new String(Base64.encode(md.digest(), Base64.DEFAULT)));
                }
            } else {
                PackageInfo info = getPackageManager().getPackageInfo
                        (BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
                for (Signature signature : info.signatures) {
                    md.update(signature.toByteArray());
                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            }
        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Log.d("FCM", "FCM Token: " + SharedHelper.getKey(baseActivity(), "device_token"));
//        checkVersion();
        redirectionToHome();

    }

    private void checkVersion() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("version", BuildConfig.VERSION_CODE);
        map.put("device_type", BuildConfig.DEVICE_TYPE);
        map.put("sender", "provider");
        presenter.checkVersion(map);
        if (!TextUtils.isEmpty(SharedHelper.getKey(MvpApplication.getInstance(), "access_token",
                null)))
            presenter.services();
    }

    private void checkUserAppInstalled() {
        if (isPackageExisted(SplashActivity.this))
            showWarningAlert(getString(R.string.user_provider_app_warning));
        else redirectionToHome();
    }

    private boolean isPackageExisted(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(BuildConfig.DRIVER_PACKAGE, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    private void showWarningAlert(String message) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this);
            alertDialogBuilder
                    .setTitle(getResources().getString(R.string.warning))
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.continue_app),
                            (dialog, id) -> redirectionToHome());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void redirectionToHome() {
        if (SharedHelper.getBoolKey(SplashActivity.this, "logged_in", false))
            presenter.profile();
        else {
            Intent nextScreen = new Intent(SplashActivity.this, OnBoardActivity.class);
            nextScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(nextScreen);
            finishAffinity();
        }
    }

    @Override
    public void onSuccess(User user) {
        MvpApplication.DATUM.setUser(user);
        putKey(this, "stripe_publishable_key", user.getStripePublishableKey());
        putKey(this, "user_id", String.valueOf(user.getId()));
        putKey(this, "appContact", user.getAppContact());
        putKey(this, "currency", user.getCurrency());
        putKey(this, "lang", user.getLanguage());
        putKey(this, "walletBalance", String.valueOf(user.getWalletBalance()));
        putKey(this, "logged_in", true);
        putKey(this, "measurementType", user.getMeasurement());
        putKey(this, "referral_code", user.getReferral_unique_id());
        putKey(this, "referral_count", user.getReferral_count());
        MvpApplication.showOTP = (user.getRide_otp() != null) && (user.getRide_otp().equals("1"));
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
    }

    @Override
    public void onSuccess(CheckVersion version) {
        try {
            if (!version.getForceUpdate()) new Handler().postDelayed(() -> {
                Log.d("Loggedin", String.valueOf(SharedHelper.getBoolKey(SplashActivity.this,
                        "logged_in", false)));
                String device_token = String.valueOf(SharedHelper.getKey(SplashActivity.this,
                        "device_token"));
                Log.d("device_token", device_token);
                checkUserAppInstalled();
            }, 5000);
            else showAlertDialog(version.getUrl());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlertDialog(String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle(getString(R.string.new_version_update));
        builder.setMessage(getString(R.string.update_version_message));
        builder.setPositiveButton(getString(R.string.update), (dialog, which) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        });
        builder.show();
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onSuccess(List<Service> services) {
        AsyncTask.execute(() -> {
            try {
                for (Service service : services)
                    if (!TextUtils.isEmpty(service.getMarker())) {
                        Bitmap b = getBitmapFromURL(service.getMarker());
                        if (b != null)
                            putKey(this, service.getName() + service.getId(), encodeBase64(b));
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
