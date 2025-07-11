package com.example;

import com.example.config.AppConfig;
import com.example.model.WeatherData;
import com.example.utils.FileLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.*;
import java.util.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.*;

public class WeatherConsumer {
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static AppConfig config = null;

  private static String outputFile;
  private static String errorFile;

  private static class CityStatistics {
    int sunnyDays = 0;
    int rainyDays = 0;
    int cloudyDays = 0;

    int maxTemp = Integer.MIN_VALUE;
    int minTemp = Integer.MAX_VALUE;
    String lastUpdate = "";
  }

  public static void main(String[] args) {
    try {
      // -------------------------- config --------------------------

      config = AppConfig.load("/app/config.json");

      Map<String, Object> consumerConfig = new HashMap<>();

      consumerConfig.put("bootstrap.servers", config.getKafka().get("bootstrapServers"));
      consumerConfig.put("group.id", config.getKafka().get("groupId"));
      consumerConfig.put("auto.offset.reset", config.getKafka().get("autoOffsetReset"));
      consumerConfig.put("key.deserializer", StringDeserializer.class.getName());
      consumerConfig.put("value.deserializer", StringDeserializer.class.getName());

      String topic = (String) config.getKafka().get("topic");
      int reportInterval = (int) config.getTiming().get("consumerReportIntervalSeconds");
      int pollTimeout = (int) config.getTiming().get("consumerPollTimeoutMs");

      outputFile = (String) config.getFiles().get("producerOutputFilePath");
      errorFile = (String) config.getFiles().get("producerExceptionFilePath");

      // ------------------------------------------------------------

      Map<String, CityStatistics> stats = new HashMap<>();
      Instant lastReport = Instant.now();

      try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfig)) {
        consumer.subscribe(Collections.singletonList(topic));
        FileLogger.printToFile("Subscribed to topic: " + topic, outputFile);

        while (true) {
          ConsumerRecords<String, String> records = consumer.poll(pollTimeout);

          for (ConsumerRecord<String, String> record : records) processWeatherRecord(record, stats);

          if (Duration.between(lastReport, Instant.now()).getSeconds() >= reportInterval) {
            printWeatherReport(stats);
            lastReport = Instant.now();
            stats.clear();
          }
        }
      }

    } catch (Exception e) {
      FileLogger.printToFile("Consumer fatal error: " + e.getMessage(), errorFile);

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

      else if ("cloudy".equals(weather.getCondition()))
        cityStats.cloudyDays++;

      cityStats.maxTemp = Math.max(cityStats.maxTemp, weather.getTemperature());
      cityStats.minTemp = Math.min(cityStats.minTemp, weather.getTemperature());
      cityStats.lastUpdate = weather.getTimestamp();

    } catch (Exception e) {
      FileLogger.printToFile(
          "Failed to process record [" + record.key() + "]: " + e.getMessage(), errorFile);
    }
  }

  private static void printWeatherReport(Map<String, CityStatistics> stats) {
    FileLogger.printToFile("\n----------- WEATHER ANALYTICS REPORT ------------", outputFile);
    FileLogger.printToFile("--- Generated at: " + Instant.now() + " ---", outputFile);
    FileLogger.printToFile("-------------------------------------------------", outputFile);

    stats.forEach((city, data) -> {
      FileLogger.printToFile("City: " + city, outputFile);
      FileLogger.printToFile("• Sunny days: " + data.sunnyDays, outputFile);
      FileLogger.printToFile("• Rainy days: " + data.rainyDays, outputFile);
      FileLogger.printToFile("• Cloudy days: " + data.cloudyDays, outputFile);

      FileLogger.printToFile(
          "• Temp. range: " + data.minTemp + "°C : " + data.maxTemp + "°C", outputFile);
      FileLogger.printToFile("• Last update: " + data.lastUpdate + "\n", outputFile);
    });

    if (stats.containsKey("Tyumen") && stats.get("Tyumen").rainyDays >= 2)
      FileLogger.printToFile(">> Mushroom picking season in Tyumen! <<", outputFile);

    if (stats.containsKey("Saint Petersburg") && stats.get("Saint Petersburg").rainyDays > 3)
      FileLogger.printToFile(">> Typical rainy St. Petersburg weather! <<", outputFile);

    if (stats.containsKey("Saint Petersburg") && stats.get("Saint Petersburg").sunnyDays > 2)
      FileLogger.printToFile(">> Untypical sunny St. Petersburg weather! <<", outputFile);
  }
}
