package ru.lavrent.lab6.common.utils;

import ru.lavrent.lab6.common.exceptions.ValidationException;
import ru.lavrent.lab6.common.interfaces.Validatable;

import java.io.Serializable;

public abstract class Entity extends AutoToString implements Validatable, Serializable {
  public boolean boolValidate() {
    try {
      validate();
      return true;
    } catch (ValidationException e) {
      return false;
    }
  }
}
