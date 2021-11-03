# Money Application

A sample Spring Boot App which allows to:
  * retreive the EUR currency exhange rates for a given date fetched from the ECB website
  * retreive the highest EUR exhange rate of a currency for a given date period  
  * retreive the average EUR exhange rate of a currency for a given date period
  * convert an amount of a given currency to another currency based on the EUR exchange rates for a specific date
  
 ## Currengt limitaions
 
  The rates are loaded on the startup of the application, for a more robust solution this should be done on a daily basis with a timed java taskd
 
## Source code

https://github.com/boscaiolo/money
