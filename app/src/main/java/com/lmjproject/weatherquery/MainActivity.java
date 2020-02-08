package com.lmjproject.weatherquery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.lmjproject.weatherquery.Http.Http;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final int SELECT_CITY = 0;
    private Toolbar toolbar;
    private String cityData="54511|北京|100000|北京市|beijing|ABJ",cityName="北京";
    private SharedPreferences shared;
    private TextView text;//测试
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();
        selectCity();
    }

    //初始化控件
    private void initWidget(){
        setToolbar();
        shared = getSharedPreferences(SelectCityActivity.CITY, Context.MODE_PRIVATE);
        text=findViewById(R.id.main_text);
    }

    //查询城市天气
    private void selectCity(){
        cityData=shared.getString(SelectCityActivity.CITY_DATA,"54511|北京|100000|北京市|beijing|ABJ");
        cityName=shared.getString(SelectCityActivity.CITY_NAME,"北京");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<String> weekWeather = Http.getWeekWeather(cityData);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String t=cityName+"\n";
                        for (String s:weekWeather){
                            t+=s+"\n";
                        }
                        text.setText(t);
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
