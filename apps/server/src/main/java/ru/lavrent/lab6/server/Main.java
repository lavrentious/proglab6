package ru.lavrent.lab6.server;

import ru.lavrent.lab6.server.managers.RuntimeManager;

import java.io.IOException;

public class Main {
  public static void main(String[] args) {
    try {
      RuntimeManager runtimeManager = new RuntimeManager();
      runtimeManager.run();
    } catch (IOException e) {
      System.out.println("io exception in main: " + e.getMessage());
    }
  }
}
