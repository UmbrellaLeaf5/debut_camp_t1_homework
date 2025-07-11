package io.github.UmbrellaLeaf5;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.UmbrellaLeaf5.config.AppConfig;
import io.github.UmbrellaLeaf5.model.WeatherData;
import io.github.UmbrellaLeaf5.utils.FileLogger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.producer.*;


public class WeatherProducer {
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

  private static AppConfig config = null;

  @SuppressWarnings("unused") private static String outputFile;
  private static String errorFile;

  public static void main(String[] args) {
    try {
      // -------------------------- config --------------------------

      config = AppConfig.load("/app/config.json");

      Map<String, Object> producerConfig = new HashMap<>();
      producerConfig.put("bootstrap.servers", config.getKafka().get("bootstrapServers"));

      String topic = (String) config.getKafka().get("topic");
      int delay = (int) config.getTiming().get("producerDelaySeconds");
      int threadTimeout = (int) config.getTiming().get("producerThreadTimeoutMs");

      int retries = (int) config.getKafka().get("producerRetries");
      int linger = (int) config.getKafka().get("producerLingerMs");

      outputFile = (String) config.getFiles().get("producerOutputFilePath");
      errorFile = (String) config.getFiles().get("producerExceptionFilePath");

      // ------------------------------------------------------------

      producerConfig.put(
          "key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
      producerConfig.put(
          "value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
      producerConfig.put("acks", "all");
      producerConfig.put("retries", retries);
      producerConfig.put("linger.ms", linger);

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
                FileLogger.printfToFile(
                    "Failed to send message to %s: %s%n", errorFile, topic, e.getMessage());

              else
                FileLogger.printfToFile("Sent weather data for %s [partition %d]%n", outputFile,
                    weather.getCity(), metadata.partition());
            });

          } catch (Exception e) {
            FileLogger.printToFile("JSON serialization error: " + e.getMessage(), errorFile);
          }
        }, 0, delay, TimeUnit.SECONDS);

        while (true) Thread.sleep(threadTimeout);

      } catch (Exception e) {
        FileLogger.printToFile("Producer fatal error: " + e.getMessage(), errorFile);

        e.printStackTrace();
      }

    } catch (Exception e) {
      FileLogger.printToFile("Configuration error: " + e.getMessage(), errorFile);
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
