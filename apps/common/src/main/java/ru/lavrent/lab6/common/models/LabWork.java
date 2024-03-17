package ru.lavrent.lab6.common.models;

import ru.lavrent.lab6.common.exceptions.ValidationException;

public class LabWork extends DryLabWork implements Comparable<LabWork> {
  final Long id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого
  // поля должно быть уникальным, Значение этого поля должно генерироваться
  // автоматически

  public LabWork(long id, String name, Coordinates coordinates, java.time.ZonedDateTime creationDate, Long minimalPoint,
      Difficulty difficulty, Discipline discipline) throws ValidationException {
    super(name, coordinates, creationDate, minimalPoint, difficulty, discipline);
    this.id = id;
  }

  public LabWork(DryLabWork labWork, long id) {
    super(labWork.name, labWork.coordinates, labWork.creationDate, labWork.minimalPoint, labWork.difficulty,
        labWork.discipline);
    this.id = id;
    validateId(id);
  }

  public long getId() {
    return id;
  }

  @Override
  public void validate() throws ValidationException {
    super.validate();
  }

  public static void validateId(Long id) throws ValidationException {
    if (id == null) {
      throw new ValidationException("id must be not null");
    }
    if (id <= 0) {
      throw new ValidationException("id must be greater than 0");
    }
  }

  @Override
  public int compareTo(LabWork o) {
    return Long.compare(getId(), o.getId()); // FIXME: what do i compare by??
  }
}
