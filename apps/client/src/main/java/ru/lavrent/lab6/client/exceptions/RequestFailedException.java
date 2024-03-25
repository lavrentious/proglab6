package ru.lavrent.lab6.client.exceptions;

public class RequestFailedException extends RuntimeException {
  public RequestFailedException() {
    super();
  }

  public RequestFailedException(Throwable cause) {
    super(cause);
  }

  public RequestFailedException(String message) {
    super(message);

  }

  public RequestFailedException(String message, Throwable cause) {
    super(message, cause);
  }
}
