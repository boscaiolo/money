package com.example.money;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;

@RestController
public class MoneyServiceImpl implements MoneyService {

    private HistoricRatesProvider historicRatesProvider;

    @Autowired
    public MoneyServiceImpl(HistoricRatesProvider historicRatesProvider){
        this.historicRatesProvider = historicRatesProvider;
    }

    @Override
    public ResponseEntity<Map<String, BigDecimal>> getRatesForDate(LocalDate date) {
        Map<String, BigDecimal> rates = historicRatesProvider.getHistoricRates().get(date);
        if(rates == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(rates, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> convertCurrencyForDate(LocalDate date, String source, String target, BigDecimal amount) {
        Map<String, BigDecimal> rates = historicRatesProvider.getHistoricRates().get(date);
        if(rates == null){
            String error = "Rates for " + date + " do not exist";
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        BigDecimal sourceRate = (source.equals("EUR")) ? BigDecimal.ONE : rates.get(source);
        if(sourceRate == null){
            String error = "Rate for the source currency '" + source + "' do not exist";
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        BigDecimal targetRate = (target.equals("EUR")) ? BigDecimal.ONE : rates.get(target);
        if(targetRate == null){
            String error = "Rate for the target currency '" + target + "' do not exist";
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        BigDecimal exchangeRate = amount.divide(sourceRate, 4, RoundingMode.HALF_EVEN);
        BigDecimal targetAmount = exchangeRate.multiply(targetRate);

        return new ResponseEntity<>(targetAmount.toPlainString(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BigDecimal> highestCurrencyRateForDate(LocalDate startDate, LocalDate endDate, String currency) {
        return null;
    }

    @Override
    public ResponseEntity<BigDecimal> averageCurrencyRateForDate(LocalDate startDate, LocalDate endDate, String currency) {
        return null;
    }
}
