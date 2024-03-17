package ru.lavrent.lab6.common.network.responses;

import ru.lavrent.lab6.common.utils.Commands;

public class AddResponse extends Response {
  public final long newId;

  public AddResponse(long newId) {
    super(Commands.ADD);
    this.newId = newId;
  }
}
