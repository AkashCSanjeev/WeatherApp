package com.example.weatherapp;

public class WeatherDetails {

    private String mainLoc, mainWeather, windText, humidityText, uvText, pressureText;
    private long unixDay, sunRiseText, sunSetText;
    private double minTempText, maxTempText;
    int iconID;


    public WeatherDetails() {
    }


    public String getMainLoc() {
        return mainLoc;
    }

    public WeatherDetails(long unixDay, String windText, double minTempText, double maxTempText,
                          String humidityText, long sunRiseText, long sunSetText,
                          String uvText, String pressureText, String mainWeather, int iconID, String mainLoc) {
        this.unixDay = unixDay;
        this.windText = windText;
        this.minTempText = minTempText;
        this.maxTempText = maxTempText;
        this.humidityText = humidityText;
        this.sunRiseText = sunRiseText;
        this.sunSetText = sunSetText;
        this.uvText = uvText;
        this.pressureText = pressureText;
        this.mainWeather = mainWeather;
        this.iconID = iconID;
        this.mainLoc=mainLoc;
    }

    public int getIconID() {
        return iconID;
    }

    public long getUnixDay() {
        return unixDay;
    }

    public String getMainWeather() {
        return mainWeather;
    }

    public String getWindText() {
        return windText;
    }

    public double getMinTempText() {
        return minTempText;
    }

    public double getMaxTempText() {
        return maxTempText;
    }

    public String getHumidityText() {
        return humidityText;
    }

    public long getSunRiseText() {
        return sunRiseText;
    }

    public long getSunSetText() {
        return sunSetText;
    }

    public String getUvText() {
        return uvText;
    }

    public String getPressureText() {
        return pressureText;
    }
}
