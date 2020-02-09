package com.lmjproject.weatherquery;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class WeekWeatherAdapter extends RecyclerView.Adapter<WeekWeatherAdapter.Holder> {
    class Holder extends RecyclerView.ViewHolder{
        public ImageView image;
        public TextView text;
        public View view;
        public Holder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.week_weather_item_image);
            text=itemView.findViewById(R.id.week_weather_item_text);
            view=itemView;
        }
    }
    private List<String>mList;
    public WeekWeatherAdapter(List<String>list){
        mList=list;
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.week_weather_item,parent,false);
        return new WeekWeatherAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String[] weekSplit = mList.get(position).split("\\|");
        if (weekSplit.length!=7){
            return;
        }
        String imageUrl=weekSplit[2].substring(10,weekSplit[2].length()-2);
        Glide.with(holder.view).load(imageUrl).into(holder.image);
        String text=weekSplit[0]+" "+weekSplit[1]+" "+weekSplit[3]+" "+weekSplit[4]+" "+weekSplit[5]+" "+weekSplit[6];
        holder.text.setText(text);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


}
