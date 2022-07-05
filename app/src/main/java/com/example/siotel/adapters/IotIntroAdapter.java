package com.example.siotel.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.siotel.R;
import com.example.siotel.models.IotIntroModel;

import java.util.List;

public class IotIntroAdapter extends RecyclerView.Adapter<IotIntroAdapter.IotViewHolder>   {


    Context context;
    List<IotIntroModel> arrayList;


    public IotIntroAdapter(Context context,List<IotIntroModel> arrayList) {
        this.context=context;
        this.arrayList = arrayList;

    }

    @NonNull
    @Override
    public IotIntroAdapter.IotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_dashboardintro_rv,parent,false);
        IotViewHolder  iotViewHolder  =new IotViewHolder (view);
        return  iotViewHolder ;
    }

    @Override
    public void onBindViewHolder(@NonNull IotIntroAdapter.IotViewHolder holder, int position) {


      /*  if(position==1)
            holder.setBackgroundColor(Color.RED);
        else if(position==2)
            holder.view.setBackgroundColor(Color.parseColor("#amberColorCode")); */





        IotIntroModel IotIntroModel=arrayList.get(position);
        holder.imageView.setImageResource(IotIntroModel.getImg());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public static class IotViewHolder extends RecyclerView.ViewHolder {

       ImageView imageView;
        public IotViewHolder(@NonNull View itemView) {
            super(itemView);
          imageView=itemView.findViewById(R.id.iotimage);


        }



    }
}
