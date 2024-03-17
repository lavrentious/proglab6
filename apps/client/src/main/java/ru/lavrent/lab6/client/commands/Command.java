package ru.lavrent.lab6.client.commands;

import ru.lavrent.lab6.common.interfaces.Executable;
import ru.lavrent.lab6.common.utils.AutoToString;

import java.io.IOException;

public abstract class Command extends AutoToString implements Executable {
  private String name;
  private String description;

  /**
   * initialize the command
   * 
   * @param name        the command's alias
   * @param description text that is displayed in help
   */
  public Command(String name, String description) {
    this.name = name;
    this.description = description;
  }

  /**
   * execute command with no arguments
   */
  public void execute() throws IOException {
    execute(new String[] {});
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }
}
