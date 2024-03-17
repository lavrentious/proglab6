package ru.lavrent.lab6.common.interfaces;

import ru.lavrent.lab6.common.exceptions.ValidationException;

@FunctionalInterface
public interface ValidatorFn<T> {
  void validate(T t) throws ValidationException;
}