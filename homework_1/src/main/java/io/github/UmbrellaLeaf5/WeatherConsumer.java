package io.github.UmbrellaLeaf5;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.UmbrellaLeaf5.config.AppConfig;
import io.github.UmbrellaLeaf5.model.WeatherData;
import io.github.UmbrellaLeaf5.utils.FileLogger;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * Consumer для обработки данных о погоде из Kafka.
 *
 * Собирает статистику по городам: количество солнечных/дождливых/облачных дней,
 * диапазон температур и время последнего обновления.
 */
public class WeatherConsumer {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private static AppConfig config = null;

  private static String outputFile; // файл для вывода отчетов
  private static String errorFile; // файл для вывода ошибок

  /**
   * Внутренний класс для хранения статистики по городу.
   */
  private static class CityStatistics {
    int sunnyDays = 0; // количество солнечных дней
    int rainyDays = 0; // количество дождливых дней
    int cloudyDays = 0; // количество облачных дней

    int maxTemp = Integer.MIN_VALUE; // максимальная температура
    int minTemp = Integer.MAX_VALUE; // минимальная температура

    String lastUpdate = ""; // дата последнего обновления
  }

  /**
   * Основной метод Consumer'а.
   *
   * Подключается к Kafka, получает сообщения и формирует отчеты.
   * @param args (String[]): аргументы командной строки.
   */
  public static void main(String[] args) {
    try {
      Map<String, Object> consumerConfig = new HashMap<>();

      // -------------------------- config --------------------------

      config = AppConfig.load("/app/config.json");

      consumerConfig.put("bootstrap.servers", config.getKafka().get("bootstrapServers"));
      consumerConfig.put("group.id", config.getKafka().get("groupId"));
      consumerConfig.put("auto.offset.reset", config.getKafka().get("autoOffsetReset"));

      String topic = (String) config.getKafka().get("topic");
      int reportInterval = (int) config.getTiming().get("consumerReportIntervalSeconds");
      int pollTimeout = (int) config.getTiming().get("consumerPollTimeoutMs");

      outputFile = (String) config.getFiles().get("consumerOutputFilePath");
      errorFile = (String) config.getFiles().get("consumerExceptionFilePath");

      // ------------------------------------------------------------

      consumerConfig.put("key.deserializer", StringDeserializer.class.getName());
      consumerConfig.put("value.deserializer", StringDeserializer.class.getName());

      Map<String, CityStatistics> stats = new HashMap<>();
      Instant lastReportTime = Instant.now();

      try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerConfig)) {
        consumer.subscribe(Collections.singletonList(topic));
        FileLogger.printToFile("Subscribed to topic: " + topic, outputFile);

        for (;;) {
          // получение сообщений из Kafka
          ConsumerRecords<String, String> records = consumer.poll(pollTimeout);

          // обработка каждого сообщения
          for (ConsumerRecord<String, String> record : records) processWeeklyWeather(record, stats);

          // формирование отчета по истечении интервала
          if (Duration.between(lastReportTime, Instant.now()).getSeconds() >= reportInterval) {
            printWeatherReport(stats);
            lastReportTime = Instant.now();

            stats.clear();
          }
        }
      }

    } catch (Exception e) {
      FileLogger.printToFile("Consumer fatal error: " + e.getMessage(), errorFile);
      e.printStackTrace();
    }
  }

  /**
   * Обрабатывает данные о погоде за неделю для одного города.
   *
   * @param record (ConsumerRecord<String, String>): запись из Kafka, содержащая ключ (город) и
   *     значение (JSON с данными о погоде).
   * @param stats (Map<String, CityStatistics>): коллекция для накопления статистики по городам, где
   *     ключ - название города, значение - соответствующая статистика.
   */
  private static void processWeeklyWeather(
      ConsumerRecord<String, String> record, Map<String, CityStatistics> stats) {
    try {
      WeatherData weather = objectMapper.readValue(record.value(), WeatherData.class);
      CityStatistics cityStats =
          stats.computeIfAbsent(weather.getCity(), k -> new CityStatistics());

      // анализ ежедневных данных
      for (WeatherData.DailyWeather daily : weather.getDailyData()) {
        if ("sunny".equals(daily.getCondition()))
          cityStats.sunnyDays++;

        else if ("rainy".equals(daily.getCondition()))
          cityStats.rainyDays++;

        else if ("cloudy".equals(daily.getCondition()))
          cityStats.cloudyDays++;

        cityStats.maxTemp = Math.max(cityStats.maxTemp, daily.getTemperature());
        cityStats.minTemp = Math.min(cityStats.minTemp, daily.getTemperature());

        cityStats.lastUpdate = daily.getDate();
      }

    } catch (Exception e) {
      FileLogger.printToFile(
          "Failed to process record [" + record.key() + "]: " + e.getMessage(), errorFile);
    }
  }

  /**
   * Формирует и выводит отчет о погоде по всем городам.
   * Добавляет специальные сообщения для Тюмени и Санкт-Петербурга.
   *
   * @param stats (Map<String, CityStatistics>): коллекция со статистикой по городам, где ключ -
   *     название города, значение - соответствующая статистика.
   */
  private static void printWeatherReport(Map<String, CityStatistics> stats) {
    FileLogger.printToFile("\n------------- WEEKLY WEATHER REPORT -------------", outputFile);
    FileLogger.printToFile("--- Generated at: " + Instant.now() + " ---", outputFile);

    if (stats.isEmpty()) {
      FileLogger.printToFile("\n------------- EMPTY WEATHER REPORT -------=-------", outputFile);
      return;
    }

    FileLogger.printToFile("-------------------------------------------------", outputFile);

    // вывод статистики по каждому городу
    stats.forEach((city, data) -> {
      FileLogger.printToFile("City: " + city, outputFile);
      FileLogger.printToFile("• Sunny days: " + data.sunnyDays, outputFile);
      FileLogger.printToFile("• Rainy days: " + data.rainyDays, outputFile);
      FileLogger.printToFile("• Cloudy days: " + data.cloudyDays, outputFile);

      FileLogger.printToFile(
          "• Temp. range: " + data.minTemp + "°C : " + data.maxTemp + "°C", outputFile);
      FileLogger.printToFile("• Last update: " + data.lastUpdate + "\n", outputFile);
    });

    // специальные сообщения:

    if (stats.containsKey("Tyumen") && stats.get("Tyumen").rainyDays >= 2)
      FileLogger.printToFile(">> Mushroom picking time in Tyumen! <<", outputFile);

    if (stats.containsKey("Saint Petersburg")) {
      CityStatistics spb = stats.get("Saint Petersburg");

      if (spb.rainyDays > 3)
        FileLogger.printToFile(">> Typical rainy weather in St. Petersburg! <<", outputFile);

      if (spb.sunnyDays > 2)
        FileLogger.printToFile(">> Untypical sunny weather in St. Petersburg! <<", outputFile);
    }
  }
}
