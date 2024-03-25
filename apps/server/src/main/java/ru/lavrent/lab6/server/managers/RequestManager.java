package ru.lavrent.lab6.server.managers;

import ru.lavrent.lab6.common.network.requests.BlankRequest;
import ru.lavrent.lab6.common.network.requests.Request;
import ru.lavrent.lab6.common.network.responses.Response;
import ru.lavrent.lab6.server.commands.Command;
import ru.lavrent.lab6.server.exceptions.BadRequest;

import java.io.IOException;
import java.util.HashMap;

public class RequestManager {
  private HashMap<String, Command> commands;

  public RequestManager() {
    try {
      Class.forName(BlankRequest.class.getName()); // load class
    } catch (ClassNotFoundException e) {
    }
    commands = new HashMap<>();
  }

  public Response handleRequest(Request request) throws IOException, BadRequest {
    Command command = commands.get(request.getName());
    if (command == null) {
      throw new BadRequest("unknown command: " + request.getName());
    }
    return command.execute(request);
  }

  public void addCommand(Command command) {
    commands.put(command.getName(), command);
  }
}
