package com.example.siotel.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siotel.R;
import com.example.siotel.models.CurrRechaModel;

import java.util.List;

public class HCurrentAmountAdapter extends RecyclerView.Adapter<HCurrentAmountAdapter.DevicesViewHolder> {


    Context context;
    List<CurrRechaModel> arrayList;


    public HCurrentAmountAdapter(Context context, List<CurrRechaModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public HCurrentAmountAdapter.DevicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_current_recharge, parent, false);
        DevicesViewHolder devicesViewHolder = new DevicesViewHolder(view);
        return devicesViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HCurrentAmountAdapter.DevicesViewHolder holder, int position) {

        CurrRechaModel currRechaModel = arrayList.get(position);
        holder.ch_num.setText(currRechaModel.getH_num());
        holder.ch_balance.setText(Integer.toString(currRechaModel.getCurrentRechargeResponse().getAmount()));
        holder.ch_kwh.setText(currRechaModel.getCurrentRechargeResponse().getKwh_d());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class DevicesViewHolder extends RecyclerView.ViewHolder {

        TextView ch_num, ch_balance,ch_kwh;

        public DevicesViewHolder(@NonNull View itemView) {
            super(itemView);

            ch_num = itemView.findViewById(R.id.ch_num);
            ch_balance = itemView.findViewById(R.id.ch_balance);
            ch_kwh=itemView.findViewById(R.id.ch_kwh);


        }


    }
}

