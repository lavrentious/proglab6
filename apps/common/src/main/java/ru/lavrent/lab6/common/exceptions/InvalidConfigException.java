package ru.lavrent.lab6.common.exceptions;

/**
 * exception meaning that some variables are missing or incorerect
 */
public class InvalidConfigException extends Exception {
  public InvalidConfigException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidConfigException(String message) {
    super(message);
  }
}
