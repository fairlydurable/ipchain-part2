package io.temporal.first_action;

public class InvalidStatusException extends Exception {
  public InvalidStatusException(String message) {
    super(message);
  }
}
