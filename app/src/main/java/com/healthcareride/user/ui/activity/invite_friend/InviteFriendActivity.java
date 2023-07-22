package com.healthcareride.user.ui.activity.invite_friend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.healthcareride.user.MvpApplication;
import com.healthcareride.user.R;
import com.healthcareride.user.base.BaseActivity;
import com.healthcareride.user.data.SharedHelper;
import com.healthcareride.user.data.network.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InviteFriendActivity extends BaseActivity implements InviteFriendIView {

    private static final String TAG = "InviteFriendActivity";
    @BindView(R.id.invite_friend)
    TextView invite_friend;
    @BindView(R.id.referral_code)
    TextView referral_code;
    @BindView(R.id.llReferral)
    RelativeLayout referralLayout;
    @BindView(R.id.text_referral_count)
    TextView referralCountText;
    @BindView(R.id.text_referral_amount)
    TextView referralAmountText;
    private StringBuilder currencyBuilder;

    private InviteFriendPresenter<InviteFriendActivity> inviteFriendPresenter =
            new InviteFriendPresenter<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_invite_friend;
    }

    @Override
    public void initView() {
        currencyBuilder = new StringBuilder()
                .append(SharedHelper.getKey(this, "currency"))
                .append(" ");
        ButterKnife.bind(this);
        inviteFriendPresenter.attachView(this);

        if (!SharedHelper.getKey(this, "referral_code").equalsIgnoreCase("")) {
            updateUI();
        } else {
            //To get updated referral details
            inviteFriendPresenter.profile();
        }
    }

    private void updateUI() {
        referral_code.setText(SharedHelper.getKey(this, "referral_code"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            invite_friend.setText(Html.fromHtml(SharedHelper.getKey(this, "referral_text"),
                    Html.FROM_HTML_MODE_COMPACT));
        } else {
            invite_friend.setText(Html.fromHtml(SharedHelper.getKey(this, "referral_text")));
        }
        if (MvpApplication.DATUM != null) {
            referralLayout.setVisibility(View.VISIBLE);
            referralCountText.setText(MvpApplication.DATUM.getUser().getReferral_count());
            referralAmountText.setText(new StringBuilder(currencyBuilder)
                    .append(getNumberFormat()
                            .format(Double.parseDouble(MvpApplication.DATUM.getUser().getReferral_amount()))));
        } else
            referralLayout.setVisibility(View.GONE);
    }


    @Override
    public void onSuccess(User user) {
        MvpApplication.DATUM.setUser(user);
        SharedHelper.putKey(this, "referral_code", user.getReferral_unique_id());
        SharedHelper.putKey(this, "referral_count", user.getReferral_count());
        SharedHelper.putKey(this, "referral_text", user.getReferral_text());
        SharedHelper.putKey(this, "referral_total_text", user.getReferral_total_text());
        MvpApplication.showOTP = user.getRide_otp().equals("1");
        updateUI();
    }

    @Override
    public void onError(Throwable throwable) {
        handleError(throwable);
    }

    @SuppressLint("StringFormatInvalid")
    @OnClick({R.id.share})
    public void onClickAction(View view) {
        switch (view.getId()) {
            case R.id.share:
                try {
                    String appName = getString(R.string.app_name);
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, appName);
                    i.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content_referral,
                            appName, SharedHelper.getKey(this, "referral_code")));
                    startActivity(Intent.createChooser(i, "choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
