package ru.lavrent.lab6.common.interfaces;

import ru.lavrent.lab6.common.exceptions.InvalidConfigException;

public interface IConfig {
  public void onLoad();

  public void validate() throws InvalidConfigException;
}
