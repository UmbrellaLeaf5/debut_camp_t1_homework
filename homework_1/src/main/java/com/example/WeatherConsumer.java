package com.example;

import java.util.List;
import java.util.Map;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class WeatherConsumer {
  public static void main(String[] args) throws Exception {
    final KafkaConsumer<String, String> consumer =
        new KafkaConsumer<>(Map.of(BOOTSTRAP_SERVERS, System.getenv(BOOTSTRAP_SERVERS), GROUP_ID,
                                System.getenv(GROUP_ID), AUTO_OFFSET_RESET, "earliest"),
            new StringDeserializer(), new StringDeserializer());

    consumer.subscribe(List.of(TOPIC));
    while (true) {
      consumer.poll(1000).spliterator().forEachRemaining(System.out::println);
    }
  }

  static final String BOOTSTRAP_SERVERS = "bootstrap.servers";
  static final String TOPIC = System.getenv("TOPIC");
  static final String AUTO_OFFSET_RESET = "auto.offset.reset";
  static final String GROUP_ID = "group.id";
}
