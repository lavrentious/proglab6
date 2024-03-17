package ru.lavrent.lab6.server.interfaces;

import ru.lavrent.lab6.server.exceptions.DeserializationException;
import ru.lavrent.lab6.server.exceptions.SerializationException;

import java.io.FileNotFoundException;

/**
 * interface for saving and loading data from file
 */
public interface IDumper {
  /**
   * write the data into a file
   * 
   * @throws FileNotFoundException  if the file isn't found
   * @throws SerializationException if something went wrong during serialization
   */
  public void dump() throws FileNotFoundException, SerializationException;

  /**
   * load the data from file
   * 
   * @throws FileNotFoundException    if the file isn't found
   * @throws DeserializationException if something went wrong during
   *                                  deserialization
   */
  public void load() throws FileNotFoundException, DeserializationException;
}
