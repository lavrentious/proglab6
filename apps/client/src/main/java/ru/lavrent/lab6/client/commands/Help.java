package ru.lavrent.lab6.client.commands;

import ru.lavrent.lab6.client.managers.CommandManager;
import ru.lavrent.lab6.common.utils.Commands;

public class Help extends Command {
  private CommandManager commandManager;

  public Help(CommandManager commandManager) {
    super(Commands.HELP, "display this list");
    this.commandManager = commandManager;
  }

  public void execute(String[] args) {
    System.out.println("available commands:");
    int i = 1;
    for (Command command : commandManager.getCmdList()) {
      System.out.println("%d. %s - %s".formatted(i++, command.getName(), command.getDescription()));
    }
  }
}
