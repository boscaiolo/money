package com.example.money;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class MoneyApplicationTests {

	@Autowired
	private MockMvc mvc;

	@Test
	void contextLoads() {
	}

	@Test
	public void whenMapLoadedGetRatesForDateReturnsCorrectResponse() throws Exception {

		ResultActions result = mvc.perform(get("/currency-rates?date=2021-09-28")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void whenMapLoadedGetRatesForDateWithoutDateReturnsCurrentDateRates() throws Exception {

		ResultActions result = mvc.perform(get("/currency-rates")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void whenMapLoadedGetRatesForDateReturnRatesForDate() throws Exception {

		ResultActions result = mvc.perform(get("/currency-rates?date=1999-01-04")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void whenMapLoadedGetRatesForDateNotExistingReturnNotFound() throws Exception {

		ResultActions result = mvc.perform(get("/currency-rates?date=4000-01-04")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void whenMapLoadedConvertCurrencyForDateReturnsProper() throws Exception {

		ResultActions result = mvc.perform(get("/currency-rates/convert-for-date?date=2021-09-28&source=EUR&target=BGN&amount=1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void whenMapLoadedConvertCurrencyForDateReturnsNotFound() throws Exception {

		ResultActions result = mvc.perform(get("/currency-rates/convert-for-date?date=4000-09-28&source=EUR&target=BGN&amount=1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void whenMapLoadedHighestCurrencyReturnsNotFound() throws Exception {

		ResultActions result = mvc.perform(get("/currency-rates/highest-currency-rate-for-period?currency=STO")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void whenMapLoadedHighestCurrencyReturnsProper() throws Exception {

		ResultActions result = mvc.perform(get("/currency-rates/highest-currency-rate-for-period?currency=BGN")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void whenMapLoadedAverageCurrencyReturnsNotFound() throws Exception {

		ResultActions result = mvc.perform(get("/currency-rates/average-currency-rate-for-period?currency=STO")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void whenMapLoadedAverageCurrencyReturnsProper() throws Exception {

		ResultActions result = mvc.perform(get("/currency-rates/average-currency-rate-for-period?currency=BGN")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}

}
