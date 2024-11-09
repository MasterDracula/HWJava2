package org.example;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {
    private static final String APIKEY = "96217ba5-bf5a-487d-9c8b-a1c3aa5a8693";
    public static final String URL = "https://api.weather.yandex.ru/v2/forecast";
    public static void main(String[] args) throws Exception{
        double latitude = 55.77;
        double longtitude = 37.62;
        int limit = 7;
        String URLParams = String.format("%s?lat=%s&lon=%s&limit=%d", URL, latitude, longtitude, limit);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(URLParams)).header("X-Yandex-API-Key", APIKEY).build();
        HttpResponse <String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String StringResponseBody = response.body();
        System.out.println("Asnswer of server: " + StringResponseBody);
        if (StringResponseBody.contains("error")) {
            System.out.println("Error in response " + StringResponseBody);
            return;
        }
        JSONObject json = new JSONObject(StringResponseBody);
        double Temperature = json.getJSONObject("fact").getDouble("temp");
        System.out.println("Temperature: " + Temperature);
        double Sum = 0;
        for (int i = 0; i < limit; i++) {
            Sum += json.getJSONArray("forecasts").getJSONObject(i).getJSONObject("parts").getJSONObject("day").getDouble("temp_avg");
        }
        double middleTemp = Sum/limit;
        System.out.println("Middle temperature for period: " + limit + " days is: " +  middleTemp);
    }
}
