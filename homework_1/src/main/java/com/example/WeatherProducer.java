package com.example;

import com.example.config.AppConfig;
import com.example.model.WeatherData;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import org.apache.kafka.clients.producer.*;

public class WeatherProducer {
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

  private static AppConfig config = null;

  public static void main(String[] args) {
    try {
      // -------------------------- config --------------------------

      config = AppConfig.load("config.json");

      Map<String, Object> producerConfig = new HashMap<>();
      producerConfig.put("bootstrap.servers", config.getKafka().get("bootstrapServers"));

      String topic = (String) config.getKafka().get("topic");
      int delay = (int) config.getTiming().get("producerDelaySeconds");
      int threadTimeout = (int) config.getTiming().get("producerThreadTimeoutMs");

      // ------------------------------------------------------------

      producerConfig.put(
          "key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
      producerConfig.put(
          "value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
      producerConfig.put("acks", "all");
      producerConfig.put("retries", 3);
      producerConfig.put("linger.ms", 1);

      try (Producer<String, String> producer = new KafkaProducer<>(producerConfig)) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
          WeatherData weather = generateRandomWeather();

          try {
            String json = objectMapper.writeValueAsString(weather);
            ProducerRecord<String, String> record =
                new ProducerRecord<>(topic, weather.getCity(), json);

            producer.send(record, (metadata, e) -> {
              if (e != null)
                System.err.printf("Failed to send message to %s: %s%n", topic, e.getMessage());

              else
                System.out.printf("Sent weather data for %s [partition %d]%n", weather.getCity(),
                    metadata.partition());
            });

          } catch (Exception e) {
            System.err.println("JSON serialization error: " + e.getMessage());
          }
        }, 0, delay, TimeUnit.SECONDS);

        while (true) Thread.sleep(threadTimeout);

      } catch (Exception e) {
        System.err.println("Producer fatal error: " + e.getMessage());
        e.printStackTrace();
      }

    } catch (Exception e) {
      System.err.println("Configuration error: " + e.getMessage());
    }
  }

  private static WeatherData generateRandomWeather() {
    if (config == null)
      return null;

    WeatherData weather = new WeatherData();

    @SuppressWarnings("unchecked")
    List<String> cities = (List<String>) config.getWeather().get("cities");

    @SuppressWarnings("unchecked")
    List<String> conditions = (List<String>) config.getWeather().get("conditions");

    @SuppressWarnings("unchecked")
    Map<String, Integer> tempRange =
        (Map<String, Integer>) config.getWeather().get("temperatureRange");

    weather.setCity(cities.get(ThreadLocalRandom.current().nextInt(cities.size())));
    weather.setCondition(conditions.get(ThreadLocalRandom.current().nextInt(conditions.size())));
    weather.setTemperature(
        ThreadLocalRandom.current().nextInt(tempRange.get("min"), tempRange.get("max") + 1));
    weather.setTimestamp(LocalDateTime.now().format(DATE_FORMAT));

    return weather;
  }
}
