package ru.lavrent.lab6.client.managers;

import ru.lavrent.lab6.client.commands.Add;
import ru.lavrent.lab6.client.commands.Clear;
import ru.lavrent.lab6.client.commands.Command;
import ru.lavrent.lab6.client.commands.ExecuteScript;
import ru.lavrent.lab6.client.commands.Exit;
import ru.lavrent.lab6.client.commands.Help;
import ru.lavrent.lab6.client.commands.History;
import ru.lavrent.lab6.client.commands.Info;
import ru.lavrent.lab6.client.commands.Show;
import ru.lavrent.lab6.client.utils.ClientEnvConfig;
import ru.lavrent.lab6.client.utils.Reader;
import ru.lavrent.lab6.client.utils.TCPClient;
import ru.lavrent.lab6.common.exceptions.InvalidConfigException;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * RuntimeManager
 */
public class RuntimeManager {
  private CommandManager commandManager;
  private Reader reader;
  private TCPClient tcpClient;
  private ClientEnvConfig config;

  public RuntimeManager(TCPClient tcpClient, String filePath) throws UnknownHostException, IOException {
    this.commandManager = new CommandManager();
    this.tcpClient = tcpClient;
    this.reader = new Reader(commandManager, filePath, null);
    try {
      this.config = ClientEnvConfig.getInstance();
      System.out.println("port=%d".formatted(config.getPort()));
      getReader().setOnHalt(tcpClient::disconnect);
      loadCommands();
    } catch (InvalidConfigException e) {
      System.out.println("invalid config: " + e.getMessage());
    }
  }

  private void loadCommands() {
    Command[] commands = new Command[] {
        new Info(tcpClient),
        new Show(tcpClient),
        new Clear(tcpClient),
        new ExecuteScript(tcpClient),
        new Help(commandManager),
        new History(commandManager),
        new Exit(reader),
        new Add(reader, tcpClient),
    };
    for (Command cmd : commands) {
      commandManager.addCommand(cmd);
    }
  }

  public void run() {
    getReader().read();
  }

  public CommandManager getCommandManager() {
    return commandManager;
  }

  public Reader getReader() {
    return reader;
  }
}