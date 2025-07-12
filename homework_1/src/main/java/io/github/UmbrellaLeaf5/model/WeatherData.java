package io.github.UmbrellaLeaf5.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Модель данных о погоде, содержащая информацию о городе и ежедневных прогнозах.
 */
public class WeatherData {
  @JsonProperty("city") private String city;
  @JsonProperty("dailyData") private List<DailyWeather> dailyData;

  /**
   * Модель ежедневных данных о погоде.
   */
  public static class DailyWeather {
    @JsonProperty("condition") private String condition;
    @JsonProperty("temperature") private int temperature;
    @JsonProperty("date") private String date;

    /**
     * @return String: текстовое описание погодных условий.
     */
    public String getCondition() {
      return condition;
    }

    public void setCondition(String condition) {
      this.condition = condition;
    }

    /**
     * @return int: температура в градусах Цельсия.
     */
    public int getTemperature() {
      return temperature;
    }

    public void setTemperature(int temperature) {
      this.temperature = temperature;
    }

    /**
     * @return String: дата в формате строки.
     */
    public String getDate() {
      return date;
    }

    public void setDate(String date) {
      this.date = date;
    }
  }

  /**
   * @return String: название города.
   */
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  /**
   * @return List<DailyWeather>: список ежедневных прогнозов погоды.
   */
  public List<DailyWeather> getDailyData() {
    return dailyData;
  }

  public void setDailyData(List<DailyWeather> dailyData) {
    this.dailyData = dailyData;
  }
}
