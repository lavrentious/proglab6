package ru.lavrent.lab6.common.models;

import ru.lavrent.lab6.common.exceptions.ValidationException;
import ru.lavrent.lab6.common.utils.Entity;

public class Coordinates extends Entity {
  private Long x; // Поле не может быть null
  private Integer y; // Значение поля должно быть больше -498, Поле не может быть null

  public Coordinates(Long x, Integer y) throws ValidationException {
    this.x = x;
    this.y = y;
    validate();
  }

  public long getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public static void validateX(Long x) throws ValidationException {
    if (x == null) {
      throw new ValidationException("x must not be null");
    }
  }

  public static void validateY(Integer y) throws ValidationException {
    if (y == null) {
      throw new ValidationException("y must not be null");
    }
    if (y <= -498) {
      throw new ValidationException("y must be > -498");
    }
  }

  @Override
  public void validate() throws ValidationException {
    validateX(x);
    validateY(y);
  }
}