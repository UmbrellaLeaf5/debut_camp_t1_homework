package io.github.UmbrellaLeaf5.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Класс для работы с конфигурацией приложения.
 */
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

  /**
   * @return Map<String, Object>: настройки Kafka (брокеры, топики, параметры producer/consumer)
   */
  public Map<String, Object> getKafka() {
    return kafka;
  }

  /**
   * @return Map<String, Object>: настройки генерации погодных данных (города, условия, диапазон
   *     температур)
   */
  public Map<String, Object> getWeather() {
    return weather;
  }

  /**
   * @return Map<String, Object>: временные интервалы для работы producer/consumer
   */
  public Map<String, Object> getTiming() {
    return timing;
  }

  /**
   * @return Map<String, Object>: пути к файлам для логирования
   */
  public Map<String, Object> getFiles() {
    return files;
  }
}
