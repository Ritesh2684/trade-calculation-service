# trade-calculation-service

This service calculates total value traded per participant

## How to execute the application

Application could be executed using any one of the below methods,

1. Place the file at location `C:/dev/data/trade/trades.csv` and execute the command execute the command `mvn spring-boot:run`
2. Update the parameter inputFileLocation in application.properties to the desired location, Place the file at that location and execute the command execute the command `mvn spring-boot:run`
3. Provide the path via overriding the inputFileLocation patameter at the time with command execution 
`mvn spring-boot:run -Dspring-boot.run.arguments=--inputFileLocation=C:/dev/data/trade/trades.csv`

Once application is up and running, Access database using url `http://localhost:8080/h2-console`

### Result

Once the application is successfully executed, result will be printed on the console as below,

````
2020-08-07 18:51:12.975  INFO 31972 --- [           main] c.t.c.b.TradeEventStepExecutionListener  : --------------TOTAL AMOUNT PER PARTICIPANT ID---------------------------------------
ParticipantID :  Firm 2, TotalTradeAmount: 6575.0
ParticipantID :  Firm 6, TotalTradeAmount: 429.0
ParticipantID :  Firm 3, TotalTradeAmount: 4377.5
ParticipantID :  Firm 1, TotalTradeAmount: 5216.75
ParticipantID :  Firm 7, TotalTradeAmount: 8096.5
ParticipantID :  Firm 4, TotalTradeAmount: 1430.0
ParticipantID :  Firm 5, TotalTradeAmount: 501.75000000000006
2020-08-07 18:51:12.981  INFO 31972 --- [           main] c.t.c.b.TradeEventStepExecutionListener  : --------------TOTAL AMOUNT PER PARTICIPANT ID---------------------------------------

````

## Solution

- For each incoming Trade Event, 
	* Check if the other side of the Trade Event exists in database based on the parameters mentioned in the problem statement.
	* If exists, mark both the Trade Events with Flat isTraded as 'Y' and update the database, settle the trade amount per participant id i.e. add if New and minus if Cancel
	* If not exists, store the record in database.
	
## Tools used

- Java
- Spring batch
- H2 Database
- Maven

## Few possible enhancements
- File should be archived after processing
- Exception handling with respect to database.
