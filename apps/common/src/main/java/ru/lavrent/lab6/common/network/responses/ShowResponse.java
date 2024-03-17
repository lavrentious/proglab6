package ru.lavrent.lab6.common.network.responses;

import ru.lavrent.lab6.common.models.LabWork;
import ru.lavrent.lab6.common.utils.Commands;

import java.util.List;

public class ShowResponse extends Response {
  public final List<LabWork> list;

  public ShowResponse(List<LabWork> list) {
    super(Commands.SHOW);
    this.list = list;
  }
}
