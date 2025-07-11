package com.example;

import java.util.Map;
import java.util.concurrent.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class MyProducer {
  public static void main(String[] args) throws Exception {
    final Producer<String, String> producer =
        new KafkaProducer<>(Map.of(BOOTSTRAP_SERVERS, System.getenv(BOOTSTRAP_SERVERS)),
            new StringSerializer(), new StringSerializer());

    // producer.send(new ProducerRecord<>(TOPIC,"DUMMY",null)).get(20, TimeUnit.SECONDS); // Force
    // topic creation

    Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(
        ()
            -> producer.send(
                new ProducerRecord<>(TOPIC, "key-" + ThreadLocalRandom.current().nextInt(10),
                    "val-" + ThreadLocalRandom.current().nextInt())),
        0, 100, TimeUnit.MILLISECONDS);
  }

  static final String BOOTSTRAP_SERVERS = "bootstrap.servers";
  static final String TOPIC = System.getenv("TOPIC");
}
