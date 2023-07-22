package com.healthcareride.user.ui.fragment.lost_item_status;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcareride.user.MvpApplication;
import com.healthcareride.user.R;
import com.healthcareride.user.base.BaseBottomSheetDialogFragment;
import com.healthcareride.user.data.network.model.Datum;
import com.healthcareride.user.data.network.model.LostItem;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LostItemStatusFragment extends BaseBottomSheetDialogFragment implements LostItemStatusIView, View.OnClickListener {

    private static final String TRIP_KEY = "trip_data";
    @BindView(R.id.ivSupportCall)
    ImageView callImage;
    @BindView(R.id.user_dispute)
    TextView userDispute;
    @BindView(R.id.admin_comment)
    TextView adminComment;
    @BindView(R.id.lost_item_status)
    TextView lostItemStatus;
    @BindView(R.id.llAdminComments)
    LinearLayout adminCommentLayout;
    private LostItemStatusPresenter<LostItemStatusFragment> presenter =
            new LostItemStatusPresenter<>();

    public static LostItemStatusFragment newInstance(Datum datum) {
        Bundle args = new Bundle();
        LostItemStatusFragment fragment = new LostItemStatusFragment();
        args.putSerializable(TRIP_KEY, datum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_fragment_lost_item_status;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
        callImage.setOnClickListener(this);

        Bundle bundle = getArguments();
        Datum datum = (Datum) Objects.requireNonNull(bundle).getSerializable(TRIP_KEY);

        LostItem lostItem = datum != null ? datum.getLostitem() : null;
        if (lostItem != null) {
            userDispute.setText(lostItem.getLostItemName());
            if (lostItem.getComments() != null) {
                adminCommentLayout.setVisibility(View.VISIBLE);
                adminComment.setText(lostItem.getComments());
            }
            lostItemStatus.setText(lostItem.getStatus().toUpperCase());
            if (lostItem.getStatus().contains("open")) {
                lostItemStatus.setTextColor(getResources().getColor(R.color.open_word));
                lostItemStatus.setBackground(getResources().getDrawable(R.drawable.button_round_status_opened));
            } else {
                lostItemStatus.setTextColor(getResources().getColor(R.color.close_word));
                lostItemStatus.setBackground(getResources().getDrawable(R.drawable.button_round_status_closed));
            }
        }

    }

    private void callPhoneNumber(String mobileNumber) {
        if (mobileNumber != null && !mobileNumber.isEmpty())
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mobileNumber)));
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ivSupportCall)
            callPhoneNumber(MvpApplication.helpNumber);
    }
}
