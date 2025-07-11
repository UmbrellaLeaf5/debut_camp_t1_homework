package com.example.utils;

import java.io.*;
import java.nio.file.*;

public class FileLogger {
  public static void printToFile(String message, String filePath) {
    System.out.println(message);

    try {
      Files.createDirectories(Paths.get(filePath).getParent());
      Files.write(Paths.get(filePath), (message + System.lineSeparator()).getBytes(),
          StandardOpenOption.CREATE, StandardOpenOption.APPEND);

    } catch (IOException e) {
      System.err.println("Failed to write logs to " + filePath + ": " + e.getMessage());
    }
  }

  public static void printfToFile(String format, String filePath, Object... args) {
    String message = String.format(format, args);
    printToFile(message, filePath);
  }
}
