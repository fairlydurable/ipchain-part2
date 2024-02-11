package io.temporal.first_action;

import io.temporal.activity.ActivityInterface;
import java.io.IOException;

@ActivityInterface
public interface WeatherFetcherActivity {

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
  public String fetchForecast(String[] coordinates) throws IOException;

}
