package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherData {
  @JsonProperty("city") private String city;

  @JsonProperty("condition") private String condition;

  @JsonProperty("temperature") private int temperature;

  @JsonProperty("timestamp") private String timestamp;

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

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

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
}
