package com.lmjproject.weatherquery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import com.lmjproject.weatherquery.Http.Http;

import java.util.ArrayList;
import java.util.List;

public class SelectCityActivity extends AppCompatActivity {
    public static final String CITY="city",CITY_NAME="city_name",PROVICE_NAME="province_name",CITY_DATA="city_data";
    private boolean isTextChanged = false,isUiOk=false;
    private EditText editText;
    private RecyclerView recyclerView;
    private SelectCityAdapter adapter;
    private Toolbar toolbar;
    private List<String> cityList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        initWidget();
        setToolbar();
    }


    //绑定控件及事件
    private void initWidget(){
        editText=findViewById(R.id.select_city_EditText);
        recyclerView=findViewById(R.id.select_city_RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new SelectCityAdapter(cityList,this);
        recyclerView.setAdapter(adapter);
        isTextChanged=true;
        // editText.addTextChangedListener //暂时不用了 因为http请求返回时间的问题
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isTextChanged){
                    textChanged();
                    isUiOk=false;
                    SelectCityActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            isUiOk=true;
                        }
                    });
                    while (isUiOk==false){ }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    //输入文本改变事件
    private void textChanged(){
        String autoStr=editText.getText().toString();
        if (autoStr==null){
            return;
        }
        String city = Http.getCity(autoStr);
        if(city==null){
            return;
        }
        //转换城市列表的格式 并放入list
        String[] split = city.split("\n");
        if (split==null){
            return;
        }
        cityList.clear();
        for (String s:split){
            cityList.add(s);
        }
    }
    //设置toolbar
    private void setToolbar(){
        toolbar=findViewById(R.id.select_city_Toolbar);
        setSupportActionBar(toolbar);
        ActionBar bar=getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle("选择城市");
    }

    //toolbar点击事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isTextChanged=false;
        isUiOk=true;
    }
}
