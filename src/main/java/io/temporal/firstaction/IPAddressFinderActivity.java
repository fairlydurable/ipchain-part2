package io.temporal.first_action;

import io.temporal.activity.ActivityInterface;
import java.io.IOException;

@ActivityInterface
public interface IPAddressFinderActivity {
  
  /**
   * Retrieve the active IP address with ipify.org.
   *
   * @return A String with the caller's public IP address
   * @throws IOException If an error occurs while retrieving the IP address
   */
  String getPublicIPAddress() throws IOException;
  
}
