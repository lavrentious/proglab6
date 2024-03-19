package ru.lavrent.lab6.server.managers;

import ru.lavrent.lab6.common.exceptions.InvalidConfigException;
import ru.lavrent.lab6.server.TCPServer;
import ru.lavrent.lab6.server.commands.Add;
import ru.lavrent.lab6.server.commands.Clear;
import ru.lavrent.lab6.server.commands.Command;
import ru.lavrent.lab6.server.commands.Info;
import ru.lavrent.lab6.server.commands.Show;
import ru.lavrent.lab6.server.exceptions.DeserializationException;
import ru.lavrent.lab6.server.utils.ServerEnvConfig;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

/**
 * RuntimeManager
 */
public class RuntimeManager {
  private RequestManager requestManager;
  private TCPServer tcpServer;
  private CollectionManager collectionManager;
  private ServerEnvConfig config;

  public RuntimeManager() throws IOException {
    this.collectionManager = new CollectionManager();
    this.requestManager = new RequestManager();
    try {
      this.config = ServerEnvConfig.getInstance();
      System.out.println("port=%d ; dbPath=%s".formatted(config.getPort(), config.getDbPath()));
      this.tcpServer = new TCPServer(config.getPort(), requestManager);
      loadCommands();
      loadDb();
    } catch (InvalidConfigException e) {
      System.out.println("invalid config: " + e.getMessage());
    }
  }

  private void loadCommands() {
    Command[] commands = new Command[] {
        new Add(this.collectionManager),
        new Clear(this.collectionManager),
        new Show(this.collectionManager),
        new Info(this.collectionManager),
    };
    for (Command cmd : commands) {
      this.requestManager.addCommand(cmd);
    }
  }

  private void loadDb() {
    String dbPath = config.getDbPath();
    if (dbPath != null) {
      try {
        System.out.println("loading db from " + dbPath);
        collectionManager.loadFromFile(dbPath);
      } catch (FileNotFoundException | AccessDeniedException | DeserializationException e) {
        System.out.println("could not load db: " + e.getMessage());
      }
    }
  }

  public void run() throws IOException {
    this.tcpServer.listen();
  }
}