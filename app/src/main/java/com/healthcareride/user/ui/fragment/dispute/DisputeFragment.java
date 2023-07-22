package com.healthcareride.user.ui.fragment.dispute;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.LinkedTreeMap;
import com.healthcareride.user.R;
import com.healthcareride.user.base.BaseBottomSheetDialogFragment;
import com.healthcareride.user.data.network.model.Datum;
import com.healthcareride.user.data.network.model.DisputeResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.healthcareride.user.MvpApplication.DATUM;
import static com.healthcareride.user.MvpApplication.helpNumber;

public class DisputeFragment extends BaseBottomSheetDialogFragment implements DisputeIView {

    private static final String TAG = DisputeFragment.class.getSimpleName();

    @BindView(R.id.cancel_reason)
    EditText cancelReason;
    @BindView(R.id.rcvReason)
    RecyclerView rcvReason;
    private DisputePresenter<DisputeFragment> presenter = new DisputePresenter<>();
    private List<DisputeResponse> disputeResponseList = new ArrayList<>();
    private DisputeFragment.DisputeAdapter adapter;
    private int lastSelectedLocation = -1;
    private DisputeCallBack mCallBack;

    public DisputeFragment() {

    }

    public void setCallBack(DisputeCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_fragment_dispute;
    }

    @Override
    public void initView(View view) {
        ButterKnife.bind(this, view);
        presenter.attachView(this);

        adapter = new DisputeFragment.DisputeAdapter(disputeResponseList);
        rcvReason.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        rcvReason.setItemAnimator(new DefaultItemAnimator());
        rcvReason.setAdapter(adapter);

        showLoading();
        presenter.getDispute();
    }

    @OnClick({R.id.dismiss, R.id.submit, R.id.ivSupportCall})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dismiss:
                dismiss();
                break;
            case R.id.submit:
                if (lastSelectedLocation == -1) {
                    Toast.makeText(getContext(), getString(R.string.invalid_selection),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                createDispute();
                break;

            case R.id.ivSupportCall:
                dialContactNumber(helpNumber);
                break;

            default:
                break;
        }
    }

    private void dialContactNumber(String contactNumber) {
        if (contactNumber != null) {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contactNumber)));
        }
    }

    private void createDispute() {
        if (DATUM != null) {
            Datum datum = DATUM;
            HashMap<String, Object> map = new HashMap<>();
            map.put("request_id", datum.getId());
            map.put("dispute_type", "user");
            map.put("user_id", datum.getUserId());
            map.put("provider_id", datum.getProviderId());
            map.put("comments", cancelReason.getText().toString().trim());
            map.put("dispute_name",
                    disputeResponseList.get(lastSelectedLocation).getDispute_name());
            showLoading();
            presenter.dispute(map);
        }
    }

    @Override
    public void onSuccessDispute(List<DisputeResponse> responseList) {
        disputeResponseList.addAll(responseList);
        DisputeResponse disputeResponse = new DisputeResponse();
        disputeResponse.setDispute_name(getResources().getString(R.string.other_reason));
        disputeResponseList.add(disputeResponse);
        setDefaultSelection();
        hideLoading();
    }

    @Override
    public void onSuccess(Object object) {
        try {
            hideLoading();
            if (object instanceof LinkedTreeMap) {
                LinkedTreeMap responseMap = (LinkedTreeMap) object;
                if (responseMap.get("message") != null) {
                    mCallBack.onDisputeCreated();
                    Toast.makeText(getActivity().getApplicationContext(),
                            responseMap.get("message").toString(), Toast.LENGTH_SHORT)
                            .show();
                }
            } else {
                Toast.makeText(getActivity().getApplicationContext(),
                        getResources().getString(R.string.lost_item_error), Toast.LENGTH_SHORT)
                        .show();
            }
        } catch (Exception e1) {
            Log.e(TAG, e1.getMessage());
        }
        dismiss();
    }

    private void setDefaultSelection() {
        rcvReason.setVisibility(View.VISIBLE);
        lastSelectedLocation = -1;
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(Throwable e) {
        handleError(e);
    }

    private class DisputeAdapter extends RecyclerView.Adapter<DisputeFragment.DisputeAdapter.MyViewHolder> {

        private List<DisputeResponse> list;

        private DisputeAdapter(List<DisputeResponse> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public DisputeFragment.DisputeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new DisputeFragment.DisputeAdapter.MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cancel_reasons_inflate, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull DisputeFragment.DisputeAdapter.MyViewHolder holder,
                                     int position) {
            DisputeResponse data = list.get(position);
            holder.tvReason.setText(data.getDispute_name());
            if (lastSelectedLocation == position) holder.cbItem.setChecked(true);
            else holder.cbItem.setChecked(false);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            LinearLayout llItemView;
            TextView tvReason;
            CheckBox cbItem;

            MyViewHolder(View view) {
                super(view);
                llItemView = view.findViewById(R.id.llItemView);
                tvReason = view.findViewById(R.id.tvReason);
                cbItem = view.findViewById(R.id.cbItem);

                llItemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (getAdapterPosition() == list.size() - 1)
                    cancelReason.setVisibility(View.VISIBLE);
                else
                    cancelReason.setVisibility(View.GONE);
                lastSelectedLocation = getAdapterPosition();
                notifyDataSetChanged();
            }
        }
    }
}
