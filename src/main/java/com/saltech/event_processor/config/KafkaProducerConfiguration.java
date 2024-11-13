package com.saltech.event_processor.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.record.CompressionType;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableKafka
@Slf4j
public class KafkaProducerConfiguration {

    @Autowired
    private Environment environment;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                environment.getProperty("spring.cloud.stream.kafka.binder.brokers"));
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 10000);
        configProps.put(ProducerConfig.RETRIES_CONFIG, 100);
        configProps.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        configProps.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.GZIP.name);
        configProps.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaProducer kafkaProducer() {
        Map<String, Object> configProp2s = new HashMap<>();
        configProp2s.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                environment.getProperty("spring.cloud.stream.kafka.binder.brokers"));
        configProp2s.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProp2s.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProp2s.put(ProducerConfig.ACKS_CONFIG, "all");
        configProp2s.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 10000);
        configProp2s.put(ProducerConfig.RETRIES_CONFIG, 100);
        configProp2s.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        configProp2s.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        configProp2s.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, CompressionType.GZIP.name);
        configProp2s.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        configProp2s.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        return new KafkaProducer(configProp2s);
    }


    @Bean("kafkaTemplate")
    @ConditionalOnMissingBean(KafkaTemplate.class)
    public KafkaTemplate<String, String> kafkaTemplate() {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<>(producerFactory());

        kafkaTemplate.setProducerListener(new ProducerListener<String, String>() {
            @Override
            public void onSuccess(
                    ProducerRecord<String, String> producerRecord,
                    RecordMetadata recordMetadata) {

                log.info("ACK from ProducerListener message: {} offset:  {}",
                        producerRecord.value(),
                        recordMetadata.offset());
            }
        });
        return kafkaTemplate;
    }
}
