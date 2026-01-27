#!/usr/bin/env bash

mvn package
find target/ -name "*.jar" | tac | xargs java -jar
mvn clean
