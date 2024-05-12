package com.example.notificationservice.config;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

//    @Value("${spring.kafka.bootstrap-servers}")
//    private String bootstrapServers;
//
//    @Bean
//    public ConsumerFactory<String, NotificationMessage> consumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-group");
//        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(),
//                new JsonDeserializer<>(NotificationMessage.class));
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, NotificationMessage> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, NotificationMessage> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
}

