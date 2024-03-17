package ru.lavrent.lab6.server.commands;

import ru.lavrent.lab6.common.network.requests.Request;
import ru.lavrent.lab6.common.network.responses.OkResponse;
import ru.lavrent.lab6.common.utils.Commands;
import ru.lavrent.lab6.server.managers.CollectionManager;

import java.io.IOException;

public class Clear extends Command {
  private CollectionManager collectionManager;

  public Clear(CollectionManager collectionManager) {
    super(Commands.CLEAR);
    this.collectionManager = collectionManager;
  }

  public OkResponse execute(Request req) throws IOException {
    collectionManager.clear();
    return new OkResponse();
  }
}
