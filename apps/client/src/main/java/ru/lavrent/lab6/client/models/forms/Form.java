package ru.lavrent.lab6.client.models.forms;

import ru.lavrent.lab6.common.exceptions.ScriptException;
import ru.lavrent.lab6.common.exceptions.ValidationException;
import ru.lavrent.lab6.common.interfaces.ValidatorFn;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public abstract class Form<T> {
  /**
   * run the form and return the created result
   * 
   * @param scanner
   * @return entered entity
   * @throws ValidationException
   */
  public abstract T run(Scanner scanner) throws ValidationException;

  /**
   * @param message     message before input
   * @param scanner     Scanner instance to use
   * @param validatorFn function to validate input
   * @return entered int/null
   */
  public static Integer getInt(String message, Scanner scanner, ValidatorFn<Integer> validatorFn) {
    do {
      System.out.print(message);
      try {
        String line = scanner.nextLine();
        Integer ans = line.isEmpty() ? null : Integer.parseInt(line);
        if (validatorFn != null)
          validatorFn.validate(ans);
        return ans;
      } catch (ValidationException e) {
        System.out.println("validation failed: %s. retry.".formatted(e.getMessage()));
      } catch (NumberFormatException e) {
        System.out.println("incorrect number format %s. retry.".formatted(e.getMessage()));
      } catch (NoSuchElementException e) {
        System.out.println("no line found: %s".formatted(e.getMessage()));
      }
    } while (true);
  }

  /**
   * @param message     message before input
   * @param scanner     Scanner instance to use
   * @param validatorFn function to validate input
   * @return entered long/null
   */
  public static Long getLong(String message, Scanner scanner, ValidatorFn<Long> validatorFn) {
    do {
      System.out.print(message);
      try {
        String line = scanner.nextLine();
        Long ans = line.isEmpty() ? null : Long.parseLong(line);
        if (validatorFn != null)
          validatorFn.validate(ans);
        return ans;
      } catch (ValidationException e) {
        System.out.println("validation failed: %s. retry.".formatted(e.getMessage()));
      } catch (NumberFormatException e) {
        System.out.println("incorrect number format %s. retry.".formatted(e.getMessage()));
      } catch (NoSuchElementException e) {
        throw new ScriptException("no line found: %s".formatted(e.getMessage()));
      }
    } while (true);
  }

  /**
   * @param message     message before input
   * @param scanner     Scanner instance to use
   * @param validatorFn function to validate input
   * @return entered string
   */
  public static String getString(String message, Scanner scanner, ValidatorFn<String> validatorFn) {
    do {
      System.out.print(message);
      try {
        String ans = scanner.nextLine();
        if (validatorFn != null)
          validatorFn.validate(ans);
        return ans;
      } catch (ValidationException e) {
        System.out.println("validation failed: %s. retry.".formatted(e.getMessage()));
      } catch (NoSuchElementException e) {
        throw new ScriptException("no line found: %s".formatted(e.getMessage()));
      }
    } while (true);
  }

  /**
   * 
   * @param message      message before input
   * @param scanner      Scanner instance to use
   * @param defaultValue default value
   * @return entered boolean/default boolean
   */
  public static boolean getYN(String message, Scanner scanner, Boolean defaultValue) {
    do {
      String choice = getString(message, scanner, null);
      try {
        if (defaultValue != null) {
          if (choice.equalsIgnoreCase("y"))
            return true;
          if (choice.equalsIgnoreCase("n"))
            return false;
          return defaultValue;
        } else if (!choice.equalsIgnoreCase("y") && !choice.equalsIgnoreCase("n")) {
          throw new InputMismatchException("enter y or n. retry.");
        }
        return choice.equalsIgnoreCase("y");
      } catch (InputMismatchException e) {
        System.err.println(e.getMessage());
      } catch (NoSuchElementException e) {
        throw new ScriptException("no line found: %s".formatted(e.getMessage()));
      }
    } while (true);
  }

  /**
   * input a enum value by its constant name
   * 
   * @param <T>       enum
   * @param message   message before input
   * @param enumClass enum class
   * @param scanner   Scanner instance to use
   * @return
   */
  public static <T extends Enum<T>> T getEnumValue(String message, Class<T> enumClass, Scanner scanner) {
    do {
      System.out.println(message);
      for (T option : enumClass.getEnumConstants()) {
        System.out.println(option.ordinal() + ". " + option.name());
      }
      String choice = getString("enter name: ", scanner, null);
      for (T option : enumClass.getEnumConstants()) {
        if (choice.equalsIgnoreCase(option.name())) {
          return option;
        }
      }
      System.err.println("invalid enum key. retry.");
    } while (true);
  }
}
