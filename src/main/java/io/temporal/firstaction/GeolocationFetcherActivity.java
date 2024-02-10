package io.temporal.first_action;

import io.temporal.activity.ActivityInterface;
import java.io.IOException;

@ActivityInterface
public interface GeolocationFetcherActivity {
  public []String fetchApproximateGeolocation(String ipAddress) throws IOException;
}
