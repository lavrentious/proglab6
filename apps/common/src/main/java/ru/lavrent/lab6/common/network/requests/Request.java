package ru.lavrent.lab6.common.network.requests;

import ru.lavrent.lab6.common.utils.AutoToString;

import java.io.Serializable;
import java.util.Objects;

public abstract class Request extends AutoToString implements Serializable {
  final String name;

  public Request(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Request response = (Request) o;
    return Objects.equals(name, response.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}