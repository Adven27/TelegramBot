package org.telegram.services.impl;

import org.telegram.services.Weather;

public class SimpleWeather implements Weather {

    private final WeatherPrinter weatherPrinter;
    private final WeatherResource weatherResource;

    public SimpleWeather(WeatherPrinter weatherPrinter, WeatherResource weatherResource) {
        this.weatherPrinter = weatherPrinter;
        this.weatherResource = weatherResource;
    }

    @Override
    public String printForecastFor(String city, String language, String units) {
        return weatherPrinter.printForecast(language, weatherResource.fetchForecastBy(city, language, units));
    }

    @Override
    public String printForecastFor(Double longitude, Double latitude, String language, String units) {
        return weatherPrinter.printForecast(language, weatherResource.fetchForecastBy(longitude, latitude, language, units));
    }

    @Override
    public String printCurrentFor(String city, String language, String units) {
        return weatherPrinter.printCurrent(language, weatherResource.fetchCurrentBy(city, language, units));
    }

    @Override
    public String printCurrentFor(Double longitude, Double latitude, String language, String units) {
        return weatherPrinter.printMamologda(language, weatherResource.fetchCurrentBy(longitude, latitude, language, units));
    }
}