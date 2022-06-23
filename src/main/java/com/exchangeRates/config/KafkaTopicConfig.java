package com.exchangeRates.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic exchangeRates() {
        return TopicBuilder.name("exchangeRates").build();
    }

    @Bean
    public NewTopic exchangeRatesRequests() {
        return TopicBuilder.name("exchangeRatesRequests").build();
    }


}

