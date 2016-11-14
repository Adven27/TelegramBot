package org.telegram.services.impl;

import org.telegram.services.LocalizationService;
import org.telegram.services.Weather;
import org.telegram.telegrambots.logging.BotLogger;

public class WeatherLoggingDecorator implements Weather {
    private static final String LOGTAG = "WEATHERSERVICE";
    public static final String ERROR_FETCHING_WEATHER = "errorFetchingWeather";

    private final Weather origin;
    private final LocalizationService localisation;

    public WeatherLoggingDecorator(Weather origin, LocalizationService localisation) {
        this.origin = origin;
        this.localisation = localisation;
    }

    @Override
    public String printForecastFor(String city, String language, String units) {
        try {
            return origin.printForecastFor(city, language, units);
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
            return localisation.getString(ERROR_FETCHING_WEATHER, language);
        }
    }

    @Override
    public String printForecastFor(Double longitude, Double latitude, String language, String units) {
        try {
            return origin.printForecastFor(longitude, latitude, language, units);
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
            return localisation.getString(ERROR_FETCHING_WEATHER, language);
        }
    }

    @Override
    public String printCurrentFor(String city, String language, String units) {
        try {
            return origin.printCurrentFor(city, language, units);
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
            return localisation.getString(ERROR_FETCHING_WEATHER, language);
        }
    }

    @Override
    public String printCurrentFor(Double longitude, Double latitude, String language, String units) {
        try {
            return origin.printCurrentFor(longitude, latitude, language, units);
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
            return localisation.getString(ERROR_FETCHING_WEATHER, language);
        }
    }
}