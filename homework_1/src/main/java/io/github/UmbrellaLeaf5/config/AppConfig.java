package io.github.UmbrellaLeaf5.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class AppConfig {
  private Map<String, Object> kafka;
  private Map<String, Object> weather;
  private Map<String, Object> timing;
  private Map<String, Object> files;

  public static AppConfig load(String configPath) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(new File(configPath), AppConfig.class);
  }

  public Map<String, Object> getKafka() {
    return kafka;
  }

  public Map<String, Object> getWeather() {
    return weather;
  }

  public Map<String, Object> getTiming() {
    return timing;
  }

  public Map<String, Object> getFiles() {
    return files;
  }
}
