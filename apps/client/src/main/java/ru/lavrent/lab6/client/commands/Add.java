package ru.lavrent.lab6.client.commands;

import ru.lavrent.lab6.client.models.forms.LabWorkForm;
import ru.lavrent.lab6.client.utils.Reader;
import ru.lavrent.lab6.client.utils.TCPClient;
import ru.lavrent.lab6.common.exceptions.ValidationException;
import ru.lavrent.lab6.common.models.DryLabWork;
import ru.lavrent.lab6.common.network.requests.AddRequest;
import ru.lavrent.lab6.common.network.responses.AddResponse;
import ru.lavrent.lab6.common.utils.Commands;

import java.io.IOException;

public class Add extends Command {
  private Reader reader;
  private TCPClient tcpClient;

  public Add(Reader reader, TCPClient tcpClient) {
    super(Commands.ADD, "add a new element to collection");
    this.reader = reader;
    this.tcpClient = tcpClient;
  }

  public void execute(String[] args) throws IOException {
    try {
      DryLabWork labWork = reader.runForm(new LabWorkForm());
      AddResponse res = (AddResponse) tcpClient.send(new AddRequest(labWork));
      System.out.println("labwork created: " + res.newId);
    } catch (ValidationException e) {
      System.err.println("labwork validation failed: " + e.toString());
    }
  }
}
