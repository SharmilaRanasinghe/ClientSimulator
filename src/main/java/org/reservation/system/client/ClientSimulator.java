package org.reservation.system.client;

import org.reservation.system.config.ConfigReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ClientSimulator {
    private static final String BASE_URL = "http://localhost:8080/reservation-system";
    private static final String DEFAULT_ORIGIN = "A";
    private static final String DEFAULT_DESTINATION = "D";
    private static final String DEFAULT_PASSENGERS_COUNT = "5";
    private static final String DEFAULT_TRAVEL_DATE = LocalDate.now().plusDays(1)
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    private static final String DEFAULT_COMMAND = "both";

    private final static String origin;
    private final static String destination;
    private final static int passengersCount;
    private final static String travelDate;
    private final static String command;

    static {
        origin = getConfigProperty("origin", DEFAULT_ORIGIN);
        destination = getConfigProperty("destination", DEFAULT_DESTINATION);
        passengersCount = Integer.parseInt(getConfigProperty("passengers_count", DEFAULT_PASSENGERS_COUNT));
        travelDate = getConfigProperty("travel_date",  DEFAULT_TRAVEL_DATE);
        command = getConfigProperty("command", DEFAULT_COMMAND);
    }

    private static String getConfigProperty(String property, String defaultValue) {
        if ( ConfigReader.config.getProperty(property) == null || ConfigReader.config.getProperty(property).isEmpty() ) {
                return defaultValue;
        }
        return ConfigReader.config.getProperty(property);
    }

    public static void simulate() {
        try {

            if (!command.equals("reserve")) {
                checkAvailability();
            }
            if (!command.contains("check")) {
                reserveSeat();
            }
        } catch (Exception e) {
            System.out.println("Exception occured: " + e.getMessage());
        }
    }

    private static void reserveSeat() throws IOException {
        String reserveUrl = BASE_URL + "/reserve";

        String reserveRequestBody = "{\"origin\":\"%s\",\"destination\":\"%s\",\"passengerCount\":%d,\"travelDate\":\"%s\",\"paymentAmount\":300}";
        reserveRequestBody = String.format(reserveRequestBody, origin, destination, passengersCount, travelDate);
        System.out.println("Reservation Request" + reserveRequestBody);
        String reserveResponse = sendPostRequest(reserveUrl, reserveRequestBody);
        System.out.println("Reservation Response: " + reserveResponse);
    }

    private static void checkAvailability() throws IOException {
        String path  = "/check-availability?origin=%s&destination=%s&passengerCount=%d&travelDate=%s";
        path = String.format(path, origin, destination, passengersCount, travelDate);
        String availabilityUrl = BASE_URL + path;
        System.out.println("Availability url - " + availabilityUrl);
        String availabilityResponse = sendGetRequest(availabilityUrl);
        System.out.println("Availability Response: " + availabilityResponse);
    }

    private static String sendGetRequest(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        return readResponse(connection);
    }

    private static String sendPostRequest(String url, String body) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        try (OutputStream os = connection.getOutputStream()) {
            os.write(body.getBytes());
        }
        return readResponse(connection);
    }

    private static String readResponse(HttpURLConnection connection) throws IOException {
        int statusCode = connection.getResponseCode();
        BufferedReader reader;
        if (statusCode >= 400) {
            reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
        } else {
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        return response.toString();
    }

}
