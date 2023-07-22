package com.healthcareride.user.ui.activity.wallet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appoets.paytmpayment.PaytmCallback;
import com.appoets.paytmpayment.PaytmObject;
import com.appoets.paytmpayment.PaytmPayment;
import com.healthcareride.user.R;
import com.healthcareride.user.base.BaseActivity;
import com.healthcareride.user.common.Constants;
import com.healthcareride.user.data.SharedHelper;
import com.healthcareride.user.data.network.model.AddWallet;
import com.healthcareride.user.data.network.model.BrainTreeResponse;
import com.healthcareride.user.ui.activity.braintree.BrainTreePaymentActivity;
import com.healthcareride.user.ui.activity.payment.PaymentActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

import static com.healthcareride.user.MvpApplication.isBraintree;
import static com.healthcareride.user.MvpApplication.isCard;
import static com.healthcareride.user.MvpApplication.isPaytm;
import static com.healthcareride.user.MvpApplication.isPayumoney;
import static com.healthcareride.user.ui.activity.payment.PaymentActivity.PICK_PAYMENT_METHOD;

public class WalletActivity extends BaseActivity implements WalletIView {

    private static final int BRAINTREE_PAYMENT_REQUEST_CODE = 101;
    private static final String CURRENCY_KEY = "currency";
    @BindView(R.id.wallet_balance)
    TextView walletBalance;
    @BindView(R.id.amount)
    EditText amount;
    @BindView(R.id._199)
    Button _199;
    @BindView(R.id._599)
    Button _599;
    @BindView(R.id._1099)
    Button _1099;
    @BindView(R.id.add_amount)
    Button addAmount;
    @BindView(R.id.cvAddMoneyContainer)
    CardView cvAddMoneyContainer;
    private StringBuilder walletBalanceBuilder;
    private WalletPresenter<WalletActivity> presenter = new WalletPresenter<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        walletBalanceBuilder = new StringBuilder()
                .append(SharedHelper.getKey(this, "currency"))
                .append(" ");
        ButterKnife.bind(this);
        presenter.attachView(this);
        // Activity title will be updated after the locale has changed in Runtime
        setTitle(getString(R.string.wallet));

        String currency = SharedHelper.getKey(this, CURRENCY_KEY);
        _199.setText(currency + " " + getString(R.string._199));
        _599.setText(currency + " " + getString(R.string._599));
        _1099.setText(currency + " " + getString(R.string._1099));
        amount.setKeyListener(new DigitsInputFilter());
        amount.setTag(currency);

        walletBalance.setText(new StringBuilder(walletBalanceBuilder).append(getNumberFormat().format(Double.parseDouble(SharedHelper.getKey(this, "walletBalance", "0")))));

        if (!isCard && !isBraintree && !isPaytm && !isPayumoney) {
            cvAddMoneyContainer.setVisibility(View.GONE);
            addAmount.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick({R.id._199, R.id._599, R.id._1099, R.id.add_amount})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id._199:
                amount.setText(getString(R.string._199));
                break;
            case R.id._599:
                amount.setText(getString(R.string._599));
                break;
            case R.id._1099:
                amount.setText(getString(R.string._1099));
                break;
            case R.id.add_amount:
                if (amount.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.invalid_amount),
                            Toast.LENGTH_SHORT).show();
                } else if (Float.parseFloat(amount.getText().toString().trim()) == 0) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.valid_amount), Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Intent intent = new Intent(baseActivity(), PaymentActivity.class);
                    intent.putExtra("hideCash", true);
                    startActivityForResult(intent, PICK_PAYMENT_METHOD);
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PAYMENT_METHOD && resultCode == Activity.RESULT_OK && data != null)
            switch (data.getStringExtra("payment_mode")) {
                case Constants.PaymentMode.CARD:
                    HashMap<String, Object> map = new HashMap<>();
                    String cardId = data.getStringExtra("card_id");
                    map.put("amount", amount.getText().toString());
                    map.put("payment_mode", "STRIPE");
                    map.put("card_id", cardId);
                    map.put("user_type", "user");
                    showLoading();
                    presenter.addMoney(map);
                    break;
                case Constants.PaymentMode.BRAINTREE:
                    presenter.getBrainTreeToken();
                    break;
                case Constants.PaymentMode.PAYTM: {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("amount", amount.getText().toString());
                    hashMap.put("payment_mode", Constants.PaymentMode.PAYTM);
                    hashMap.put("user_type", "user");
                    showLoading();
                    presenter.addMoneyPaytm(hashMap);
                    break;
                }
            }
        else if (requestCode == BRAINTREE_PAYMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                HashMap<String, Object> map = new HashMap<>();
                String nonce = data.getStringExtra(BrainTreePaymentActivity.PAYMENT_NONCE);
                map.put("amount", amount.getText().toString());
                map.put("payment_mode", "BRAINTREE");
                map.put("braintree_nonce", nonce);
                map.put("user_type", "user");
                showLoading();
                presenter.addMoney(map);
            } else
                Toasty.error(WalletActivity.this, getString(R.string.something_went_wrong),
                        Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onSuccess(PaytmObject object) {

        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        new PaytmPayment(WalletActivity.this, object, new PaytmCallback() {
            @Override
            public void onPaytmSuccess(String status, String message, String paymentmode,
                                       String txid) {
                Toasty.success(WalletActivity.this, "Amount added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPaytmFailure(String errorMessage) {
                Toasty.error(WalletActivity.this, "failed to add money", Toast.LENGTH_SHORT).show();
            }
        }).startPayment();
    }

    @Override
    public void onSuccess(AddWallet wallet) {
        try {
            hideLoading();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), wallet.getMessage(), Toast.LENGTH_SHORT).show();
        amount.setText("");
        SharedHelper.putKey(this, "walletBalance", String.valueOf(wallet.getBalance()));
        walletBalance.setText(new StringBuilder(walletBalanceBuilder).append(getNumberFormat().format(Double.parseDouble(SharedHelper.getKey(this, "walletBalance", "0")))));
    }

    @Override
    public void onSuccess(BrainTreeResponse response) {

        if (!response.getToken().isEmpty()) {
            Intent intent = new Intent(WalletActivity.this, BrainTreePaymentActivity.class);
            intent.putExtra(BrainTreePaymentActivity.EXTRAS_TOKEN, response.getToken());
            startActivityForResult(intent, BRAINTREE_PAYMENT_REQUEST_CODE);
        }

    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
    }

    public class DigitsInputFilter extends DigitsKeyListener {

        private int decimalPlaces = 2;

        DigitsInputFilter() {
            super(false, true);
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dstart, int dend) {
            CharSequence out = super.filter(source, start, end, dest, dstart, dend);
            if (out != null) {
                source = out;
                start = 0;
                end = out.length();
            }

            int length = end - start;
            if (length == 0)
                return source;

            if (dend == 0 && source.toString().equals("."))
                return "";

            int destLength = dest.length();
            for (int i = 0; i < dstart; i++) {
                if (dest.charAt(i) == '.')
                    return (destLength - (i + 1) + length > decimalPlaces) ?
                            "" : new SpannableStringBuilder(source, start, end);
            }

            for (int i = start; i < end; ++i) {
                if (source.charAt(i) == '.') {
                    if ((destLength - dend) + (end - (i + 1)) > decimalPlaces)
                        return "";
                    else
                        break;
                }
            }

            return new SpannableStringBuilder(source, start, end);
        }
    }
}
