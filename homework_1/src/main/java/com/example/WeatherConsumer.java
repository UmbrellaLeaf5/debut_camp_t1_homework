package com.example;

import com.example.model.WeatherData;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.*;
import java.util.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.*;

public class WeatherConsumer {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private static class CityStatistics {
    int sunnyDays = 0;
    int rainyDays = 0;
    int maxTemp = Integer.MIN_VALUE;
    int minTemp = Integer.MAX_VALUE;
    String lastUpdate = "";
  }

  public static void main(String[] args) {
    Map<String, Object> config = new HashMap<>();
    config.put(
        "bootstrap.servers", System.getenv().getOrDefault("BOOTSTRAP_SERVERS", "kafka:9092"));
    config.put("group.id", System.getenv().getOrDefault("GROUP_ID", "weather-group"));
    config.put("auto.offset.reset", "earliest");
    config.put("key.deserializer", StringDeserializer.class.getName());
    config.put("value.deserializer", StringDeserializer.class.getName());

    Map<String, CityStatistics> stats = new HashMap<>();
    Instant lastReport = Instant.now();
    String topic = System.getenv().getOrDefault("TOPIC", "weather-data");

    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(config)) {
      consumer.subscribe(Collections.singletonList(topic));
      System.out.println("Subscribed to topic: " + topic);

      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(1000);

        for (ConsumerRecord<String, String> record : records) processWeatherRecord(record, stats);

        if (Duration.between(lastReport, Instant.now()).getSeconds() >= 30) {
          printWeatherReport(stats);
          lastReport = Instant.now();
          stats.clear();
        }
      }

    } catch (Exception e) {
      System.err.println("Consumer fatal error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static void processWeatherRecord(
      ConsumerRecord<String, String> record, Map<String, CityStatistics> stats) {
    try {
      WeatherData weather = objectMapper.readValue(record.value(), WeatherData.class);
      CityStatistics cityStats =
          stats.computeIfAbsent(weather.getCity(), k -> new CityStatistics());

      if ("sunny".equals(weather.getCondition()))
        cityStats.sunnyDays++;

      else if ("rainy".equals(weather.getCondition()))
        cityStats.rainyDays++;

      cityStats.maxTemp = Math.max(cityStats.maxTemp, weather.getTemperature());
      cityStats.minTemp = Math.min(cityStats.minTemp, weather.getTemperature());
      cityStats.lastUpdate = weather.getTimestamp();

    } catch (Exception e) {
      System.err.println("Failed to process record [" + record.key() + "]: " + e.getMessage());
    }
  }

  private static void printWeatherReport(Map<String, CityStatistics> stats) {
    System.out.println("\n=== WEATHER ANALYTICS REPORT ===");
    System.out.println("Generated at: " + Instant.now());
    System.out.println("==============================");

    stats.forEach((city, data) -> {
      System.out.println("City: " + city);
      System.out.println("- Sunny days: " + data.sunnyDays);
      System.out.println("- Rainy days: " + data.rainyDays);
      System.out.println("- Temperature range: " + data.minTemp + "°C to " + data.maxTemp + "°C");
      System.out.println("- Last update: " + data.lastUpdate + "\n");
    });

    if (stats.containsKey("Tyumen") && stats.get("Tyumen").rainyDays >= 2)
      System.out.println(">> Mushroom picking season in Tyumen! <<");

    if (stats.containsKey("Saint Petersburg") && stats.get("Saint Petersburg").rainyDays > 3)
      System.out.println(">> Typical rainy St. Petersburg weather <<");
  }
}
