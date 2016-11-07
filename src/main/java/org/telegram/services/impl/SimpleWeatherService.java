package org.telegram.services.impl;

import org.telegram.services.WeatherService;

public class SimpleWeatherService implements WeatherService {

    private static volatile WeatherService instance;
    private final WeatherPrinter weatherPrinter;
    private final WeatherResource weatherResource;

    private SimpleWeatherService(WeatherPrinter weatherPrinter, WeatherResource weatherResource) {
        this.weatherPrinter = weatherPrinter;
        this.weatherResource = weatherResource;
    }

    public static WeatherService getInstance(WeatherPrinter weatherPrinter, WeatherResource weatherResource) {
        WeatherService currentInstance;
        if (instance == null) {
            synchronized (SimpleWeatherService.class) {
                if (instance == null) {
                    instance = new SimpleWeatherService(weatherPrinter, weatherResource);
                }
                currentInstance = instance;
            }
        } else {
            currentInstance = instance;
        }
        return currentInstance;
    }

    @Override
    public String fetchWeatherForecastByCity(String city, String language, String units) {
        return weatherPrinter.printForecast(language, weatherResource.fetchForecastBy(city, language, units));
    }

    @Override
    public String fetchWeatherForecastByLocation(Double longitude, Double latitude, String language, String units) {
        return weatherPrinter.printForecast(language, weatherResource.fetchForecastBy(longitude, latitude, language, units));
    }

    @Override
    public String fetchWeatherCurrent(String city, String language, String units) {
        return weatherPrinter.printCurrent(language, weatherResource.fetchCurrentBy(city, language, units));
    }

    @Override
    public String fetchWeatherCurrentByLocation(Double longitude, Double latitude, String language, String units) {
        return weatherPrinter.printMamologda(language, weatherResource.fetchCurrentBy(longitude, latitude, language, units));
    }
}