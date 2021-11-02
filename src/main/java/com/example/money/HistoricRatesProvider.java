package com.example.money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public interface HistoricRatesProvider {

    Map<LocalDate, Map<String, BigDecimal>> getHistoricRates(LocalDate startDate, LocalDate endDate);
}
