package ru.lavrent.lab6.server.interfaces;

import ru.lavrent.lab6.server.exceptions.DeserializationException;
import ru.lavrent.lab6.server.exceptions.SerializationException;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * interface for saving and loading data from file
 */
public interface IDumper {
  /**
   * write the data into a file
   * 
   * @throws SerializationException if something went wrong during serialization
   */
  public void dump() throws IOException, SerializationException;

  /**
   * load the data from file
   * 
   * @throws FileNotFoundException    if the file isn't found
   * @throws DeserializationException if something went wrong during
   *                                  deserialization
   */
  public void load() throws FileNotFoundException, DeserializationException;
}
