package ru.lavrent.lab6.server;

import org.apache.commons.lang3.SerializationUtils;
import ru.lavrent.lab6.common.network.requests.Request;
import ru.lavrent.lab6.common.network.responses.ErrorResponse;
import ru.lavrent.lab6.common.network.responses.Response;
import ru.lavrent.lab6.server.exceptions.BadRequest;
import ru.lavrent.lab6.server.managers.RequestManager;
import ru.lavrent.lab6.server.managers.RuntimeManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TCPServer {
  private static final int RESPONSE_BUFFER_SIZE = 1024;
  private final RequestManager requestManager;
  private final Selector selector;

  public TCPServer(int port, RequestManager requestManager) throws IOException {
    this.requestManager = requestManager;
    ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.bind(new InetSocketAddress(port));
    serverSocketChannel.configureBlocking(false);
    selector = Selector.open();
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
  }

  public void listen() throws IOException {
    while (true) {
      selector.select();
      Set<SelectionKey> selectedKeys = selector.selectedKeys();
      Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

      while (keyIterator.hasNext()) {
        SelectionKey key = keyIterator.next();

        if (key.isAcceptable()) {
          handleConnect(key);
        } else if (key.isReadable()) {
          handleReadable(key);
        }
        keyIterator.remove();
      }
    }
  }

  private void disconnect(SocketChannel client) {
    try {
      RuntimeManager.logger.info("%s disconnected".formatted(client.getRemoteAddress()));
      client.close();
    } catch (IOException e) {
    }
  }

  private void handleConnect(SelectionKey key) throws IOException {
    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
    SocketChannel client = serverSocketChannel.accept();
    client.configureBlocking(false);
    client.register(selector, SelectionKey.OP_READ);
    RuntimeManager.logger.info("%s connected".formatted(client.getRemoteAddress()));
  }

  private void handleReadable(SelectionKey key) throws IOException {
    SocketChannel client = (SocketChannel) key.channel();
    int requestSize = this.readInt(client);
    if (requestSize == -1) {
      disconnect(client);
      return;
    }
    byte[] requestBytes = this.readRequest(client, requestSize);
    Request request = SerializationUtils.deserialize(requestBytes);
    RuntimeManager.logger.info(
        "received %s from %s (%d bytes)".formatted(request.getName(), client.getRemoteAddress(), requestSize));

    try {
      Response response = requestManager.handleRequest(request);
      sendResponse(client, response);
    } catch (BadRequest e) {
      sendResponse(client, new ErrorResponse(e.getMessage()));
    } catch (IOException e) {
      disconnect(client);
    }
  }

  private void writeInt(SocketChannel client, int x) throws IOException {
    ByteBuffer responseSizeBuffer = ByteBuffer.allocate(Integer.BYTES);
    responseSizeBuffer.putInt(x);
    responseSizeBuffer.flip();
    client.write(responseSizeBuffer);
  }

  private void sendResponse(SocketChannel client, Response response) throws IOException {
    byte[] responseBytes = SerializationUtils.serialize(response);
    this.writeInt(client, responseBytes.length);
    client.write(ByteBuffer.wrap(responseBytes));
  }

  private int readInt(SocketChannel client) throws IOException {
    ByteBuffer data = ByteBuffer.allocate(Integer.BYTES);
    int size = client.read(data);
    if (size == -1) {
      return -1;
    }
    return data.flip().getInt();
  }

  private byte[] readRequest(SocketChannel client, int responseSize) throws IOException {
    ByteBuffer buffer = ByteBuffer.allocate(RESPONSE_BUFFER_SIZE);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    int totalRead = 0;
    while (totalRead < responseSize) {
      totalRead += client.read(buffer);
      buffer.flip();
      baos.write(buffer.array());
    }
    buffer.clear();
    return baos.toByteArray();
  }
}
