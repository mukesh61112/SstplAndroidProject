package com.example.siotel.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siotel.R;
import com.example.siotel.models.HouseholdsDetailsModel;

import java.util.List;

public class HouseholdsDetailsAdapter extends RecyclerView.Adapter<HouseholdsDetailsAdapter.MDViewHolder> {



    Context context;
    List<HouseholdsDetailsModel> arrayList;

    public HouseholdsDetailsAdapter(List<HouseholdsDetailsModel> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public HouseholdsDetailsAdapter.MDViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_household_details_rv,parent,false);
        //DevicesAdapter.DevicesViewHolder devicesViewHolder=new DevicesAdapter.DevicesViewHolder(view);
        HouseholdsDetailsAdapter.MDViewHolder mdViewHolder=new HouseholdsDetailsAdapter.MDViewHolder(view);
        return  mdViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HouseholdsDetailsAdapter.MDViewHolder holder, int position) {

        if(position==0) {

            holder.metersn.setBackgroundColor(Color.rgb(90,152,232));
            holder.cum_eb_kwh.setBackgroundColor(Color.rgb(90,152,232));
            holder.balance_amount.setBackgroundColor(Color.rgb(90,152,232));
            holder.date.setBackgroundColor(Color.rgb(90,152,232));
        }




        HouseholdsDetailsModel meterDetails=arrayList.get(position);
        holder.metersn.setText(meterDetails.getMeterSN());
        holder.cum_eb_kwh.setText(meterDetails.getCum_eb_kwh());
        holder.balance_amount.setText(meterDetails.getBalance_amount());
//       // String s=meterDetails.getDate();
//       // String [] arr=s.split("T");
//        holder.date.setText(arr[0]);
//        holder.time.setText(arr[1]);
        String s=meterDetails.getDate();
        String [] arr=s.split("[T]");
        holder.date.setText(arr[0] +"  "+ arr[1]);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    static class MDViewHolder extends RecyclerView.ViewHolder {

        TextView metersn,cum_eb_kwh,balance_amount,date;
        public MDViewHolder(@NonNull View itemView) {
            super(itemView);
            metersn = itemView.findViewById(R.id.meter_sn);
            cum_eb_kwh = itemView.findViewById(R.id.cum_eb_kwh);
            balance_amount = itemView.findViewById(R.id.balance_amount);
            date = itemView.findViewById(R.id.date_time);
        }
    }
}
