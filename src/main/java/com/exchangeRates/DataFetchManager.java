package com.exchangeRates;


import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@EnableScheduling
public class DataFetchManager {
    private DataFetcher dataFetcher;
    private DateManager dateManager;
    private CurrencySender currencySender;

    public DataFetchManager(DataFetcher dataFetcher, DateManager dateManager, CurrencySender currencySender) {
        this.dataFetcher = dataFetcher;
        this.dateManager = dateManager;
        this.currencySender = currencySender;
    }

    public void fetchAndSendHistoricalData() {
        List<String> validDates = dateManager.getThisYearValidDates();
        for (int i = 0; i < validDates.size(); i++) {
            try {
                List<Currency> currencies = dataFetcher.getData(validDates.get(i));
                currencySender.sendCurrencyData(currencies);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Scheduled(cron = "00 01 13 * * MON-FRI")
    public void fetchAndSendTodayData() {

        String today = dateManager.getTodayDateAsString();
        System.out.println(today);
        if (!today.equals("Holiday")) {
            try {
                List<Currency> currencies = dataFetcher.getData(today);
                currencySender.sendCurrencyData(currencies);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
