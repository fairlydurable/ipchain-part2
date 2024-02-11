.PHONY: compile run-sample run run2 run3

clean:
	mvn clean

build:
	mvn -q compile

run-sample:
	@mvn -q exec:java -Dexec.mainClass="io.temporal.sample_project.Main"

run:
	@mvn -q exec:java -Dexec.mainClass="io.temporal.first_action.IPAddressFinderActivityImpl"
	
run2:
	@mvn -q exec:java -Dexec.mainClass="io.temporal.first_action.GeolocationFetcherActivityImpl" -Dexec.args="`make run`"

run3:
	@mvn -q exec:java -Dexec.mainClass="io.temporal.first_action.WeatherFetcherActivityImpl" -Dexec.args="`make run2`"
