package com.lmjproject.weatherquery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lmjproject.weatherquery.Http.Http;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final int SELECT_CITY = 0;
    private Toolbar toolbar;
    private String cityData="54511|北京|100000|北京市|beijing|ABJ",cityName="北京",provinceName="北京市";
    private SharedPreferences shared;
    private androidx.swiperefreshlayout.widget.SwipeRefreshLayout swipeRef;
    private TextView today_celsius,today_weather,today_weekday,today_direction,today_wind;//摄氏度,天气,星期几,方向,大小
    private ImageView today_imageView;//天气图片
    private RecyclerView weekWeatherRecView;
    private WeekWeatherAdapter adapter;
    private List<String>mList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fitsSystemWindows();
        setContentView(R.layout.activity_main);
        initWidget();
        selectCity();
    }

    //将背景设置到系统栏
    private void fitsSystemWindows(){
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    //初始化控件
    private void initWidget(){
        setToolbar();
        shared = getSharedPreferences(SelectCityActivity.CITY, Context.MODE_PRIVATE);
        today_imageView=findViewById(R.id.main_today_image);
        today_celsius=findViewById(R.id.main_today_celsius);
        today_weather=findViewById(R.id.main_today_weather);
        today_weekday=findViewById(R.id.main_today_weekday);
        today_direction=findViewById(R.id.main_today_direction);
        today_wind=findViewById(R.id.main_today_wind);
        weekWeatherRecView=findViewById(R.id.main_weekWeather_recyclerView);
        weekWeatherRecView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        mList=new ArrayList<>();
        adapter=new WeekWeatherAdapter(mList);
        weekWeatherRecView.setAdapter(adapter);
        //下拉刷新功能
        swipeRef=findViewById(R.id.main_SwipeRefreshLayout);
        swipeRef.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                selectCity();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        swipeRef.setRefreshing(false);
                    }
                }).start();
            }
        });
    }

    //查询城市天气
    private void selectCity(){
        cityData=shared.getString(SelectCityActivity.CITY_DATA,"54511|北京|100000|北京市|beijing|ABJ");
        cityName=shared.getString(SelectCityActivity.CITY_NAME,"北京");
        provinceName=shared.getString(SelectCityActivity.PROVICE_NAME,"北京市");
        getSupportActionBar().setTitle(provinceName+"-"+cityName);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> weekWeather = Http.getWeekWeather(cityData);
                if (weekWeather==null){
                    return;
                }
                if (weekWeather.size()==7){
                    mList.clear();
                    for (String s:weekWeather){
                        Log.d("onBindViewHolder", s);
                        mList.add(s);
                    }
                }
                final String[] todaySplit = weekWeather.get(0).split("\\|");
                if (todaySplit.length!=7){
                    return;
                }
                final String imageUrl=todaySplit[2].substring(10,todaySplit[2].length()-2);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //今天|星期日|<img src="http://image.nmc.cn/static2/site/nmc/themes/basic/weather/white/night/1.png">|多云| 5℃ |南风|3~4级|
                        today_wind.setText(todaySplit[6]);//大小
                        today_direction.setText(todaySplit[5]);//方向
                        today_celsius.setText(todaySplit[4]);//摄氏度
                        today_weather.setText(todaySplit[3]);//天气
                        today_weekday.setText(todaySplit[0]+" "+todaySplit[1]);//星期几
                        Glide.with(MainActivity.this).load(imageUrl).into(today_imageView);//图片
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    //设置工具菜单
    private void setToolbar(){
        toolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar=getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setHomeAsUpIndicator(R.drawable.item2);
        bar.setTitle("");
    }

    //菜单点击事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent it=new Intent(MainActivity.this,SelectCityActivity.class);
                startActivityForResult(it,SELECT_CITY);
                break;
        }
        return true;
    }

    //活动返回事件
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SELECT_CITY:
                //从选择城市的活动中返回时
                selectCity();
                break;
        }

    }



}
