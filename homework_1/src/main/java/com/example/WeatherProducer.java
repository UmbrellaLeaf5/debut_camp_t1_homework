package com.example;

import com.example.model.WeatherData;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import org.apache.kafka.clients.producer.*;

public class WeatherProducer {
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final String[] CITIES = {
      "Magadan", "Chukotka", "Saint Petersburg", "Tyumen", "Moscow"};
  private static final String[] CONDITIONS = {"sunny", "cloudy", "rainy"};
  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

  public static void main(String[] args) {
    Map<String, Object> config = new HashMap<>();

    config.put(
        "bootstrap.servers", System.getenv().getOrDefault("BOOTSTRAP_SERVERS", "kafka:9092"));
    config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    config.put("acks", "all");
    config.put("retries", 3);
    config.put("linger.ms", 1);

    try (Producer<String, String> producer = new KafkaProducer<>(config)) {
      ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

      String topic = System.getenv().getOrDefault("TOPIC", "weather-data");

      executor.scheduleAtFixedRate(() -> {
        WeatherData weather = generateRandomWeather();

        try {
          String json = objectMapper.writeValueAsString(weather);
          ProducerRecord<String, String> record =
              new ProducerRecord<>(topic, weather.getCity(), json);

          producer.send(record, (metadata, e) -> {
            if (e != null) {
              System.err.printf("Failed to send message to %s: %s%n", topic, e.getMessage());
            } else {
              System.out.printf("Sent weather data for %s [partition %d]%n", weather.getCity(),
                  metadata.partition());
            }
          });
        } catch (Exception e) {
          System.err.println("JSON serialization error: " + e.getMessage());
        }
      }, 0, 5, TimeUnit.SECONDS);

      while (true) Thread.sleep(1000);

    } catch (Exception e) {
      System.err.println("Producer fatal error: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static WeatherData generateRandomWeather() {
    WeatherData weather = new WeatherData();

    weather.setCity(CITIES[ThreadLocalRandom.current().nextInt(CITIES.length)]);
    weather.setCondition(CONDITIONS[ThreadLocalRandom.current().nextInt(CONDITIONS.length)]);
    weather.setTemperature(ThreadLocalRandom.current().nextInt(-10, 35));
    weather.setTimestamp(LocalDateTime.now().format(DATE_FORMAT));

    return weather;
  }
}
