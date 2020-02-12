package com.weatherappv2;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.weatherappv2.adapter.WeatherForecastAdapter;
import com.weatherappv2.common.Utils;
import com.weatherappv2.model.WeatherForecastResult;
import com.weatherappv2.retrofit.RetrofitClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {

    CompositeDisposable compositeDisposable;
    IOpenWeather mService;


    TextView txt_city_name,txt_geo_coord;
    RecyclerView recyclerView_forecast;

    static ForecastFragment instance;

    public static ForecastFragment getInstance() {
        if (instance == null)
            instance = new ForecastFragment();
        return instance;
    }

    public ForecastFragment() {
        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeather.class);



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View itemView = inflater.inflate(R.layout.fragment_forecast, container, false);
        txt_city_name = (TextView) itemView.findViewById(R.id.txt_city_name);
        txt_geo_coord = (TextView) itemView.findViewById(R.id.txt_geo_coord);
        recyclerView_forecast = (RecyclerView) itemView.findViewById(R.id.recycler_forecast);
        recyclerView_forecast.setHasFixedSize(true);
        recyclerView_forecast.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false));

        getForecastWeatherInformation();

        return itemView;


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

    private void getForecastWeatherInformation() {
        compositeDisposable.add(mService.getForecastWeatherByLatLng(
                String.valueOf(Utils.current_location.getLatitude()),
                String.valueOf(Utils.current_location.getLongitude()),
                Utils.APP_ID,
                "metric")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecastResult>() {
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
                        displayForCastWeather(weatherForecastResult);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("ERROR", "" + throwable.getMessage());


                    }
                })
        );
    }

    private void displayForCastWeather(WeatherForecastResult weatherForecastResult) {
        txt_city_name.setText(new StringBuilder(weatherForecastResult.city.name));
        txt_geo_coord.setText(new StringBuilder(weatherForecastResult.city.coord.toString()));
        WeatherForecastAdapter adapter= new WeatherForecastAdapter(getContext(),weatherForecastResult);
        recyclerView_forecast.setAdapter(adapter);
    }

}
