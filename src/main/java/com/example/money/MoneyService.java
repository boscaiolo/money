package com.example.money;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RequestMapping("/ecb-currency-rates")
public interface MoneyService {

    @GetMapping
    ResponseEntity<Map<String, BigDecimal>> getRatesForDate(
            @RequestParam(name="date", required=false, defaultValue="#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);

    @GetMapping("/convert")
    ResponseEntity<String> convertCurrencyForDate(
            @RequestParam(name="date", required=false, defaultValue="#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(name="source", required=true) String source,
            @RequestParam(name="target", required=true) String target,
            @RequestParam(name="amount", required=true) BigDecimal amount);

    @GetMapping("/highest")
    ResponseEntity<BigDecimal> highestCurrencyRateForDate(
            @RequestParam(name="startDate", required=false, defaultValue="#{T(java.time.LocalDate).of(1999, 1, 4)}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name="endDate", required=false, defaultValue="#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(name="currency", required=true) String currency);

    @GetMapping("/average")
    ResponseEntity<BigDecimal> averageCurrencyRateForDate(
            @RequestParam(name="startDate", required=false, defaultValue="#{T(java.time.LocalDateTime).of(1999, 1, 4)}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name="endDate", required=false, defaultValue="#{T(java.time.LocalDateTime).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(name="currency", required=true) String currency);
}
