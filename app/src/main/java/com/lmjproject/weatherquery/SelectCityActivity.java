package com.lmjproject.weatherquery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;
import com.lmjproject.weatherquery.Http.Http;

import java.util.ArrayList;
import java.util.List;

public class SelectCityActivity extends AppCompatActivity {
    public static final String CITY="city",CITY_NAME="city_name",CITY_DATA="city_data";
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
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final String autoStr = s.toString();
                //文本改变时 向服务器查询
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String city = Http.getCity(autoStr);
                        //转换城市列表的格式 并放入list
                        String[] split = city.split("\n");
                        if (split.length==0){
                            return;
                        }
                        cityList.clear();
                        for (String s:split){
                            cityList.add(s);
                        }
                        SelectCityActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }).start();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //文本改变前
            }
            @Override
            public void afterTextChanged(Editable s) {
                //文本改变后
            }
        });
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
}
