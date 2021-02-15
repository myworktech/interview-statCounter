## INSTALL
1. mkdir statCounter
2. cd statCounter
3. git clone https://github.com/myworktech/StatCounter.git
4. mvn clean install -DskipTests=true

## RUN   
1. cd statCounter/target
2. java -jar statCounter.jar либо chmod +x statCounter.jar && ./statCounter.jar

## TEST

1. Ensure the application is running
2. mvn test