package io.temporal.first_action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.regex.Pattern;

import org.json.JSONObject;

/**
 * Fetch geolocation data from IP address.
 *
 * <p>Validates an IPv4 address, fetches its geolocation data
 * from the ipapi API, and prints the latitude and longitude coordinates
 * to the console.
 *
 * @author Erica Sadun
 * @see <a href="https://ipapi.co/">ipapi.co</a>
 */
public class GeolocationFetcherActivityImpl implements GeolocationFetcherActivity {
  
  /**
   * Entry point to fetch and print approximate geolocation data for an IP address string.
   *
   * @param ipAddress the IP address string for the search.
   */
  public static void main(String[] args) {
    GeolocationFetcherActivityImpl geoFetcher = new GeolocationFetcherActivityImpl();
    try {
      // Check for argument
      if (args.length != 1) {
        throw new InvalidStatusException("Expecting 1 argument. Got " + Integer.toString(args.length));
      }
      
      // Test for IPv4 structure/conformance
      String ipAddress = args[0];
      if (!geoFetcher.isValidIPAddress(ipAddress)) {
        throw new InvalidStatusException("IP address is not valid IPv4.");
      }
      
      // Fetch the location and print it.
      String[] geolocationArray = geoFetcher.fetchApproximateGeolocation(ipAddress);
      System.out.print(geolocationArray[0] + " " +
                       geolocationArray[1] + "\n");
    } catch (Exception exception) {
      System.err.println("Error retrieving approximate geolocation: " + exception.getMessage());
    }
  }
  
  /**
   * Return the approximate geolocation data of an IP address.
   *
   * @param ipAddress the IP address for which geolocation data is to be fetched
   * @return a String array containing the latitude and longitude as strings
   * @throws IOException if an I/O error occurs while fetching the geolocation data
   * @apiNote EPSG:4326 specifies that latitude comes first, before longitude. Despite that
   * many computer systems and software still use longitude and then latitude ordering.
   * PostGIS and WFS 1.0 use long/lat. WFS 1.3 and GeoTools use lat/long.
   * Some tooling allows you to swap the expected order with overrides.
   * @see <a href="https://docs.geotools.org/latest/userguide/library/referencing/order.html">Axis Order</a> from the OSGeo project.
   */
  public String[] fetchApproximateGeolocation(String ipAddress) throws IOException {
    try {
      JSONObject geolocationData = fetchGeolocationData(ipAddress);
      return new String[]{
        Double.toString(geolocationData.getDouble("latitude")),
        Double.toString(geolocationData.getDouble("longitude"))
      };
    } catch (IOException exception) {
      throw exception;
    }
  }
  
  /**
   * Returns Boolean indicating whether this string is a likely IP address.
   *
   * @apiNote A rough approximation. Not meant for production use.
   *
   * @param ipAddress The string-based IP address to validate
   * @return A Boolean. True if the IP address is likely valid, false otherwise
   */
  private boolean isValidIPAddress(String ipAddress) {
    // Regular expression to match IPv4 address format
    String ipRegex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
      "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
      "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
      "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    return Pattern.matches(ipRegex, ipAddress);
  }
  
  /**
   * Return JSONObject geolocation data for an IP address string
   *
   * @param ipAddress A string-based IP address
   * @return A JSONObject containing Geolocation data for the IP address
   * @throws IOException If an error occurs while fetching geolocation
   */
  private JSONObject fetchGeolocationData(String ipAddress) throws IOException {
    // See: https://ipapi.co/
    String geolocationEndpointString = "https://ipapi.co/" + ipAddress + "/json/";
    return fetchDataFromURLString(geolocationEndpointString);
  }
  
  /**
   * Fetches data from a URL and returns it as a JSONObject.
   *
   * @param urlString The URL to fetch data from.
   * @return The fetched data as a JSONObject.
   * @throws IOException If an error occurs while fetching data.
   */
  private JSONObject fetchDataFromURLString(String urlString) throws IOException {
    try {
      URI uri = new URI(urlString);
      HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
      connection.setRequestMethod("GET");
      
      if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
        throw new IOException("HTTP connection error. Code: " + connection.getResponseCode());
      }
      
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      StringBuilder response = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      reader.close();
      
      return new JSONObject(response.toString());
    } catch (IOException | URISyntaxException exception) {
      throw new IOException("Error fetching data from URL: " + exception.getMessage());
    }
  }
}
