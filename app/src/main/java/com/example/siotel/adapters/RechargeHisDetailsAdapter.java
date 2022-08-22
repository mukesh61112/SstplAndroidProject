package com.example.siotel.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siotel.R;
import com.example.siotel.models.HRHDetailsModel;
import com.example.siotel.models.HouseholdsDetailsModel;

import java.util.List;

public class  RechargeHisDetailsAdapter extends RecyclerView.Adapter<RechargeHisDetailsAdapter.MDViewHolder> {



    Context context;
    List<HRHDetailsModel> arrayList;

    public RechargeHisDetailsAdapter(List<HRHDetailsModel> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public RechargeHisDetailsAdapter.MDViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_household_recharge_details_rv,parent,false);
        //DevicesAdapter.DevicesViewHolder devicesViewHolder=new DevicesAdapter.DevicesViewHolder(view);
        RechargeHisDetailsAdapter.MDViewHolder mdViewHolder=new RechargeHisDetailsAdapter.MDViewHolder(view);
        return  mdViewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RechargeHisDetailsAdapter.MDViewHolder holder, int position) {

        if(position==0) {

            holder.metersn.setBackgroundColor(Color.rgb(90,152,232));

            holder.balance_amount.setBackgroundColor(Color.rgb(90,152,232));
            holder.date.setBackgroundColor(Color.rgb(90,152,232));
        }




        HRHDetailsModel meterDetails=arrayList.get(position);
        holder.metersn.setText(meterDetails.getDevid());

        holder.balance_amount.setText(meterDetails.getAmount());
        String s=meterDetails.getDate();
        //String [] arr=s.split("T");
//        holder.date.setText(arr[0]);
//        holder.time.setText(arr[1]);
        String arr[]=s.split("[T.]");
        holder.date.setText(arr[0]+"  "+arr[1]);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    static class MDViewHolder extends RecyclerView.ViewHolder {

        TextView metersn,balance_amount,date;
        public MDViewHolder(@NonNull View itemView) {
            super(itemView);
            metersn = itemView.findViewById(R.id.crhd_household_num);

            balance_amount = itemView.findViewById(R.id.crhd_balance_amount);
            date = itemView.findViewById(R.id.crhd_date_time);
        }
    }
}
