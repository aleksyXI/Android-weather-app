package com.example.weatherapp;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;


public class WeatherFragment extends Fragment implements View.OnClickListener{

    String city;
    TextView cityName;
    TextView textTemp;
    TextView textTempDescr;
    TextView iconWeather;
    Integer fragNum=0;
    ImageButton exitBtn;
    ImageButton updBtn;
    Context context;
    WeatherWeekAdapter wwAdapter;
    Handler handler;
    Typeface weatherFont;

    public WeatherFragment(){
        handler = new Handler();
    }

    private OnFragmentDataListener mListener;

    public interface OnFragmentDataListener {
        void sendData(int Num);
    }

    static WeatherFragment newInstance(String city,int pos) {
        WeatherFragment weatherFragment = new WeatherFragment();
        Bundle arguments = new Bundle();
        arguments.putString("City", city);
        arguments.putInt("Num",pos);
        weatherFragment.setArguments(arguments);
        return weatherFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weather_fragment, container,false);
        RecyclerView rv = (RecyclerView)view.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        rv.setLayoutManager(llm);
        List<WeatherDay> wDays = getForecast();
        wwAdapter = new WeatherWeekAdapter(wDays,rv.getContext());
        rv.setAdapter(wwAdapter);
        Bundle bundle = getArguments();
        if (bundle != null) {
            fragNum=getArguments().getInt("Num");
            city = getArguments().getString("City");
        }
        exitBtn = (ImageButton) view.findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener((View.OnClickListener) this);
        updBtn = (ImageButton) view.findViewById(R.id.updBnt);
        updBtn.setOnClickListener((View.OnClickListener) this);
        cityName = (TextView) view.findViewById(R.id.cityName);
        cityName.setText(city);
        textTemp=(TextView) view.findViewById(R.id.textTemp);
        textTempDescr=(TextView) view.findViewById(R.id.textTempDescr);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather_icons.ttf");
        iconWeather=(TextView) view.findViewById(R.id.icon) ;
        iconWeather.setTypeface(weatherFont);
        updateWeatherData(city);
        return view;
    }
    private List<WeatherDay> getForecast() {
        return Arrays.asList(
                new WeatherDay("Белгород","ПН", "12/16",getActivity().getString(R.string.thunder), "3", "0"),
                new WeatherDay("Белгород","Вт", "12/17",getActivity().getString(R.string.snowy), "6", "0"),
                new WeatherDay("Белгород","Ср", "12/17",getActivity().getString(R.string.cloudy), "3", "1"),
                new WeatherDay("Белгород","Чт", "12/18",getActivity().getString(R.string.sunny), "2", "0"),
                new WeatherDay("Белгород","Пт", "12/19",getActivity().getString(R.string.foggy), "1", "-3"),
                new WeatherDay("Белгород","Сб", "12/20",getActivity().getString(R.string.foggy), "-4", "-8"),
                new WeatherDay("Белгород","Вс", "12/21",getActivity().getString(R.string.foggy), "-5", "-10")
        );
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentDataListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " должен реализовывать интерфейс OnFragmentInteractionListener");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.updBnt:
                updateWeatherData(city);
                break;
            case R.id.exitBtn:
                mListener.sendData(fragNum);
                break;
        }
    }

    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = RemoteFetch.getJSON(getActivity(), city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }
    private void renderWeather(JSONObject json) {
        try {
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            textTemp.setText(String.valueOf((int)main.getDouble("temp")));

            setWeatherIconText(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }
    private void setWeatherIconText(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon="";
        String temp_descr = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = getActivity().getString(R.string.sunny);
                temp_descr = getActivity().getString(R.string.weather_sunny);

            } else {
                icon = getActivity().getString(R.string.clear_night);
                temp_descr = getActivity().getString(R.string.weather_clear_night);
            }
        } else {
            switch(id) {
                case 2 : icon = getActivity().getString(R.string.thunder);
                        temp_descr=getActivity().getString(R.string.weather_thunder);
                    break;
                case 3 : icon = getActivity().getString(R.string.drizzle);
                        temp_descr = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = getActivity().getString(R.string.foggy);
                        temp_descr = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8 : icon = getActivity().getString(R.string.cloudy);
                        temp_descr = getActivity().getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = getActivity().getString(R.string.snowy);
                        temp_descr = getActivity().getString(R.string.weather_snowy);
                    break;
                case 5 : icon = getActivity().getString(R.string.rainy);
                         temp_descr = getActivity().getString(R.string.weather_rainy);
                    break;
            }
        }
        iconWeather.setText(icon);
        textTempDescr.setText(temp_descr);
    }



}
