package ru.lavrent.lab6.client.models.forms;

import ru.lavrent.lab6.common.exceptions.ValidationException;
import ru.lavrent.lab6.common.models.Discipline;

import java.util.Scanner;

public class DisciplineForm extends Form<Discipline> {
  public Discipline run(Scanner scanner) throws ValidationException {
    System.out.println("create discipline");
    String name = getString("discipline name: ", scanner, Discipline::validateName);
    long lectureHours = getLong("lecture hours: ", scanner, Discipline::validateLectureHours);
    Long practiceHours = getLong("practice hours: ", scanner, Discipline::validatePracticeHours);
    Integer labsCount = getInt("labs count: ", scanner, Discipline::validateLabsCount);
    return new Discipline(name, lectureHours, practiceHours, labsCount);
  }
}