package ru.lavrent.lab6.client;

import ru.lavrent.lab6.client.managers.RuntimeManager;
import ru.lavrent.lab6.client.utils.ClientEnvConfig;
import ru.lavrent.lab6.client.utils.TCPClient;

import java.net.UnknownHostException;

public class Main {
  public static void main(String[] args) {
    try {
      RuntimeManager runtimeManager = new RuntimeManager(
          new TCPClient("localhost", ClientEnvConfig.getInstance().getPort()), null);
      runtimeManager.run();
    } catch (UnknownHostException e) {
      System.out.println("unknown host " + e.getMessage());
    } catch (Exception e) {
      System.out.println("error while connecting " + e.getMessage());
    }
  }
}
