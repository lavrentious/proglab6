package ru.lavrent.lab6.client.commands;

import ru.lavrent.lab6.client.managers.CommandManager;
import ru.lavrent.lab6.client.models.forms.Form;
import ru.lavrent.lab6.client.utils.TCPClient;
import ru.lavrent.lab6.common.models.Difficulty;
import ru.lavrent.lab6.common.network.requests.CountLessThanDifficultyRequest;
import ru.lavrent.lab6.common.network.responses.CountLessThanDifficultyResponse;
import ru.lavrent.lab6.common.utils.Commands;

import java.io.IOException;

public class CountLessThanDifficulty extends Command {
  private TCPClient tcpClient;

  public CountLessThanDifficulty(TCPClient tcpClient) {
    super(Commands.COUNT_LESS_THAN_DIFFICULTY,
        "[difficulty] - print number of elements with diffculty less than given");
    this.tcpClient = tcpClient;
  }

  public void execute(String[] args) throws IOException {
    try {
      final String diffcultyName = CommandManager.ArgsUtils.getIth(args, 0);
      Difficulty diffculty = Form.getEnumValueByString(Difficulty.class, diffcultyName);
      if (diffculty == null) {
        throw new IllegalArgumentException("incorrect difficulty '%s'".formatted(diffcultyName));
      }
      CountLessThanDifficultyResponse res = (CountLessThanDifficultyResponse) tcpClient
          .send(new CountLessThanDifficultyRequest(diffculty));
      System.out.println("number of labworks in collection with difficulty less than %s (%d): %d"
          .formatted(diffculty.name(), diffculty.ordinal(), res.count));
    } catch (IllegalArgumentException e) {
      System.err.println(e.toString());
    }
  }
}
