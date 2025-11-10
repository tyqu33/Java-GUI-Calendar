# Calendar Application Running Instructions

This document details how to run the Calendar application using JAR file with arguments.

## Running Preparation

The executable JAR file, CalendarApp.jar, is already included in this project: build/libs/calendar-1.0.jar

## Interactive Mode

In this mode, the application starts up, displays a welcome message, and waits for user commands to be entered line by line through the terminal.

Example:

java -jar build/libs/calendar-1.0.jar --mode interactive

## Headless Mode

This mode is used for batch processing. The application reads commands from the specified text file in sequence, executes the commands, and exits at the end of the file when it reaches "exit".

Example:

java -jar build/libs/calendar-1.0.jar --mode headless commands.txt
