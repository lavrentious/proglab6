package ru.lavrent.lab6.client.commands;

import ru.lavrent.lab6.client.utils.TCPClient;
import ru.lavrent.lab6.common.network.requests.BlankRequest;
import ru.lavrent.lab6.common.network.responses.InfoResponse;
import ru.lavrent.lab6.common.utils.Commands;

import java.io.IOException;

public class Info extends Command {
  private TCPClient tcpClient;

  public Info(TCPClient tcpClient) {
    super(Commands.INFO, "info about the collection");
    this.tcpClient = tcpClient;
  }

  public void execute(String[] args) throws IOException {
    InfoResponse res = (InfoResponse) tcpClient.send(new BlankRequest(Commands.INFO));
    System.out.println("current collection:");
    System.out.println("type: %s".formatted(res.type));
    System.out.println("creation time: %s %s".formatted(res.createdAt.toLocalDate(), res.createdAt.toLocalTime()));
    System.out.println("last update time: %s %s".formatted(res.updatedAt.toLocalDate(),
        res.updatedAt.toLocalTime()));
  }
}
