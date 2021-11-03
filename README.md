# Money Application

A sample Spring Boot App which allows to:
  * retrieve the EUR currency exchange rates for a given date fetched from the ECB website
  * retrieve the highest EUR exchange rate of a currency for a given date period  
  * retrieve the average EUR exchange rate of a currency for a given date period
  * convert an amount of a given currency to another currency based on the EUR exchange rates for a specific date
  
 ## Current limitaions
 
  The rates are loaded on startup of the application so in order to get the latest rates the application has to be restarted, for more robust solution this should be done on a daily basis with a timed java task
 
## Source code

https://github.com/boscaiolo/money
