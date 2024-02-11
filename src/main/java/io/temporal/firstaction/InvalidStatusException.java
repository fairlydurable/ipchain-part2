package io.temporal.first_action;

/**
 * A String-based exception type for any occasion.
 */
public class InvalidStatusException extends Exception {
  /**
   * Returns an Exception with a String message.
   *
   * @param message an  explanatory String.
   */
  public InvalidStatusException(String message) {
    super(message);
  }
}
