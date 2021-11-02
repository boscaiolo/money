package com.example.money;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CsvHistoricRatesProvider implements HistoricRatesProvider{

    final Map<LocalDate, Map<String, BigDecimal>> historicRates = new ConcurrentHashMap<>();

    @PostConstruct
    public void init(){
        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader();

        try (InputStream csvFile = new ClassPathResource("eurofxref-hist.csv").getInputStream()) {
            MappingIterator<Map<String, String>> mi = csvMapper.readerFor(Map.class)
                    .with(schema).readValues(csvFile);
            while (mi.hasNext()){
                Map<String,String> rowAsMap = mi.next();
                Map<String, BigDecimal> dateRates = new ConcurrentHashMap<>();
                LocalDate date = LocalDate.parse(rowAsMap.get("Date"));
                rowAsMap.remove("Date");
                for (Map.Entry<String, String> rate : rowAsMap.entrySet()){
                    if(!rate.getValue().equals("N/A")){
                        dateRates.put(rate.getKey(), new BigDecimal(rate.getValue()));
                    }
                }
                historicRates.put(date, dateRates);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Map<LocalDate, Map<String, BigDecimal>> getHistoricRates() {
        return this.historicRates;
    }
}
