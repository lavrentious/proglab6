package ru.lavrent.lab6.client.utils;

import ru.lavrent.lab6.common.exceptions.InvalidConfigException;
import ru.lavrent.lab6.common.interfaces.IConfig;

public class ClientEnvConfig implements IConfig {
  private static ClientEnvConfig instance;
  private int port = 5555;

  private ClientEnvConfig() throws InvalidConfigException {
    onLoad();
    validate();
  }

  public static ClientEnvConfig getInstance() throws InvalidConfigException {
    if (instance == null) {
      instance = new ClientEnvConfig();
    }
    return instance;
  }

  @Override
  public void onLoad() {
    String port = System.getenv("port");
    if (port != null)
      this.port = Integer.parseInt(port);
  }

  @Override
  public void validate() throws InvalidConfigException {
    if (port <= 0 || port >= 65536) {
      throw new InvalidConfigException("'port' must be > 0 and < 65536");
    }
  }

  public int getPort() {
    return port;
  }
}
