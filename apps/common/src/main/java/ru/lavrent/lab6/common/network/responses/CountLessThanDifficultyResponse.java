package ru.lavrent.lab6.common.network.responses;

import ru.lavrent.lab6.common.utils.Commands;

public class CountLessThanDifficultyResponse extends Response {
  public final int count;

  public CountLessThanDifficultyResponse(int count) {
    super(Commands.COUNT_LESS_THAN_DIFFICULTY);
    this.count = count;
  }
}