package ru.lavrent.lab6.client.exceptions;

public class TCPClientException extends RuntimeException {
  public TCPClientException() {
    super();
  }

  public TCPClientException(Throwable cause) {
    super(cause);
  }

  public TCPClientException(String message) {
    super(message);

  }

  public TCPClientException(String message, Throwable cause) {
    super(message, cause);
  }
}
