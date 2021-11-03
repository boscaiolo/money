package com.example.money;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.zip.ZipFile;

@Component
public class CsvHistoricRatesProvider implements HistoricRatesProvider {

    private final Map<LocalDate, Map<String, BigDecimal>> historicRates = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        ZipFile zipFile = downloadZipFile();
        if (zipFile != null) {
            CsvMapper csvMapper = new CsvMapper();
            CsvSchema schema = CsvSchema.emptySchema().withHeader();

            try (InputStream csvFile = zipFile.getInputStream(zipFile.entries().nextElement())) {
                MappingIterator<Map<String, String>> mi = csvMapper.readerFor(Map.class)
                        .with(schema).readValues(csvFile);
                while (mi.hasNext()) {
                    Map<String, String> rowAsMap = mi.next();
                    Map<String, BigDecimal> dateRates = new ConcurrentHashMap<>();
                    LocalDate date = LocalDate.parse(rowAsMap.get("Date"));
                    rowAsMap.remove("Date");
                    for (Map.Entry<String, String> rate : rowAsMap.entrySet()) {
                        if (!rate.getValue().equals("N/A")) {
                            dateRates.put(rate.getKey(), new BigDecimal(rate.getValue()));
                        }
                    }
                    historicRates.put(date, dateRates);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Map<LocalDate, Map<String, BigDecimal>> getHistoricRates() {
        return this.historicRates;
    }

    @Override
    public List<BigDecimal> getPeriodHistoricRatesForCurrency(LocalDate startDate, LocalDate endDate, String currency) {
        LocalDate start = (startDate!= null) ? startDate : LocalDate.of(1999, 1, 4);
        LocalDate end = (endDate != null) ? endDate : LocalDate.now();
        Map<LocalDate, Map<String, BigDecimal>> ratesForPeriod = historicRates.entrySet().stream().filter(e ->
                (e.getKey().isEqual(start) || e.getKey().isAfter(start)) &&
                        (e.getKey().isEqual(end) || e.getKey().isBefore(end)))
                    .collect(Collectors.toMap(Map.Entry :: getKey, Map.Entry :: getValue));

        // TODO : convert this to a lambda
        List<BigDecimal> currencyRates = new ArrayList<>();
        for(Map<String, BigDecimal> m : ratesForPeriod.values()){
            BigDecimal rate = m.get(currency);
            if(rate != null){
                currencyRates.add(rate);
            }
        }

        return currencyRates;
    }

    private ZipFile downloadZipFile(){
        try (BufferedInputStream in = new BufferedInputStream(new URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist.zip").openStream());
             FileOutputStream fileOutputStream = new FileOutputStream("eurofxref-hist.zip")) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            return new ZipFile("eurofxref-hist.zip");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}