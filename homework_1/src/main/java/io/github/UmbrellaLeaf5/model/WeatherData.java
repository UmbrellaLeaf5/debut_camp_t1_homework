package io.github.UmbrellaLeaf5.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class WeatherData {
  @JsonProperty("city") private String city;
  @JsonProperty("dailyData") private List<DailyWeather> dailyData;

  public static class DailyWeather {
    @JsonProperty("condition") private String condition;
    @JsonProperty("temperature") private int temperature;
    @JsonProperty("date") private String date;

    public String getCondition() {
      return condition;
    }

    public void setCondition(String condition) {
      this.condition = condition;
    }

    public int getTemperature() {
      return temperature;
    }

    public void setTemperature(int temperature) {
      this.temperature = temperature;
    }

    public String getDate() {
      return date;
    }

    public void setDate(String date) {
      this.date = date;
    }
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public List<DailyWeather> getDailyData() {
    return dailyData;
  }

  public void setDailyData(List<DailyWeather> dailyData) {
    this.dailyData = dailyData;
  }
}
