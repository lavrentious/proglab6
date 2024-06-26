package ru.lavrent.lab6.client.utils;

import org.apache.commons.lang3.SerializationUtils;
import ru.lavrent.lab6.client.exceptions.RequestFailedException;
import ru.lavrent.lab6.common.network.requests.Request;
import ru.lavrent.lab6.common.network.responses.ErrorResponse;
import ru.lavrent.lab6.common.network.responses.OkResponse;
import ru.lavrent.lab6.common.network.responses.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class TCPClient {
  private final int RESPONSE_BUFFER_SIZE = 16;

  private Socket socket;
  private OutputStream out;
  private InputStream in;
  private String host;
  private int port;
  final private int MAX_RETRIES = 5;
  private int retries;
  private final ByteBuffer intBuffer;

  public TCPClient(String host, int port) throws UnknownHostException, IOException {
    try {
      Class.forName(OkResponse.class.getName()); // load class
      Class.forName(ErrorResponse.class.getName()); // load class
    } catch (ClassNotFoundException e) {
    }
    this.intBuffer = ByteBuffer.allocate(Integer.BYTES);
    this.host = host;
    this.port = port;
    connect();
    System.out.println("connected to " + socket.toString());
  }

  private void connect() throws IOException, UnknownHostException {
    try {
      this.socket = new Socket(host, port);
    } catch (IOException e) {
      this.retries++;
      if (retries > MAX_RETRIES)
        throw new IOException(e);
      System.out.println("trying to reconnect (%d/%d)...".formatted(retries, MAX_RETRIES));
      connect();
    }
    System.out.println("connected to " + socket.toString());
    this.out = socket.getOutputStream();
    this.in = socket.getInputStream();
    this.retries = 0;
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
    try {
      byte[] bytes = SerializationUtils.serialize(request);
      writeInt(bytes.length);
      System.err.println("wrote %d byte header".formatted(bytes.length));
      out.write(bytes);
      System.err.println("wrote bytes".formatted(bytes.length));
      out.flush();

      // read response size
      int responseSize = readInt();
      System.out.println("\n[incoming %d byte response]".formatted(responseSize));
      byte[] responseBytes = this.readResponse(responseSize);

      Response response = SerializationUtils.deserialize(responseBytes);
      if (response instanceof ErrorResponse) {
        ErrorResponse r = (ErrorResponse) response;
        throw new RequestFailedException(r.message);
      }
      return response;
    } catch (IOException e) {
      System.out.println("err while sending request " + e.getMessage());
      System.out.println("trying to reconnect...");
      this.retries = 0;
      connect();
      return send(request);
    }
  }

  private void writeInt(int x) throws IOException {
    intBuffer.clear();
    intBuffer.putInt(x);
    intBuffer.flip();
    this.out.write(intBuffer.array());
  }

  private int readInt() throws IOException {
    intBuffer.clear();
    in.read(intBuffer.array());
    return intBuffer.getInt();
  }

  private byte[] readResponse(int responseSize) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    byte[] chunk = new byte[this.RESPONSE_BUFFER_SIZE];
    int totalRead = 0;
    while (totalRead < responseSize) {
      totalRead += in.read(chunk);
      baos.write(chunk);
    }
    return baos.toByteArray();
  }
}
