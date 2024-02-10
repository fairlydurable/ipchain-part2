package io.temporal.first_action;

import io.temporal.activity.ActivityInterface;
import java.io.IOException;

@ActivityInterface
public interface IPAddressFinderActivity {
  String getPublicIPAddress() throws IOException;
}
