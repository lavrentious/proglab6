package ru.lavrent.lab6.common.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public abstract class AutoToString {
  private static int indent = 0;
  private final static String indentChar = "  ";

  private static String objToString(Object obj) {
    if (obj instanceof String)
      return "\"" + obj + "\"";
    return obj == null ? "null" : obj.toString();
  }

  public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
    fields.addAll(Arrays.asList(type.getDeclaredFields()));
    if (type.getSuperclass() != null) {
      getAllFields(fields, type.getSuperclass());
    }
    return fields;
  }

  private void fieldToString(Field field, StringBuilder sb) {
    try {
      boolean accessible = field.canAccess(this);
      field.setAccessible(true);
      sb.append(indentChar.repeat(indent) + field.getName() + " = " + AutoToString.objToString(field.get(this)));
      field.setAccessible(accessible);
      sb.append("\n");
    } catch (IllegalAccessException e) {
      sb.append("?\n");
    } catch (IllegalArgumentException e) {
    }
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getSimpleName() + " {\n");
    AutoToString.indent++;
    for (Field field : getAllFields(new LinkedList<Field>(), this.getClass())) {
      fieldToString(field, sb);
    }
    AutoToString.indent--;
    sb.append(indentChar.repeat(indent) + "}");
    return sb.toString();
  }
}
