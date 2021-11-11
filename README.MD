## Description
Test task on a job interview.
Implement a simple web-service with two methods: one for accepting key-value (int as value) and aggregate values, second for simple 'report' of aggregated value

## Implementation notes
The result is not production-ready. On huge amounts of pair or keys it will consume a lot of memory.
Some optimization required, e.g. bulk persisting and drain in-memory storage after persist. 
Also, it's better to use production RDBMS rather than H2, utilize 'testcontainers' for testing. 

## Install
1. mkdir statCounter
2. cd statCounter
3. git clone https://github.com/myworktech/StatCounter.git
4. mvn clean install -DskipTests=true

## Run   
1. cd target
2. java -jar statCounter.jar

## Test
1. Run the application
2. mvn test
