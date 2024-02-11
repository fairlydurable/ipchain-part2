package io.temporal.first_action;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class retrieves weather data from the National Weather Service API.
 *
 * @author Erica Sadun
 * @version 1.0
 * @see <a href="https://www.weather.gov/documentation/services-web-api">NationalWeatherService API documentation</a>
 */
public class WeatherFetcherActivityImpl {
  
  /**
   * The main entry point for the WeatherFetcher application.
   *
   * <p>Fetches weather conditions based on latitude and longitude.
   *
   * @param args Command-line arguments. Expects two arguments: longitude and latitude.
   * @throws IOException If an error occurs while fetching weather data.
   * @see <a href="https://www.weather.gov/documentation/services-web-api">NationalWeatherService API documentation</a>
   * @apiNote EPSG:4326 specifies that latitude comes first, before longitude. Despite that
   * many computer systems and software still use longitude and then latitude ordering.
   * PostGIS and WFS 1.0 use long/lat. WFS 1.3 and GeoTools use lat/long.
   * Some tooling allows you to swap the expected order with overrides.
   * @see <a href="https://docs.geotools.org/latest/userguide/library/referencing/order.html">Axis Order</a> from the OSGeo project.
   */
  public static void main(String[] args) {
    WeatherFetcherActivityImpl weatherFetcher = new WeatherFetcherActivityImpl();
    
    try {
      System.out.println(weatherFetcher.fetchForecast(args));
    } catch (Exception exception) {
      System.err.println("Error retrieving forecast: " + exception.getMessage());
    }
  }
  
  /**
   * Fetch weather data using coordinates and return a forecast string.
   *
   * @param coordinates an array containing the latitude and longitude coordinates.
   * @return the forecast as a string
   * @throws IOException if an I/O error occurs while fetching the forecast data
   * @throws InvalidStatusException if the number of coordinates is not exactly 2
   * @see <a href="https://www.weather.gov/documentation/services-web-api">NationalWeatherService API documentation</a>
   * @apiNote EPSG:4326 specifies that latitude comes first, before longitude. Despite that
   * many computer systems and software still use longitude and then latitude ordering.
   * PostGIS and WFS 1.0 use long/lat. WFS 1.3 and GeoTools use lat/long.
   * Some tooling allows you to swap the expected order with overrides.
   * @see <a href="https://docs.geotools.org/latest/userguide/library/referencing/order.html">Axis Order</a> from the OSGeo project.
   */
  public String fetchForecast(String[] coordinates) throws IOException {
    try {
      if (coordinates.length != 2) {
        throw new InvalidStatusException("Expecting 2 arguments (longitude latitude). Got " + Integer.toString(coordinates.length));
      }
      
      String latitude = coordinates[0];
      String longitude = coordinates[1];
            
      // First, extract a URL with the local forecast information
      JSONObject weatherData = fetchWeatherData(latitude, longitude);
      JSONObject weatherProperties = weatherData.getJSONObject("properties");
      String forecastURLString = weatherProperties.getString("forecast");
      
      // Then break down the forecast JSON to retrievea detailed forecast
      JSONObject forecastData = fetchDataFromURLString(forecastURLString);
      JSONObject forecastProperties = forecastData.getJSONObject("properties");
      JSONArray forecastPeriods = forecastProperties.getJSONArray("periods");
      
      // Ensure there's at least one location to select
      if (forecastPeriods.length() > 0) {
        JSONObject firstForecastProperties = forecastPeriods.getJSONObject(0);
        String forecastString = firstForecastProperties.getString("detailedForecast");
        return forecastString;
      } else {
        throw new RuntimeException("Missing forecast information.");
      }
    } catch (Exception exception) {
      throw new RuntimeException("Unexpected error occurred", exception);
    }
  }

  /**
   * Fetch weather data from the National Weather Service API.
   *
   * @param longitude Longitude of the location.
   * @param latitude  Latitude of the location.
   * @return Weather data as a JSONObject.
   * @throws IOException If an error occurs while fetching weather data.
   */
  private static JSONObject fetchWeatherData(String latitude, String longitude) throws IOException {
    String weatherEndpoint = "https://api.weather.gov/points/" + latitude + "," + longitude;
    return fetchDataFromURLString(weatherEndpoint);
  }
  
  /**
   * Fetch data from a URL string, returning a JSON object
   *
   * @param urlString The URL to fetch data from.
   * @return The fetched data as a JSONObject.
   * @throws IOException If an error occurs while fetching data.
   */
  private static JSONObject fetchDataFromURLString(String urlString) throws IOException {
    try {
      URI uri = new URI(urlString);
      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
      
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      
      if (response.statusCode() != 200) {
        throw new IOException("HTTP connection error. Code: " + response.statusCode());
      }
      return new JSONObject(response.body());
      
    } catch (URISyntaxException | InterruptedException e) {
      throw new IOException("Error fetching data from URL: " + e.getMessage());
    }
  }
}

