#!/usr/bin/env bash

DIRECTORY="target/"
if [ ! -d "$DIRECTORY" ]; then
    mvn package
fi

FILENAME="bibleguesser-*.*.*.jar"
JARFILE=$(find "$DIRECTORY" -name "$FILENAME" | tac | head -n 1)

java -jar "$JARFILE"
