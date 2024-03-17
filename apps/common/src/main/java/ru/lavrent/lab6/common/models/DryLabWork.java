package ru.lavrent.lab6.common.models;

import ru.lavrent.lab6.common.exceptions.ValidationException;
import ru.lavrent.lab6.common.utils.Entity;

import java.time.ZonedDateTime;

/**
 * сущность лабораторной работы без ID (для локального использования на клиенте)
 */
public class DryLabWork extends Entity {
  String name; // Поле не может быть null, Строка не может быть пустой
  Coordinates coordinates; // Поле не может быть null
  java.time.ZonedDateTime creationDate; // Поле не может быть null, Значение этого поля должно генерироваться
                                        // автоматически
  Long minimalPoint; // Поле может быть null, Значение поля должно быть больше 0
  Difficulty difficulty; // Поле не может быть null
  Discipline discipline; // Поле не может быть null

  public DryLabWork(String name, Coordinates coordinates, java.time.ZonedDateTime creationDate,
      Long minimalPoint,
      Difficulty difficulty, Discipline discipline) throws ValidationException {

    this.name = name;
    this.coordinates = coordinates;
    this.creationDate = creationDate;
    this.minimalPoint = minimalPoint;
    this.difficulty = difficulty;
    this.discipline = discipline;
    validate();
  }

  public String getName() {
    return name;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public java.time.ZonedDateTime getCreationDate() {
    return creationDate;
  }

  public long getMinimalPoint() {
    return minimalPoint;
  }

  public Difficulty getDifficulty() {
    return difficulty;
  }

  public Discipline getDiscipline() {
    return discipline;
  }

  @Override
  public void validate() throws ValidationException {
    validateName(name);
    validateCoordinates(coordinates);
    validateCreationDate(creationDate);
    validateMinimalPoint(minimalPoint);
    validateDifficulty(difficulty);
    validateDiscipline(discipline);
  }

  public static void validateName(String name) throws ValidationException {
    if (name == null) {
      throw new ValidationException("name must not be null");
    }
    if (name.isEmpty()) {
      throw new ValidationException("name must not be empty");
    }
  }

  public static void validateCoordinates(Coordinates coordinates) throws ValidationException {
    if (coordinates == null) {
      throw new ValidationException("coordinates must be not null");
    }
  }

  public static void validateCreationDate(ZonedDateTime creationDate) throws ValidationException {
    if (creationDate == null) {
      throw new ValidationException("creationDate must be not null");
    }
  }

  public static void validateMinimalPoint(Long minimalPoint) throws ValidationException {
    if (minimalPoint == null) {
      throw new ValidationException("minimalPoint must not be null");
    }
    if (minimalPoint <= 0) {
      throw new ValidationException("minimalPoint must be greater than 0");
    }
  }

  public static void validateDifficulty(Difficulty difficulty) throws ValidationException {
    if (difficulty == null) {
      throw new ValidationException("difficulty must be not null");
    }
  }

  public static void validateDiscipline(Discipline discipline) throws ValidationException {
    if (discipline == null) {
      throw new ValidationException("discipline must be not null");
    }
  }
}
