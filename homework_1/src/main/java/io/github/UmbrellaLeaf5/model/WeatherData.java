package io.github.UmbrellaLeaf5.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * Модель данных о погоде, содержащая информацию о городе и ежедневных прогнозах.
 */
@Data
public class WeatherData {
  @JsonProperty("city") private String city;
  @JsonProperty("dailyData") private List<DailyWeather> dailyData;

  /**
   * Модель ежедневных данных о погоде.
   */
  @Data
  public static class DailyWeather {
    @JsonProperty("condition") private String condition;
    @JsonProperty("temperature") private int temperature;
    @JsonProperty("date") private String date;
  }
}
