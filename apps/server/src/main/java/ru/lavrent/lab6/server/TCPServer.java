package ru.lavrent.lab6.server;

import org.apache.commons.lang3.SerializationUtils;
import ru.lavrent.lab6.common.network.requests.Request;
import ru.lavrent.lab6.common.network.responses.Response;
import ru.lavrent.lab6.server.managers.RequestManager;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {
  private ServerSocket serverSocket;
  private ObjectInputStream in;
  private ObjectOutputStream out;
  private Socket socket;
  private RequestManager requestManager;

  public TCPServer(int port, RequestManager requestManager) throws IOException {
    this.serverSocket = new ServerSocket(4564);
    this.requestManager = requestManager;
  }

  private Socket waitForConnection() throws IOException {
    Socket socket = serverSocket.accept();
    System.out.println("Client connected: " + socket.toString());
    this.in = new ObjectInputStream(socket.getInputStream());
    this.out = new ObjectOutputStream(socket.getOutputStream());
    return socket;
  }

  private void disconnect() {
    try {
      this.socket.close();
      System.out.println("client %s disconnected".formatted(socket.toString()));
      this.in.close();
      this.out.close();
    } catch (IOException e) {
    }
  }

  public void listen() throws IOException {
    byte[] message;
    while (true) {
      System.out.println("awaiting connection...");
      this.socket = waitForConnection();
      while (true) {
        try {
          message = (byte[]) in.readObject();
          Request req = SerializationUtils.deserialize(message);
          System.out.println("Handling request '%s' (from %s)".formatted(req.getName(), socket.getInetAddress()));
          Response res = requestManager.handleRequest(req);
          out.writeObject(SerializationUtils.serialize(res));
        } catch (ClassNotFoundException e) {
          System.out.println("class not found: " + e.getMessage());
          break;
        } catch (EOFException e) {
          break;
        }
      }
      this.disconnect();
    }
  }
}
