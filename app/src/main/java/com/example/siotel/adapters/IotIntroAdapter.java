package com.example.siotel.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.siotel.R;
import com.example.siotel.models.IotIntroModel;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class IotIntroAdapter extends SliderViewAdapter<IotIntroAdapter.SliderAdapterViewHolder> {

    // list for storing urls of images.
    Context context;
    private final List<IotIntroModel> mSliderItems;

    // Constructor
    public IotIntroAdapter(Context context, List<IotIntroModel> sliderDataArrayList) {
        this.context=context;
        this.mSliderItems = sliderDataArrayList;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_dashboardintro_rv, null);
        return new SliderAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, final int position) {

        final IotIntroModel sliderItem = mSliderItems.get(position);

        // Glide is use to load image
        // from url in your imageview.
        viewHolder.imageViewBackground.setImageResource(sliderItem.getImg());
    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class SliderAdapterViewHolder extends ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.introimage);
            this.itemView = itemView;
        }
    }
}