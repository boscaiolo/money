package com.example.money;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface HistoricRatesProvider {

    Map<LocalDate, Map<String, BigDecimal>> getHistoricRates();

    List<BigDecimal> getPeriodHistoricRatesForCurrency(LocalDate startDate, LocalDate endDate, String currency);
}
