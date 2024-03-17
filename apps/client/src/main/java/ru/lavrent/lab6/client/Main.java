package ru.lavrent.lab6.client;

import ru.lavrent.lab6.client.managers.RuntimeManager;
import ru.lavrent.lab6.client.utils.TCPClient;

import java.io.IOException;
import java.net.UnknownHostException;

public class Main {
  public static void main(String[] args) {
    try {
      RuntimeManager runtimeManager = new RuntimeManager(new TCPClient("localhost", 4564), null);
      runtimeManager.run();
    } catch (UnknownHostException e) {
      System.out.println("unknown host " + e.getMessage());
    } catch (IOException e) {
      System.out.println("error while connecting " + e.getMessage());
    }
  }
}
