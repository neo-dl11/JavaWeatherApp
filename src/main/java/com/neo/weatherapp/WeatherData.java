package com.neo.weatherapp;

public class WeatherData {
    private Main main;
    private Wind wind;
    private Weather[] weather; 
    private String name; 

    public double getTemperature() {
        return main.temp;
    }

    public int getHumidity() {
        return main.humidity;
    }

    public double getWindSpeed() {
        return wind.speed;
    }

    public String getDescription() {
        if (weather.length > 0) {
            return weather[0].description; // Assuming we take the first description
        }
        return "No weather description available";
    }

    public String getName() {
        return name;
    }

    class Main {
        double temp;
        int humidity;
    }

    class Wind {
        double speed;
    }

    class Weather {
        String description;
    }
}
