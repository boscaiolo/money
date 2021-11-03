package com.example.money;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * A REST API for currency conversion allowing to:
 *  get the conversion rates for a given date
 *  convert an amount of a given currency to another currency based on the EUR exchange rates for a specific date
 *  retrieve the highest EUR exchange rate of a currency for a given date period
 *  retrieve the average EUR exchange rate of a currency for a given date period
 */
@RequestMapping("/currency-rates")
public interface MoneyService {

    /**
     * Retrieves the rates for all listed currencies for the provided date if the Date is not provided the method
     * will return the rates for the current date
     *  @param date {@code {@link LocalDate}} if not provided defaults to the current date
     * @return a (@code {@link Map<String, BigDecimal>} of the rates for the date provided
     */
    @GetMapping
    ResponseEntity<Map<String, BigDecimal>> getRatesForDate(
            @RequestParam(name="date", required=false, defaultValue="#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);

    /**
     * Converts an amount of currency to another currency based on the EUR exchange rate for that date
     * will return the rates for the current date
     *  @param date {@code {@link LocalDate}} if not provided defaults to the current date
     *  @param source the source currency
     *  @param target the target currency
     *  @param amount the amount of the source currency
     * @return the amount in target currency if available
     */
    @GetMapping("/convert-for-date")
    ResponseEntity<String> convertCurrencyForDate(
            @RequestParam(name="date", required=false, defaultValue="#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(name="source", required=true) String source,
            @RequestParam(name="target", required=true) String target,
            @RequestParam(name="amount", required=true) BigDecimal amount);

    /**
     * Retrieves the highest EUR conversion rate of a currency for a given date period
     * @param startDate The start date of the period, if not provided it defaults to 1999-01-04
     * @param endDate  The end date of the period, if not provided it defaults to the current date
     * @param currency The wanted currency
     * @return The highest exchange rate for the currency during the given period
     */
    @GetMapping("/highest-currency-rate-for-period")
    ResponseEntity<String> highestCurrencyRateForDate(
            @RequestParam(name="startDate", required=false, defaultValue="#{T(java.time.LocalDate).of(1999, 1, 4)}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name="endDate", required=false, defaultValue="#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(name="currency", required=true) String currency);

    /**
     * Retrieves the average EUR conversion rate of a currency for a given date period
     * @param startDate The start date of the period, if not provided it defaults to 1999-01-04
     * @param endDate  The end date of the period, if not provided it defaults to the current date
     * @param currency The wanted currency
     * @return The average exchange rate for the currency during the given period
     */
    @GetMapping("/average-currency-rate-for-period")
    ResponseEntity<String> averageCurrencyRateForDate(
            @RequestParam(name="startDate", required=false, defaultValue="#{T(java.time.LocalDate).of(1999, 1, 4)}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name="endDate", required=false, defaultValue="#{T(java.time.LocalDate).now()}")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(name="currency", required=true) String currency);
}
