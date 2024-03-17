package ru.lavrent.lab6.common.network.responses;

import ru.lavrent.lab6.common.utils.Commands;

import java.time.LocalDateTime;

public class InfoResponse extends Response {
  public final String type;
  public final LocalDateTime createdAt;
  public final LocalDateTime updatedAt;

  public InfoResponse(String type,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    super(Commands.SHOW);
    this.type = type;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
}
