package com.weatherappv2.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.weatherappv2.common.Utils;
import com.weatherappv2.model.WeatherForecastResult;
import com.weatherappv2.R;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.MyViewHolder > {


    Context context;
    WeatherForecastResult weatherForecastResult;


    public WeatherForecastAdapter(Context context, WeatherForecastResult weatherForecastResult) {
        this.context = context;
        this.weatherForecastResult = weatherForecastResult;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_weather_forecast,viewGroup,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherForecastResult.list.get(i).weather.get(0).getIcon())
                .append(".png").toString()).into(myViewHolder.img_weather);

        myViewHolder.txt_date.setText(new StringBuilder
                (Utils.convertUnixToDate(weatherForecastResult.list.get(i).dt)));
        myViewHolder.txt_description
                .setText(new StringBuilder(weatherForecastResult.list.get(i).weather.get(0).getDescription()));

        myViewHolder.txt_temperature.setText
                (new StringBuilder(String.valueOf(weatherForecastResult.list.get(i).main.getTemp()))
                        .append("°C"));


    }

    @Override
    public int getItemCount() {
        return weatherForecastResult.list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txt_date,txt_description,txt_temperature;
        ImageView img_weather;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_weather = (ImageView) itemView.findViewById(R.id.img_weather);
            txt_date    =(TextView) itemView.findViewById(R.id.txt_date);
            txt_description = (TextView) itemView.findViewById(R.id.txt_description);
            txt_temperature = (TextView) itemView.findViewById(R.id.txt_temperature);

        }
    }
}
