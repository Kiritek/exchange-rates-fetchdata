package com.exchangeRates;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.TemporalAdjusters.firstDayOfYear;

@Service
public class DateManager {

    private List<LocalDate> holidayList = new ArrayList<>() {
        {
            add(LocalDate.parse("2022-01-01"));
            add(LocalDate.parse("2022-01-02"));
            add(LocalDate.parse("2022-01-24"));
            add(LocalDate.parse("2022-04-22"));
            add(LocalDate.parse("2022-04-24"));
            add(LocalDate.parse("2022-04-25"));
            add(LocalDate.parse("2022-05-01"));
            add(LocalDate.parse("2022-06-01"));
            add(LocalDate.parse("2022-06-12"));
            add(LocalDate.parse("2022-06-13"));
            add(LocalDate.parse("2022-08-15"));
            add(LocalDate.parse("2022-11-30"));
            add(LocalDate.parse("2022-12-01"));
            add(LocalDate.parse("2022-12-25"));
            add(LocalDate.parse("2022-12-26"));
        }
    };

    public boolean checkHoliday(LocalDate dateToCheck) {
        boolean isDateValid = true;
        if (holidayList.contains(dateToCheck)) {
            isDateValid = false;
        }
        return isDateValid;
    }

    public boolean checkNonWeekend(LocalDate dateToCheck) {
        boolean isDateValid = true;
        if ("SUNDAY".equals(dateToCheck.getDayOfWeek().toString())
                || "SATURDAY".equals(dateToCheck.getDayOfWeek().toString())) {
            isDateValid = false;
        }
        return isDateValid;
    }

    public List<String> getThisYearValidDates() {
        List<String> validDates = new ArrayList<>();

        LocalDate start = LocalDate.now().with(firstDayOfYear());
        LocalDate end = LocalDate.now();
        for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            String formattedDate = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            if (checkHoliday(date) && checkNonWeekend(date)) {
                validDates.add(formattedDate);
            }
        }
        return validDates;
    }

    public String getTodayDateAsString() {
        if (checkHoliday(LocalDate.now())) {
            return LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } else {
            return "Holiday";
        }
    }

}
