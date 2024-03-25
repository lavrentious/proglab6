package ru.lavrent.lab6.client.managers;

import ru.lavrent.lab6.client.commands.Command;
import ru.lavrent.lab6.client.exceptions.RequestFailedException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * class responsible for organizing and invoking commands
 */
public class CommandManager {
  private HashMap<String, Command> commands;
  private ArrayList<String> history;

  public CommandManager() {
    commands = new HashMap<>();
    history = new ArrayList<>();
  }

  public void addCommand(Command command) {
    commands.put(command.getName(), command);
  }

  public HashMap<String, Command> getCmdMap() {
    return commands;
  }

  public List<Command> getCmdList() {
    return new ArrayList<>(commands.values());
  }

  public Command getCommand(String name) {
    return commands.get(name);
  }

  public void execute(String input) {
    String commandName = ArgsUtils.getCommandFromInput(input);
    String[] commandArgs = ArgsUtils.getArgsFromInput(input);

    Command command = getCommand(commandName);
    if (command != null) {
      try {
        command.execute(commandArgs);
        history.add(commandName);
      } catch (RequestFailedException e) {
        System.out.println("server refused the request: " + e.getMessage());
      } catch (IOException e) {
        System.out.println("error while sending request: " + e.getMessage());
      }
    } else {
      System.err.println("unknown command \"%s\" ".formatted(commandName));
    }
  }

  public List<String> getLastNHistoryItems(int n) {
    return history.subList(Math.max(history.size() - n, 0), history.size());
  }

  /**
   * utilites for processing command arguments
   */
  public static class ArgsUtils {
    /**
     * return the command assuming it's the first word
     * 
     * @param input user input
     * @return the invoked commad
     */
    public static String getCommandFromInput(String input) {
      String[] parts = input.split("\\s+");
      return parts[0];
    }

    public static String[] getArgsFromInput(String input) {
      String[] parts = input.split("\\s+");
      String[] cmdArgs = new String[parts.length - 1];
      System.arraycopy(parts, 1, cmdArgs, 0, cmdArgs.length);
      return cmdArgs;
    }

    /**
     * retrieve i-th argument
     * 
     * @param args command's args list
     * @param i    the number (from 0)
     * @return the i-th arg
     * @throws IllegalArgumentException i-th arument is not found
     */
    public static String getIth(String[] args, int i) {
      if (i < 0 || i >= args.length) {
        throw new IllegalArgumentException("argument with index %d not found".formatted(i));
      }
      return args[i];
    }

    public static int getInt(String[] args, int i) {
      return Integer.parseInt(getIth(args, i));
    }

    public static long getLong(String[] args, int i) {
      return Long.parseLong(getIth(args, i));
    }
  }
}
