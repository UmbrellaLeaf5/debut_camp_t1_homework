package com.example;

import com.example.model.WeatherData;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.*;
import java.util.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.*;

public class WeatherConsumer {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private static class CityStats {
    int sunnyDays = 0;
    int rainyDays = 0;
    int maxTemp = Integer.MIN_VALUE;
    int minTemp = Integer.MAX_VALUE;
    String lastDate = "";
  }

  public static void main(String[] args) {
    Map<String, Object> config = new HashMap<>();

    config.put("bootstrap.servers", System.getenv("BOOTSTRAP_SERVERS"));
    config.put("group.id", System.getenv("GROUP_ID"));
    config.put("auto.offset.reset", "earliest");
    config.put("key.deserializer", StringDeserializer.class.getName());
    config.put("value.deserializer", StringDeserializer.class.getName());

    Map<String, CityStats> statistics = new HashMap<>();
    Instant lastReportTime = Instant.now();

    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(config)) {
      consumer.subscribe(Collections.singletonList(System.getenv("TOPIC")));

      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(1000);

        for (ConsumerRecord<String, String> record : records) try {
            WeatherData weather = objectMapper.readValue(record.value(), WeatherData.class);
            updateStatistics(weather, statistics);
          } catch (Exception e) {
            System.err.println("JSON parsing Error: " + e.getMessage());
          }

        // каждые 30 секунд выводим статистику
        if (Duration.between(lastReportTime, Instant.now()).getSeconds() >= 30) {
          printStatistics(statistics);
          lastReportTime = Instant.now();
          statistics.clear(); // сбрасываем статистику
        }
      }

    } catch (Exception e) {
      System.err.println("Exception (consumer): " + e.getMessage());
    }
  }

  private static void updateStatistics(WeatherData weather, Map<String, CityStats> statistics) {
    CityStats stats = statistics.computeIfAbsent(weather.getCity(), k -> new CityStats());

    if ("sunny".equals(weather.getCondition()))
      stats.sunnyDays++;

    if ("rainy".equals(weather.getCondition()))
      stats.rainyDays++;

    stats.maxTemp = Math.max(stats.maxTemp, weather.getTemperature());
    stats.minTemp = Math.min(stats.minTemp, weather.getTemperature());
    stats.lastDate = weather.getTimestamp();
  }

  private static void printStatistics(Map<String, CityStats> statistics) {
    System.out.print("\n=== Weather analytics for the period ===\n");

    statistics.forEach((city, stats) -> {
      System.out.printf("City: %s\n"
              + "• Sunny days: %d\n"
              + "• Rainy days: %d\n"
              + "• Max. temperature: %d°C\n"
              + "• Min. temperature: %d°C\n"
              + "• Last update: %s\n\n",
          city, stats.sunnyDays, stats.rainyDays, stats.maxTemp, stats.minTemp, stats.lastDate);
    });

    // дополнительная аналитика
    System.out.println("=== Recommendations ===");
    statistics.forEach((city, stats) -> {
      if (city.equals("Tyumen") && stats.rainyDays >= 2)
        System.out.println("It rained in Tyumen - now it's time to go mushroom picking!");

      if (city.equals("Saint Petersburg") && stats.rainyDays > stats.sunnyDays)
        System.out.println("In St. Petersburg there is more rain than sunny days - typical!");
    });
  }
}
