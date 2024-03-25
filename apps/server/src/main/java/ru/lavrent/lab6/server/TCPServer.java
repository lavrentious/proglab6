package ru.lavrent.lab6.server;

import org.apache.commons.lang3.SerializationUtils;
import ru.lavrent.lab6.common.network.requests.Request;
import ru.lavrent.lab6.common.network.responses.ErrorResponse;
import ru.lavrent.lab6.common.network.responses.Response;
import ru.lavrent.lab6.server.exceptions.BadRequest;
import ru.lavrent.lab6.server.managers.RequestManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class TCPServer {
  private final int RESPONSE_BUFFER_SIZE = 16;

  private ServerSocketChannel serverSocketChannel;
  private SocketChannel client;
  private RequestManager requestManager;

  public TCPServer(int port, RequestManager requestManager) throws IOException {
    this.serverSocketChannel = ServerSocketChannel.open();
    this.serverSocketChannel.bind(new InetSocketAddress(port));
    this.requestManager = requestManager;
  }

  private SocketChannel waitForConnection() throws IOException {
    SocketChannel client = this.serverSocketChannel.accept();
    System.out.println("Client connected: " + client.toString());
    return client;
  }

  private void disconnect() {
    try {
      this.client.close();
      System.out.println("client %s disconnected".formatted(client.toString()));
    } catch (IOException e) {
    }
  }

  public void listen() throws IOException {
    while (true) {
      System.out.println("awaiting connection...");
      this.client = waitForConnection();
      handleClient(client);
      this.disconnect();
    }
  }

  private void handleClient(SocketChannel client) throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(RESPONSE_BUFFER_SIZE);
    while (true) {
      // deserialize and handle request
      int requestSize = this.readInt();
      if (requestSize == -1) {
        break;
      }
      byte[] requestBytes = this.readResponse(requestSize);
      Request request = SerializationUtils.deserialize(requestBytes);
      System.out.println(
          "Received %s from %s (%d bytes)".formatted(request.getName(), client.getRemoteAddress(), requestSize));

      // send response
      try {
        Response response = this.requestManager.handleRequest(request);
        this.sendResponse(response);
      } catch (BadRequest e) {
        this.sendResponse(new ErrorResponse(e.getMessage()));
      }
    }
  }

  private void sendResponse(Response response) throws IOException {
    byte[] responseBytes = SerializationUtils.serialize(response);
    this.writeInt(responseBytes.length);
    this.client.write(ByteBuffer.wrap(responseBytes));
  }

  private void writeInt(int x) throws IOException {
    ByteBuffer responseSizeBuffer = ByteBuffer.allocate(Integer.BYTES);
    responseSizeBuffer.putInt(x);
    responseSizeBuffer.flip();
    this.client.write(responseSizeBuffer);
  }

  private int readInt() throws IOException {
    ByteBuffer data = ByteBuffer.allocate(Integer.BYTES);
    int size = client.read(data);
    if (size == -1) {
      return -1;
    }
    return data.flip().getInt();
  }

  private byte[] readResponse(int responseSize) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ByteBuffer chunk = ByteBuffer.allocate(this.RESPONSE_BUFFER_SIZE);
    int totalRead = 0;
    while (totalRead < responseSize) {
      totalRead += client.read(chunk);
      chunk.flip();
      baos.write(chunk.array());
    }
    return baos.toByteArray();
  }
}
