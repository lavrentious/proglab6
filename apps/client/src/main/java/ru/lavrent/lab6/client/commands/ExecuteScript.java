package ru.lavrent.lab6.client.commands;

import ru.lavrent.lab6.client.managers.CommandManager;
import ru.lavrent.lab6.client.managers.RuntimeManager;
import ru.lavrent.lab6.client.utils.TCPClient;
import ru.lavrent.lab6.common.utils.Commands;

import java.io.IOException;
import java.net.UnknownHostException;

public class ExecuteScript extends Command {
  private TCPClient tcpClient;

  public ExecuteScript(TCPClient tcpClient) {
    super(Commands.EXECUTE_SCRIPT, "[file] run script from the specified file");
    this.tcpClient = tcpClient;
  }

  public void execute(String[] args) throws IOException {
    try {
      final String filePath = CommandManager.ArgsUtils.getIth(args, 0);
      RuntimeManager runtimeManager = new RuntimeManager(tcpClient, filePath);
      runtimeManager.getReader().read();
    } catch (IllegalArgumentException e) {
      System.err.println("argument error: " + e.getMessage());
    } catch (UnknownHostException e) {
      System.err.println("unknown host: " + e.getMessage());
    }
  }
}
