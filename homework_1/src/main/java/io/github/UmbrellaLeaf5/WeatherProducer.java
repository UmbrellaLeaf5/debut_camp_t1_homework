package io.github.UmbrellaLeaf5;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.UmbrellaLeaf5.config.AppConfig;
import io.github.UmbrellaLeaf5.model.WeatherData;
import io.github.UmbrellaLeaf5.utils.FileLogger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class WeatherProducer {
  private static final ObjectMapper objectMapper = new ObjectMapper();
  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

  private static AppConfig config = null;
  private static String outputFile;
  private static String errorFile;

  public static void main(String[] args) {
    try {
      Map<String, Object> producerConfig = new HashMap<>();

      // -------------------------- config --------------------------

      config = AppConfig.load("/app/config.json");

      producerConfig.put("bootstrap.servers", config.getKafka().get("bootstrapServers"));

      producerConfig.put("retries", config.getKafka().get("producerRetries"));
      producerConfig.put("linger.ms", config.getKafka().get("producerLingerMs"));

      String topic = (String) config.getKafka().get("topic");

      int delay = (int) config.getTiming().get("producerDelaySeconds");
      int sleepDelay = (int) config.getTiming().get("producerThreadTimeoutMs");

      outputFile = (String) config.getFiles().get("producerOutputFilePath");
      errorFile = (String) config.getFiles().get("producerExceptionFilePath");

      // ------------------------------------------------------------

      producerConfig.put("acks", "all");
      producerConfig.put("key.serializer", StringSerializer.class.getName());
      producerConfig.put("value.serializer", StringSerializer.class.getName());

      try (Producer<String, String> producer = new KafkaProducer<>(producerConfig)) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
          WeatherData weather = generateWeeklyWeather();
          try {
            String json = objectMapper.writeValueAsString(weather);
            ProducerRecord<String, String> record =
                new ProducerRecord<>(topic, weather.getCity(), json);

            producer.send(record, (metadata, e) -> {
              if (e != null) {
                FileLogger.printfToFile(
                    "Failed to send message to %s: %s%n", errorFile, topic, e.getMessage());
              } else {
                FileLogger.printfToFile("Sent weekly weather data for %s [partition %d]%n",
                    outputFile, weather.getCity(), metadata.partition());
              }
            });
          } catch (Exception e) {
            FileLogger.printToFile("JSON serialization error: " + e.getMessage(), errorFile);
          }
        }, 0, delay, TimeUnit.SECONDS);

        while (true) Thread.sleep(sleepDelay);
      }
    } catch (Exception e) {
      FileLogger.printToFile("Producer fatal error: " + e.getMessage(), errorFile);
      e.printStackTrace();
    }
  }

  @SuppressWarnings("unchecked")
  private static WeatherData generateWeeklyWeather() {
    // -------------------------- config --------------------------

    int daysToGenerate = (int) config.getWeather().get("daysToGenerate");

    List<String> cities = (List<String>) config.getWeather().get("cities");
    List<String> conditions = (List<String>) config.getWeather().get("conditions");

    Map<String, Integer> tempRange =
        (Map<String, Integer>) config.getWeather().get("temperatureRange");

    // ------------------------------------------------------------

    WeatherData weather = new WeatherData();
    List<WeatherData.DailyWeather> dailyData = new ArrayList<>();

    for (int i = 0; i < daysToGenerate; i++) {
      WeatherData.DailyWeather daily = new WeatherData.DailyWeather();
      daily.setCondition(conditions.get(ThreadLocalRandom.current().nextInt(conditions.size())));
      daily.setTemperature(
          ThreadLocalRandom.current().nextInt(tempRange.get("min"), tempRange.get("max") + 1));
      daily.setDate(LocalDateTime.now().minusDays(i).format(DATE_FORMAT));
      dailyData.add(daily);
    }

    weather.setCity(cities.get(ThreadLocalRandom.current().nextInt(cities.size())));
    weather.setDailyData(dailyData);
    return weather;
  }
}
