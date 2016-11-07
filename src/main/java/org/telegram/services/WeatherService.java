package org.telegram.services;

public interface WeatherService {
    String fetchWeatherForecastByCity(String city, String language, String units);

    String fetchWeatherForecastByLocation(Double longitude, Double latitude, String language, String units);

    String fetchWeatherCurrent(String city, String language, String units);

    String fetchWeatherCurrentByLocation(Double longitude, Double latitude, String language, String units);
}
