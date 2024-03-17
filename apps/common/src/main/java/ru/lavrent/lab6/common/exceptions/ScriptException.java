package ru.lavrent.lab6.common.exceptions;

/**
 * exception meaning that some script at some point invokes itself (endlessly)
 */
public class ScriptException extends RuntimeException {
  public ScriptException(String message, Throwable cause) {
    super(message, cause);
  }

  public ScriptException(String message) {
    super(message);
  }

  public ScriptException(Throwable e) {
    super(e);
  }

  public ScriptException() {
    super();
  }
}
