package ru.lavrent.lab6.common.exceptions;

/**
 * exception that represents error while checking validity of a field/entity
 */
public class ValidationException extends RuntimeException {
  public ValidationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ValidationException(String message) {
    super(message);
  }
}
