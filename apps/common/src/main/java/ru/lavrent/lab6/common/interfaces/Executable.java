package ru.lavrent.lab6.common.interfaces;

import java.io.IOException;

public interface Executable {
  /**
   * execute the command
   * 
   * @param args arguments the command takes
   */
  public void execute(String[] args) throws IOException;
}
