package org.telegram.services.impl;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.telegram.BuildVars;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static java.lang.Integer.parseInt;
import static java.net.URLEncoder.encode;

public class WeatherResource {

    private static final String BASEURL = "http://api.openweathermap.org/data/2.5/";
    private static final String FORECASTPATH = "forecast/daily";
    private static final String CURRENTPATH = "weather";
    private static final String APIIDEND = "&APPID=" + BuildVars.OPENWEATHERAPIKEY;
    private static final String REQUEST_PARAMS = "&cnt=1&units=@units@&lang=@language@";

    public JSONObject fetchForecastBy(String city, String language, String units) {
        return getResponseFrom(buildForecastUrlBy(city, language, units));
    }

    public JSONObject fetchForecastBy(Double longitude, Double latitude, String language, String units) {
        return getResponseFrom(buildForecastUrlBy(longitude, latitude, language, units));
    }

    public JSONObject fetchCurrentBy(String city, String language, String units) {
        return getResponseFrom(buildCurrentWeatherUrlBy(city, language, units));
    }

    public JSONObject fetchCurrentBy(Double longitude, Double latitude, String language, String units) {
        return getResponseFrom(buildCurrentWeatherUrlBy(longitude, latitude, language, units));
    }

    private JSONObject getResponseFrom(String url) {
        try {
            CloseableHttpClient client = HttpClientBuilder.create().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
            BufferedHttpEntity buf = new BufferedHttpEntity(client.execute(new HttpGet(url)).getEntity());
            return new JSONObject(EntityUtils.toString(buf, "UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String buildRequestUrl(String type, String query, String lang, String units) {
        return BASEURL + type + "?" + query + REQUEST_PARAMS.replace("@language@", lang).replace("@units@", units) + APIIDEND;
    }

    private String buildForecastUrlBy(Double longitude, Double latitude, String language, String units) {
        return buildRequestUrl(FORECASTPATH, getLocationQuery(longitude, latitude), language, units);
    }

    private String buildForecastUrlBy(String city, String language, String units) {
        return buildRequestUrl(FORECASTPATH, getCityQuery(city), language, units);
    }

    private String buildCurrentWeatherUrlBy(String city, String language, String units) {
        return buildRequestUrl(CURRENTPATH, getCityQuery(city), language, units);
    }

    private String buildCurrentWeatherUrlBy(Double longitude, Double latitude, String language, String units) {
        return buildRequestUrl(CURRENTPATH, getLocationQuery(longitude, latitude), language, units);
    }

    private String getLocationQuery(Double longitude, Double latitude) {
        try {
            return "lat=" + encode(latitude + "", "UTF-8") + "&lon=" + encode(longitude + "", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCityQuery(String city) {
        try {
            return "id=" + encode(parseInt(city) + "", "UTF-8");
        } catch (NumberFormatException | NullPointerException e) {
            try {
                return "q=" + encode(city, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                throw new RuntimeException(e);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}