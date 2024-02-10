.PHONY: compile runSample run

clean:
	mvn clean

build:
	mvn -q compile

run-sample:
	@mvn -q exec:java -Dexec.mainClass="io.temporal.sample_project.Main"

run:
	@mvn -q exec:java -Dexec.mainClass="io.temporal.first_action.IPAddressFinderActivityImpl"
