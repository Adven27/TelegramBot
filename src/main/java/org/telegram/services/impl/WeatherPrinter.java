package org.telegram.services.impl;

import org.json.JSONObject;
import org.telegram.mamot.services.DAO;
import org.telegram.services.Emoji;
import org.telegram.services.LocalizationService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.telegram.services.Emoji.*;

public class WeatherPrinter {

    private LocalizationService localisation;
    private DAO dao;

    public WeatherPrinter(LocalizationService localisation, DAO dao) {
        this.localisation = localisation;
        this.dao = dao;
    }

    String printForecast(String language, JSONObject js) {
        return isOkResp(js)
                ? format(localisation.getString("weatherForecast", language), convertListOfForecastToString(js, language, true))
                : localisation.getString("cityNotFound", language);
    }

    public String printCurrent(String language, JSONObject js) {
        if (isOkResp(js)) {
            String city = js.getString("name") + " (" + js.getJSONObject("sys").getString("country") + ")";
            return format(localisation.getString("weatherCurrent", language), city, convertCurrentWeatherToString(js));
        } else {
            return localisation.getString("cityNotFound", language);
        }
    }

    private boolean isOkResp(JSONObject json) {
        return json.getInt("cod") == 200;
    }

    private String convertListOfForecastToString(JSONObject js, String language, boolean addDate) {
        String responseToUser = "";
        for (int i = 0; i < js.getJSONArray("list").length(); i++) {
            JSONObject internalJSON = js.getJSONArray("list").getJSONObject(i);
            responseToUser += convertInternalInformationToString(internalJSON, language, addDate);
        }
        return responseToUser;
    }

    private String convertInternalInformationToString(JSONObject js, String language, boolean addDate) {
        LocalDate date = Instant.ofEpochSecond(js.getLong("dt")).atZone(ZoneId.systemDefault()).toLocalDate();
        String tMin = js.getJSONObject("temp").get("min").toString();
        String tMax = js.getJSONObject("temp").get("max").toString();
        JSONObject weather = js.getJSONArray("weather").getJSONObject(0);
        Emoji emoji = getEmojiForWeather(weather);
        String weatherDesc = weather.getString("description");

        return addDate
            ? format(localisation.getString("forecastWeatherPartMetric", language), emoji == null ? "" : emoji.toString(), ofPattern("EEE dd.MM").format(date), tMax, tMin, weatherDesc)
            : format(localisation.getString("alertWeatherPartMetric", language), emoji == null ? weatherDesc : emoji.toString(), tMax, tMin);
    }

    //TODO Fix hard code
    public String printMamologda(String language, JSONObject json) {
        return isOkResp(json) ? format("Сегодня в Мамологде %s", convertCurrentWeatherToString(json))
                                         : localisation.getString("cityNotFound", language);
    }

    private String convertCurrentWeatherToString(JSONObject js) {
        String temp = js.getJSONObject("main").get("temp").toString();
        String cloudiness = js.getJSONObject("clouds").get("all") + "%";
        String wind = js.getJSONObject("wind").get("speed") + "м/с";
        JSONObject weather = js.getJSONArray("weather").getJSONObject(0);
        Emoji emoji = getEmojiForWeather(weather);
        String weatherDesc = weather.getString("description");

        return format(" %s %s *%sC* ветер *%s* мамоблачность *%s*... %s",
                weatherDesc, emoji == null ? "" : emoji.toString(), temp, wind, cloudiness,
                dao.getEndWord(new Random().nextInt(4)));
    }

    private Emoji getEmojiForWeather(JSONObject weather) {
        switch (weather.getString("icon")) {
            case "01n": case "01d": return SUN_WITH_FACE;
            case "02n": case "02d": return SUN_BEHIND_CLOUD;
            case "03n": case "03d": case "04n": case "04d": return CLOUD;
            case "09n": case "09d": case "10n": case "10d": return UMBRELLA_WITH_RAIN_DROPS;
            case "11n": case "11d": return HIGH_VOLTAGE_SIGN;
            case "13n": case "13d": return SNOWFLAKE;
            case "50n": case "50d": return FOGGY;
            default: return null;
        }
    }
}