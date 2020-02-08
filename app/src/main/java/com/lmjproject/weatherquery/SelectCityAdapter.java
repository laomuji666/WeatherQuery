package com.lmjproject.weatherquery;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SelectCityAdapter extends RecyclerView.Adapter<SelectCityAdapter.Holder> {
    public class Holder extends RecyclerView.ViewHolder{
        public TextView baseId,city,postcode,province,pinyin;
        public LinearLayout layout;
        public int position;
        public Holder(@NonNull View itemView) {
            super(itemView);
            baseId=itemView.findViewById(R.id.select_city_item_baseId);
            city=itemView.findViewById(R.id.select_city_item_city);
            postcode=itemView.findViewById(R.id.select_city_item_postcode);
            province=itemView.findViewById(R.id.select_city_item_province);
            pinyin=itemView.findViewById(R.id.select_city_item_pinyin);
            layout=itemView.findViewById(R.id.select_city_item_layout);
        }
        public void setItemText(String text,int pos){
            //57494|武汉|430000|湖北省|wuhan|AHB
            String[] split = text.split("\\|");
            if (split.length!=6){
                //如果传过来的格式不正确
                return;
            }
            baseId.setText(split[0]);
            city.setText(split[1]);
            postcode.setText(split[2]);
            province.setText(split[3]);
            pinyin.setText(split[4]);
            position=pos;
        }
    }
    private List<String>mList;
    private Activity mActivity;
    SharedPreferences.Editor editor;
    public SelectCityAdapter(List<String>list, Activity activity){
        mList=list;
        mActivity=activity;
        editor = mActivity.getSharedPreferences(SelectCityActivity.CITY, Context.MODE_PRIVATE).edit();
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.select_city_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {
        final String str=mList.get(position);
        holder.setItemText(str,position);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString(SelectCityActivity.CITY_DATA,str);
                editor.putString(SelectCityActivity.CITY_NAME,holder.city.getText().toString());
                editor.apply();
                mActivity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
