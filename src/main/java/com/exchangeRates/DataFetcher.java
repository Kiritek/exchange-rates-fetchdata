package com.exchangeRates;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

@Service
public class DataFetcher {
    private final Logger logger = LoggerFactory.getLogger(DataFetcher.class);

    private ResponseEntity<String> getResponse(RestTemplate _restTemplate, String date) {
        try {
            String url = "https://www.bnro.ro/StatisticsReportHTML.aspx?icid=801&table=668&" +
                    "column=5456,5457,5458,5459,5460,5461,5462,5463,5464,5465,5466,5467,5468,5469,5470,5471,5472,5473," +
                    "5474,5476,5477,5478,5479,5480,5481,5482,5483,5484,5485,5486,5487,20251,24691&startDate=" + date + "&stopDate=" +
                    date;

            return _restTemplate.exchange(url
                    , HttpMethod.GET, null, String.class);
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getRawStatusCode()).headers(e.getResponseHeaders()).body(e.getResponseBodyAsString());
        }
    }

    private Document getDocumentFromJsoup(String date) {
        RestTemplate restTemplate = new RestTemplate();
        var response = getResponse(restTemplate, date);
        var jsonString = "";
        if (response.getStatusCode() == HttpStatus.OK) {
            jsonString = response.getBody();
        }

        String html = jsonString;
        return Jsoup.parse(html);
    }

    private List<String> getCurrencyNames(Document document) {
        List<String> naglowkiTabeli = new ArrayList<>();
        Elements rzedy = document.getElementsByTag("tr");
        for (Element rzad : rzedy) {
            Elements naglowki = rzad.getElementsByTag("th");
            for (Element naglowek : naglowki) {
                Elements spany = naglowek.getElementsByTag("span");
                Element dobrySpan = spany.first();
                naglowkiTabeli.add(dobrySpan.text());
            }
        }
        return naglowkiTabeli;
    }

    private List<Currency> getCurrencyData(Document document, List<String> naglowkiTabeli) {
        List<Currency> walutki = new ArrayList<>();

        Elements rzedyV = document.getElementsByTag("tr");
        for (Element rzad : rzedyV) {
            if (rzedyV.indexOf(rzad) > 1) {
                Elements komorki = rzad.getElementsByTag("td");
                String obecnyRzadData = "";
                for (Element walutka : komorki) {
                    if (komorki.indexOf(walutka) == 0) {
                        obecnyRzadData = walutka.text();
                    }
                    if (komorki.indexOf(walutka) != 0) {
                        Currency walutenka = new Currency(obecnyRzadData, naglowkiTabeli.get(komorki.indexOf(walutka)), Double.parseDouble(walutka.text()));
                        walutki.add(walutenka);
                    }
                }
            }
        }
        return walutki;
    }

    public List<Currency> getData(String date) throws IOException {

        Document document = getDocumentFromJsoup(date);
        List<String> naglowkiTabeli = getCurrencyNames(document);
        return getCurrencyData(document, naglowkiTabeli);
    }

}
