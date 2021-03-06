package com.weatherappv2;

import com.weatherappv2.model.WeatherForecastResult;
import com.weatherappv2.model.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeather {
      @GET("weather")
      Observable <WeatherResult> getWeatherByLatLng (@Query("lat") String lat,
                                             @Query("lon") String lng,
                                             @Query("appid") String appid,
                                             @Query("units") String unit);
      @GET("weather")
      Observable <WeatherResult> getWeatherByCityName (@Query("q") String cityName ,
                                                     @Query("appid") String appid,
                                                     @Query("units") String unit);
      @GET("forecast")
            Observable <WeatherForecastResult> getForecastWeatherByLatLng (@Query("lat") String lat,
                                                                           @Query("lon") String lng,
                                                                           @Query("appid") String appid,
                                                                           @Query("units") String unit);
}
