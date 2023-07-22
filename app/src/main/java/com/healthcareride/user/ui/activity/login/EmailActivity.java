package com.healthcareride.user.ui.activity.login;

import android.content.Intent;
import androidx.appcompat.widget.Toolbar;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.healthcareride.user.R;
import com.healthcareride.user.base.BaseActivity;
import com.healthcareride.user.data.network.model.ForgotResponse;
import com.healthcareride.user.data.network.model.Token;
import com.healthcareride.user.ui.activity.OnBoardActivity;
import com.healthcareride.user.ui.activity.forgot_password.ForgotPasswordActivity;
import com.healthcareride.user.ui.activity.register.RegisterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmailActivity extends BaseActivity implements LoginIView {

    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.text_email_address)
    TextView emailAddressText;

    private loginPresenter<EmailActivity> presenter;
    private boolean isForgotPasswordRequested;

    @Override
    public int getLayoutId() {
        return R.layout.activity_email;
    }

    @Override
    public void initView() {
        presenter = new loginPresenter<>();
        presenter.attachView(this);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mToolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(this, OnBoardActivity.class));
            finish();
        });
//        if (BuildConfig.DEBUG) email.setText("stevejobs@yopmail.com");
        if (getIntent().getStringExtra("email") != null) {
            isForgotPasswordRequested = true;
            email.setText(getIntent().getStringExtra("email"));
            emailAddressText.setText(getResources().getString(R.string.recovery_email));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
            Intent intent = new Intent(EmailActivity.this, OnBoardActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }

    @OnClick({R.id.sign_up, R.id.next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sign_up:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.next:
                performNextClick();
                break;
        }
    }

    private void performNextClick() {
        String userEmail = email.getText().toString().trim();
        if (userEmail.isEmpty()) {
            Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            Toast.makeText(this, getString(R.string.valid_email), Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return;
        }
        if (isForgotPasswordRequested) {
            showLoading();
            presenter.forgotPassword(userEmail);
        } else {
            Intent i = new Intent(this, PasswordActivity.class);
            i.putExtra("email", userEmail);
            startActivity(i);
        }
    }

    @Override
    public void onSuccess(Token token) {

    }

    @Override
    public void onSuccess(ForgotResponse object) {
        hideLoading();
        Toast.makeText(getApplicationContext(), object.getMessage(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
        intent.putExtra("email", object.getUser().getEmail());
        intent.putExtra("otp", object.getUser().getOtp());
        intent.putExtra("id", object.getUser().getId());
        startActivity(intent);
    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
    }
}
