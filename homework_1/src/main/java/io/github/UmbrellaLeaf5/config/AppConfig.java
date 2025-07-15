package io.github.UmbrellaLeaf5.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import lombok.Getter;

/**
 * Класс для работы с конфигурацией приложения.
 */
@Getter
public class AppConfig {
  private Map<String, Object> kafka;
  private Map<String, Object> weather;
  private Map<String, Object> timing;
  private Map<String, Object> files;

  /**
   * Загружает конфигурацию из JSON-файла.
   *
   * @param configPath (String): путь к конфигурационному файлу.
   * @return AppConfig: объект конфигурации.
   * @throws IOException если возникла ошибка чтения файла.
   */
  public static AppConfig load(String configPath) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(new File(configPath), AppConfig.class);
  }
}
