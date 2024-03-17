package ru.lavrent.lab6.client.utils;

import org.apache.commons.lang3.SerializationUtils;
import ru.lavrent.lab6.client.exceptions.TCPClientException;
import ru.lavrent.lab6.common.network.requests.Request;
import ru.lavrent.lab6.common.network.responses.ErrorResponse;
import ru.lavrent.lab6.common.network.responses.OkResponse;
import ru.lavrent.lab6.common.network.responses.Response;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient {
  private Socket socket;
  private ObjectInputStream in;
  private ObjectOutputStream out;

  public TCPClient(String host, int port) throws UnknownHostException, IOException {
    try {
      Class.forName(OkResponse.class.getName()); // load class
      Class.forName(ErrorResponse.class.getName()); // load class
    } catch (ClassNotFoundException e) {
    }
    this.socket = connect(host, port);
    System.out.println("connected to " + socket.toString());
  }

  private Socket connect(String host, int port) throws IOException, UnknownHostException {
    Socket socket = new Socket(host, port);
    this.out = new ObjectOutputStream(socket.getOutputStream());
    this.in = new ObjectInputStream(socket.getInputStream());
    return socket;
  }

  public Socket getSocket() {
    return socket;
  }

  public void disconnect() {
    try {
      System.out.println("disconnecting from " + socket.toString());
      socket.close();
      in.close();
      out.close();
    } catch (IOException e) {
    }
  }

  public Response send(Request request) throws IOException {
    byte[] msg = SerializationUtils.serialize(request);
    out.writeObject(msg);
    try {
      return (Response) SerializationUtils.deserialize((byte[]) in.readObject());
    } catch (ClassNotFoundException e) {
      throw new TCPClientException(e);
    }
  }
}
