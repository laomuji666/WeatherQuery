package com.lmjproject.weatherquery;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;

import com.lmjproject.weatherquery.Http.Http;

import java.util.List;

public class WeatherService extends Service {
    private String cityData,cityName;
    private WeatherServiceBinder mBinder=new WeatherServiceBinder();
    public WeatherService() {
    }
    public class WeatherServiceBinder extends Binder{
        private boolean isUpdate=false;
        public void updateWeather(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences shared = getSharedPreferences(SelectCityActivity.CITY, Context.MODE_PRIVATE);
                    cityData=shared.getString(SelectCityActivity.CITY_DATA,"54511|北京|100000|北京市|beijing|ABJ");
                    cityName=shared.getString(SelectCityActivity.CITY_NAME,"北京");
                    List<String> weekWeather = Http.getWeekWeather(cityData);
                    if (weekWeather==null ||weekWeather.size()!=7){
                        //当前无网络连接或天气获取错误
                    }else {
                        String[] todaySplit = weekWeather.get(0).split("\\|");
                        if (todaySplit.length==7){
                            WeatherNotification notification=new WeatherNotification(WeatherService.this);
                            String data=cityName+" "+todaySplit[1]+" "+todaySplit[3]+" "+todaySplit[4]+" "+todaySplit[5]+" "+todaySplit[6];
                            notification.showNotification("天气提醒",data);
                        }
                    }
                }
            }).start();
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AlarmManager manager=(AlarmManager) getSystemService(ALARM_SERVICE);
        final long time=4*60*60*1000+ SystemClock.elapsedRealtime();//4小时后执行
        Intent it=new Intent(this,WeatherService.class);
        PendingIntent pit=PendingIntent.getService(this,0,it,0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,time,pit);

        mBinder.updateWeather();

        return super.onStartCommand(intent, flags, startId);
    }

}
