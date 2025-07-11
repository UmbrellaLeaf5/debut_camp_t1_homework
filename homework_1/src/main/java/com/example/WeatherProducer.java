package com.example;

import com.example.model.WeatherData;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.*;
import org.apache.kafka.clients.producer.*;

public class WeatherProducer {
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final String[] CITIES = {
      "Magadan", "Chukotka", "Saint Petersburg", "Tyumen", "Moscow"};
  private static final String[] CONDITIONS = {"sunny", "cloudy", "rainy"};
  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

  public static void main(String[] args) {
    Map<String, Object> config = Map.of("bootstrap.servers", System.getenv("BOOTSTRAP_SERVERS"),
        "key.serializer", "org.apache.kafka.common.serialization.StringSerializer",
        "value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    try (Producer<String, String> producer = new KafkaProducer<>(config)) {
      ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

      executor.scheduleAtFixedRate(() -> {
        WeatherData weather = new WeatherData();

        weather.setCity(CITIES[ThreadLocalRandom.current().nextInt(CITIES.length)]);
        weather.setCondition(CONDITIONS[ThreadLocalRandom.current().nextInt(CONDITIONS.length)]);
        weather.setTemperature(ThreadLocalRandom.current().nextInt(-10, 35));
        weather.setTimestamp(LocalDateTime.now().format(DATE_FORMAT));

        try {
          String json = objectMapper.writeValueAsString(weather);
          ProducerRecord<String, String> record =
              new ProducerRecord<>(System.getenv("TOPIC"), weather.getCity(), json);

          producer.send(record);
          System.out.println("Send data: " + json);

        } catch (Exception e) {
          System.err.println("JSON creating Error: " + e.getMessage());
        }
      }, 0, 5, TimeUnit.SECONDS); // отправка каждые 5 секунд

      // бесконечный цикл для поддержания работы
      while (true) Thread.sleep(1000);

    } catch (Exception e) {
      System.err.println("Exception (producer): " + e.getMessage());
    }
  }
}
