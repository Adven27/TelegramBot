package org.telegram.services.impl;

import org.telegram.services.LocalisationService;
import org.telegram.services.WeatherService;
import org.telegram.telegrambots.logging.BotLogger;

public class WeatherServiceLoggingDecorator implements WeatherService {
    private static final String LOGTAG = "WEATHERSERVICE";
    public static final String ERROR_FETCHING_WEATHER = "errorFetchingWeather";

    private static volatile WeatherService instance;
    private final WeatherService origin;
    private final LocalisationService localisation;

    private WeatherServiceLoggingDecorator(WeatherService origin, LocalisationService localisation) {
        this.origin = origin;
        this.localisation = localisation;
    }

    public static WeatherService getInstance(WeatherService weatherService, LocalisationService localisation) {
        WeatherService currentInstance;
        if (instance == null) {
            synchronized (WeatherServiceLoggingDecorator.class) {
                if (instance == null) {
                    instance = new WeatherServiceLoggingDecorator(weatherService, localisation);
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
        try {
            return origin.fetchWeatherForecastByCity(city, language, units);
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
            return localisation.getString(ERROR_FETCHING_WEATHER, language);
        }
    }

    @Override
    public String fetchWeatherForecastByLocation(Double longitude, Double latitude, String language, String units) {
        try {
            return origin.fetchWeatherForecastByLocation(longitude, latitude, language, units);
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
            return localisation.getString(ERROR_FETCHING_WEATHER, language);
        }
    }

    @Override
    public String fetchWeatherCurrent(String city, String language, String units) {
        try {
            return origin.fetchWeatherCurrent(city, language, units);
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
            return localisation.getString(ERROR_FETCHING_WEATHER, language);
        }
    }

    @Override
    public String fetchWeatherCurrentByLocation(Double longitude, Double latitude, String language, String units) {
        try {
            return origin.fetchWeatherCurrentByLocation(longitude, latitude, language, units);
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
            return localisation.getString(ERROR_FETCHING_WEATHER, language);
        }
    }
}