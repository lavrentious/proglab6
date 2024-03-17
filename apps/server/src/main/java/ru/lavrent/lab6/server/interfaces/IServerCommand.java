package ru.lavrent.lab6.server.interfaces;

import ru.lavrent.lab6.common.network.requests.Request;
import ru.lavrent.lab6.common.network.responses.Response;

import java.io.IOException;

public interface IServerCommand {
  public Response execute(Request request) throws IOException;
}
