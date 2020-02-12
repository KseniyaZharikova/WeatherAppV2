package com.weatherappv2;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.weatherappv2.common.Utils;
import com.weatherappv2.model.WeatherResult;
import com.weatherappv2.retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class TodayWeatherFragment extends Fragment {

    ImageView img_weather;
    TextView txt_city_name;
    TextView txt_humidity;
    TextView txt_sunrise;
    TextView txt_sunset;
    TextView txt_pressure;
    TextView txt_temperature;
    TextView txt_description;
    TextView txt_data_time;
    TextView txt_wind;
    TextView txt_geo_coord;

    LinearLayout weather_panel;
     ProgressBar loading;

     CompositeDisposable compositeDisposable;
      IOpenWeather mService;

    static  TodayWeatherFragment instance;

    public static TodayWeatherFragment getInstance() {

        if (instance ==null)
            instance= new TodayWeatherFragment();
        return instance;
    }


    public TodayWeatherFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeather.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView =  inflater.inflate(R.layout.fragment_today_weather, container, false);

        img_weather= (ImageView) itemView.findViewById(R.id.img_weather);
        txt_city_name = (TextView) itemView.findViewById(R.id.txt_city_name);
        txt_humidity = (TextView) itemView.findViewById(R.id.txt_humidity);
        txt_sunrise = (TextView) itemView.findViewById(R.id.txt_sunrise);
        txt_sunset = (TextView) itemView.findViewById(R.id.txt_sunset);
        txt_pressure = (TextView) itemView.findViewById(R.id.txt_pressure);
        txt_temperature = (TextView) itemView.findViewById(R.id.txt_temperature);
        txt_data_time= (TextView) itemView.findViewById(R.id.txt_data_time);
        txt_geo_coord = (TextView) itemView.findViewById(R.id.txt_geo_coord);
        txt_description = (TextView) itemView.findViewById(R.id.txt_description);
        txt_wind = (TextView) itemView.findViewById(R.id.txt_wind);


        weather_panel = (LinearLayout) itemView.findViewById(R.id.weather_panel);
        loading = (ProgressBar) itemView.findViewById(R.id.loading);


        getWeatherInformation();

        return itemView;

    }

    private void getWeatherInformation() {
        Log.d("ololo", "Current location " + Utils.current_location.getLongitude());

        compositeDisposable.add(mService.getWeatherByLatLng(
                String.valueOf(Utils.current_location.getLatitude()),
                String.valueOf(Utils.current_location.getLongitude()),
                Utils.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {

                        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                                .append(weatherResult.getWeather().get(0).getIcon())
                                .append(".png").toString()).into(img_weather);

                        txt_city_name.setText(weatherResult.getName());
                        txt_description.setText(new StringBuilder("Weather in ")
                                .append(weatherResult.getName()).toString());
                        txt_temperature.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp()))
                                .append("°C").toString());
                        txt_data_time.setText(Utils.convertUnixToDate(weatherResult.getDt()));
                        txt_pressure.setText(new StringBuilder(String.valueOf(weatherResult
                                .getMain().getPressure())).append("hpa").toString());
                        txt_humidity.setText(new StringBuilder(String.valueOf(weatherResult.
                                getMain().getHumidity())).append("%").toString());
                        txt_sunrise.setText(Utils.convertUnixToHour(weatherResult.getSys().getSunrise()));
                        txt_sunset.setText(Utils.convertUnixToHour(weatherResult.getSys().getSunset()));
                        txt_geo_coord.setText(new StringBuilder(weatherResult.getCoord().toString()).toString());
                        txt_wind.setText(new StringBuilder(String.valueOf(weatherResult.getWind().getSpeed())));

                        weather_panel.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Toast.makeText(getActivity(), "" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
        );
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
