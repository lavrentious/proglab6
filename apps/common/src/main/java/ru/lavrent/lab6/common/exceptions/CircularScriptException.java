package ru.lavrent.lab6.common.exceptions;

/**
 * exception meaning that some script at some point invokes itself (endlessly)
 */
public class CircularScriptException extends ScriptException {
  public CircularScriptException(String message, Throwable cause) {
    super(message, cause);
  }

  public CircularScriptException(String message) {
    super(message);
  }

  public CircularScriptException(Throwable e) {
    super(e);
  }

  public CircularScriptException() {
    super();
  }
}
