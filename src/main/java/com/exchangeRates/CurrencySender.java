package com.exchangeRates;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CurrencySender {
    private KafkaTemplate<String, String> kafkaTemplate;

    public CurrencySender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

    }

    public void sendCurrencyData(List<Currency> currencyList) {
        for (int i = 0; i < currencyList.size(); i++) {
            kafkaTemplate.send("exchangeRates", currencyList.get(i).toString());
        }
        kafkaTemplate.send("exchangeRates", "EndOfTransmission");
    }
}
