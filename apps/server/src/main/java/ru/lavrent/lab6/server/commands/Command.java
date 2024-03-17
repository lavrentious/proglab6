package ru.lavrent.lab6.server.commands;

import ru.lavrent.lab6.common.utils.AutoToString;
import ru.lavrent.lab6.server.interfaces.IServerCommand;

public abstract class Command extends AutoToString implements IServerCommand {
  private String name;

  /**
   * initialize the command
   * 
   * @param name the command's alias
   */
  public Command(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
