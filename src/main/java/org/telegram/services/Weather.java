package org.telegram.services;

public interface Weather {
    String printForecastFor(String city, String language, String units);

    String printForecastFor(Double longitude, Double latitude, String language, String units);

    String printCurrentFor(String city, String language, String units);

    String printCurrentFor(Double longitude, Double latitude, String language, String units);
}
