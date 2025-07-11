package io.github.UmbrellaLeaf5.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileLogger {
  public static void printToFile(String message, String filePath) {
    System.out.println(message);

    Path path = Paths.get(filePath);

    try {
      if (!Files.exists(path))
        Files.createFile(path);

    } catch (IOException e) {
      System.err.println("Failed to write logs to " + filePath + ": " + e.getMessage());
    }

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
      writer.write(message);
      writer.newLine();

    } catch (IOException e) {
      System.err.println("Failed to write logs to " + filePath + ": " + e.getMessage());
    }
  }

  public static void printfToFile(String format, String filePath, Object... args) {
    String message = String.format(format, args);
    printToFile(message, filePath);
  }
}
