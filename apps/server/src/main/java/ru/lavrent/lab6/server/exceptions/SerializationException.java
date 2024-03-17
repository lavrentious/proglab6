package ru.lavrent.lab6.server.exceptions;

/**
 * represents exception that occurs when object cannot be deserialized
 */
public class SerializationException extends RuntimeException {
  public SerializationException(String message, Throwable cause) {
    super(message, cause);
  }

  public SerializationException(String message) {
    super(message);
  }

  public SerializationException(Throwable e) {
    super(e);
  }

  public SerializationException() {
    super();
  }
}
