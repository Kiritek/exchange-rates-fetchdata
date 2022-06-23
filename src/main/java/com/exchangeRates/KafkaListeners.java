package com.exchangeRates;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class KafkaListeners {

    private DataFetchManager dataFetchManager;

    public KafkaListeners(DataFetchManager dataFetchManager) {
        this.dataFetchManager = dataFetchManager;
    }

    @KafkaListener(topics = "exchangeRatesRequests", groupId = "exchangeRatesRequestsId")
    void listener(String data) {
        if (data.equals("RequestPreviousRates")) {
            dataFetchManager.fetchAndSendHistoricalData();
        }
    }
}
