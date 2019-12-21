package com.example.weatherapp;

public class WeatherDay {
    String city;
    String day_week;
    String dayNum;
    String weatherPic;
    String tempDay;
    String tempNight;

    public WeatherDay(String city, String day_week, String dayNum, String weatherPic, String tempDay, String tempNight) {
        this.city = city;
        this.day_week = day_week;
        this.dayNum = dayNum;
        this.weatherPic = weatherPic;
        this.tempDay = tempDay;
        this.tempNight = tempNight;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDay_week() {
        return day_week;
    }

    public void setDay_week(String day_week) {
        this.day_week = day_week;
    }

    public String getDayNum() { return dayNum; }

    public void setDayNum(String dayNum) {
        this.dayNum = dayNum;
    }

    public String getWeatherPic() {
        return weatherPic;
    }

    public void setWeatherPic(String weatherPic) {
        this.weatherPic = weatherPic;
    }

    public String getTempDay() {
        return tempDay;
    }

    public void setTempDay(String tempDay) {
        this.tempDay = tempDay;
    }

    public String getTempNight() {
        return tempNight;
    }

    public void setTempNight(String tempNight) {
        this.tempNight = tempNight;
    }



}
