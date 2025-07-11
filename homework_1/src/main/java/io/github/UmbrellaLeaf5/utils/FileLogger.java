package io.github.UmbrellaLeaf5.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Утилита для логирования сообщений в файл и консоль.
 */
public class FileLogger {
  /**
   * Записывает сообщение в файл и выводит в консоль.
   *
   * @param message (String): сообщение для записи.
   * @param filePath (String): путь к файлу для записи логов.
   */
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

  /**
   * Форматирует сообщение и записывает в файл (аналогично String.format).
   *
   * @param format (String): строка формата.
   * @param filePath (String): путь к файлу для записи логов.
   * @param args (Object...): аргументы для форматирования строки.
   */
  public static void printfToFile(String format, String filePath, Object... args) {
    String message = String.format(format, args);
    printToFile(message, filePath);
  }
}
