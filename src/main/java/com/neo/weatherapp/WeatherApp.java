package com.neo.weatherapp;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import com.google.gson.Gson;

public class WeatherApp {

    private static final String API_KEY = "03048d0e052156270c6d7f20884ab9d3";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s&units=metric";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Weather App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JTextField cityField = new JTextField(20);

        JButton getWeatherButton = new JButton("Get Weather");

        JLabel temperatureLabel = new JLabel("Enter a city and press 'Get Weather' \n");
        JLabel humidityLabel = new JLabel("Humidity: ");
        JLabel windSpeedLabel = new JLabel("\n Wind Speed: ");
        JLabel descriptionLabel = new JLabel("\n Description: ");

        getWeatherButton.addActionListener(e -> {
            String city = cityField.getText();
            try {
                WeatherData weatherData = fetchWeatherData(city);
                temperatureLabel.setText(
                        "Temperature in " + weatherData.getName() + ": " + weatherData.getTemperature() + "°C");
                humidityLabel.setText("Humidity: " + weatherData.getHumidity() + "%");
                windSpeedLabel.setText("Wind Speed: " + weatherData.getWindSpeed() + " km/h");
                descriptionLabel.setText("Weather: " + weatherData.getDescription());
            } catch (IOException | InterruptedException ex) {
                temperatureLabel.setText("Error fetching weather data");
                humidityLabel.setText("");
                windSpeedLabel.setText("");
                descriptionLabel.setText("");
            }
        });

        JPanel contentPane = new JPanel();
        contentPane.add(cityField);
        contentPane.add(getWeatherButton);
        contentPane.add(temperatureLabel);
        contentPane.add(humidityLabel);
        contentPane.add(windSpeedLabel);
        contentPane.add(descriptionLabel);
        frame.setContentPane(contentPane);

        frame.setVisible(true);
    }

    private static WeatherData fetchWeatherData(String city) throws IOException, InterruptedException {
        String requestUrl = String.format(BASE_URL, city, API_KEY);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            return gson.fromJson(response.body(), WeatherData.class);
        } else {
            throw new IOException("Error fetching weather data: " + response.statusCode());
        }
    }

    private static double parseTemperature(String responseBody) {
        String tempStr = responseBody.substring(responseBody.indexOf("\"temp\":") + 7);
        tempStr = tempStr.substring(0, tempStr.indexOf(','));
        return Double.parseDouble(tempStr);
    }
}
