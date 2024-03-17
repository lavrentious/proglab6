package ru.lavrent.lab6.common.interfaces;

import ru.lavrent.lab6.common.exceptions.ValidationException;

public interface Validatable {
  void validate() throws ValidationException;
}
