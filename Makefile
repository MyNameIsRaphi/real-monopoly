# Variables
JAR=".:lib/mysql-connector-j-9.1.0.jar:lib/json-20240303.jar"
SRC=main.java
CLASS=main
SRC_test=test.java 
CLASS_test=test

# Build target
build:
	@javac -cp $(JAR) $(SRC)

# Run target
run:
	@java -cp $(JAR) $(CLASS)

# Clean target to remove .class files
clean:
	@find . -name "*.class" -type f -delete

# Build target for test
build_test:
	@javac -cp $(JAR) $(SRC_test)

# Run target for test
run_test:
	@java -cp $(JAR) $(CLASS_test)

# Clean target for test to remove .class files
clean_test:
	@find . -name "*.class" -type f -delete

test: 
	@make build_test
	@make run_test
	@make clean_test

# Default target
all: build run