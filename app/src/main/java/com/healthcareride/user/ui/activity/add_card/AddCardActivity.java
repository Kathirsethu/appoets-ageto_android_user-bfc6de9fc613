package com.healthcareride.user.ui.activity.add_card;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;
import com.healthcareride.user.R;
import com.healthcareride.user.base.BaseActivity;
import com.healthcareride.user.data.SharedHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddCardActivity extends BaseActivity implements AddCardIView, View.OnClickListener {

    @BindView(R.id.card_form)
    CardForm cardForm;
    @BindView(R.id.submit)
    Button submit;

    private AddCardPresenter<AddCardActivity> presenter = new AddCardPresenter<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_card;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
        presenter.attachView(this);
        // Activity title will be updated after the locale has changed in Runtime
        setTitle(getString(R.string.add_card_for_payments));

        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .actionLabel(getString(R.string.add_card_details))
                .setup(this);
        submit.setOnClickListener(this);
    }

    private void addCard(Card card) {
        showLoading();
        Stripe stripe = new Stripe(this, SharedHelper.getKey(this, "stripe_publishable_key"));
        stripe.createToken(card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        Log.d("CARD:", " " + token.getId());
                        Log.d("CARD:", " " + token.getCard().getLast4());
                        String stripeToken = token.getId();
                        presenter.card(stripeToken);
                    }

                    public void onError(Exception error) {
                        hideLoading();
                        Toast.makeText(getApplicationContext(), error.getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    @Override
    public void onSuccess(Object card) {
        hideLoading();
        Toast.makeText(this, getString(R.string.card_added), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
    }

    @Override
    protected void onDestroy() {
        presenter.onDetach();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit) {
            if (cardForm.getCardNumber().isEmpty()) {
                Toast.makeText(this, getString(R.string.please_enter_card_number),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (cardForm.getExpirationMonth().isEmpty()) {
                Toast.makeText(this, getString(R.string.please_enter_card_expiration_details),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (cardForm.getCvv().isEmpty()) {
                Toast.makeText(this, getString(R.string.please_enter_card_cvv),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (!TextUtils.isDigitsOnly(cardForm.getExpirationMonth()) || !TextUtils.isDigitsOnly(cardForm.getExpirationYear())) {
                Toast.makeText(this, getString(R.string.please_enter_card_expiration_details),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String cardNumber = cardForm.getCardNumber();
            int cardMonth = Integer.parseInt(cardForm.getExpirationMonth());
            int cardYear = Integer.parseInt(cardForm.getExpirationYear());
            String cardCvv = cardForm.getCvv();
            Log.d("CARD", "CardDetails Number: " + cardNumber + "Month: " + cardMonth
                    + " Year: " + cardYear + " Cvv " + cardCvv);
            Card card = new Card(cardNumber, cardMonth, cardYear, cardCvv);
            addCard(card);
        }
    }
}
