package ru.lavrent.lab6.server.commands;

import ru.lavrent.lab6.common.network.requests.CountLessThanDifficultyRequest;
import ru.lavrent.lab6.common.network.requests.Request;
import ru.lavrent.lab6.common.network.responses.CountLessThanDifficultyResponse;
import ru.lavrent.lab6.common.utils.Commands;
import ru.lavrent.lab6.server.managers.CollectionManager;

import java.io.IOException;

public class CountLessThanDifficulty extends Command {
  private CollectionManager collectionManager;

  public CountLessThanDifficulty(CollectionManager collectionManager) {
    super(Commands.COUNT_LESS_THAN_DIFFICULTY);
    this.collectionManager = collectionManager;
  }

  public CountLessThanDifficultyResponse execute(Request req) throws IOException {
    CountLessThanDifficultyRequest request = (CountLessThanDifficultyRequest) req;
    int ans = (int) collectionManager.getList().stream()
        .filter(lw -> lw.getDifficulty().ordinal() < request.difficulty.ordinal()).count();
    var res = new CountLessThanDifficultyResponse(ans);
    return res;
  }
} 
