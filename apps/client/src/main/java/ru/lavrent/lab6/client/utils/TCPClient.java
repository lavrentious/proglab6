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

  public TCPClient(String host, int port) throws UnknownHostException, IOException {
    try {
      Class.forName(OkResponse.class.getName()); // load class
      Class.forName(ErrorResponse.class.getName()); // load class
    } catch (ClassNotFoundException e) {
    }
    connect(host, port);
    System.out.println("connected to " + socket.toString());
  }

  private void connect(String host, int port) throws IOException, UnknownHostException {
    this.socket = new Socket(host, port);
    System.out.println("connected to " + socket.toString());
    this.out = socket.getOutputStream();
    this.in = socket.getInputStream();
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
    byte[] bytes = SerializationUtils.serialize(request);
    writeInt(bytes.length);
    out.write(bytes);
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
  }

  private void writeInt(int x) throws IOException {
    ByteBuffer responseSizeBuffer = ByteBuffer.allocate(Integer.BYTES);
    responseSizeBuffer.putInt(x);
    responseSizeBuffer.flip();
    this.out.write(responseSizeBuffer.array());
  }

  private int readInt() throws IOException {
    byte[] responseSizeBytes = new byte[Integer.BYTES];
    in.read(responseSizeBytes);
    return ByteBuffer.wrap(responseSizeBytes).getInt();
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
