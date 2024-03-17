package ru.lavrent.lab6.server.managers;

import ru.lavrent.lab6.server.TCPServer;
import ru.lavrent.lab6.server.commands.Add;
import ru.lavrent.lab6.server.commands.Clear;
import ru.lavrent.lab6.server.commands.Command;
import ru.lavrent.lab6.server.commands.Info;
import ru.lavrent.lab6.server.commands.Show;

import java.io.IOException;

/**
 * RuntimeManager
 */
public class RuntimeManager {
  private RequestManager requestManager;
  private TCPServer tcpServer;
  private CollectionManager collectionManager;

  public RuntimeManager(int port) throws IOException {
    this.collectionManager = new CollectionManager();
    this.requestManager = new RequestManager();
    this.tcpServer = new TCPServer(port, requestManager);
    Command[] commands = new Command[] {
        new Add(this.collectionManager),
        new Clear(this.collectionManager),
        new Show(this.collectionManager),
        new Info(this.collectionManager),
    };
    for (Command cmd : commands) {
      requestManager.addCommand(cmd);
    }
  }

  public void run() throws IOException {
    this.tcpServer.listen();
  }
}