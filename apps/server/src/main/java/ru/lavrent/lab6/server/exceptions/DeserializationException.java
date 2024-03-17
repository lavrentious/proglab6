package ru.lavrent.lab6.server.exceptions;

/**
 * represents exception that occurs when object cannot be deserialized
 */
public class DeserializationException extends Exception {
  public DeserializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public DeserializationException(String message) {
    super(message);
  }

  public DeserializationException(Throwable e) {
    super(e);
  }

  public DeserializationException() {
    super();
  }
}
