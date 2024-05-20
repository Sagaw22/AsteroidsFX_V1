package dk.sdu.mmmi.cbse.main;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PointsClient {
    private static final String API_BASE_URL = "http://localhost:8082/points";

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public Long retrieveScore() throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return Long.parseLong(httpResponse.body());
    }

    public Long increaseScore(Long value) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "/add?value=" + value))
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return Long.parseLong(httpResponse.body());
    }
}