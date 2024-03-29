package io.temporal.first_action;

import io.temporal.activity.ActivityInterface;
import java.io.IOException;

@ActivityInterface
public interface GeolocationFetcherActivity {
  
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
  public String[] fetchApproximateGeolocation(String ipAddress) throws IOException;

}
