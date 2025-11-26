
# Calendar Application Running Instructions

This document details how to run the Calendar application using JAR file with arguments.

## Running Preparation

The executable JAR file, CalendarApp.jar, is already included in this project: build/libs/calendar-1.0.jar

## GUI(Graphical User Interface) Mode

This mode launches a graphical calendar interface where users can interact with the application.


Example:
- Simply double-click on the jar file: build/libs/calendar-1.0.jar
- Or run: java -jar build/libs/calendar-1.0.jar

## Interactive Mode

In this mode, the application starts up, displays a welcome message, and waits for user commands to be entered line by line through the terminal.

Example:

- Run: java -jar build/libs/calendar-1.0.jar --mode interactive

## Headless Mode

This mode is used for batch processing. The application reads commands from the specified text file in sequence, executes the commands, and exits at the end of the file when it reaches "exit".

Example:

- Run: java -jar build/libs/calendar-1.0.jar --mode headless commands.txt

## Calendar Application GUI Guide

### Calendar Operations

- Create a new calendar: Click "New Calendar" -> Enter name and timezone -> Click "Confirm"

- Switch between calendars: Use the "Switch to:" dropdown to select a different calendar

- Edit calendar properties: Click "Edit" next to calendar name -> Modify name or timezone -> Click "Save"

### Event Operations

- Create a single event: Click "Create Event" -> Fill in event details -> Click "Create"

- Create an event series: Click "Create EventSeries" -> Enter details and recurrence pattern -> Click "Create"

- View events on a date: Click any day cell in the calendar to see events in the left panel

- Edit a single event: Click day cell -> Click "..." on event -> Select "Edit" -> Modify details -> Click "Confirm"

- Edit an event series: Click day cell -> Click "..." on series event -> Select "Edit Series" -> Modify properties -> Click "Confirm"

- Search for events: Click "Search" -> Enter keyword -> Click "Search" to find events across all calendars

### Other Operations

- Navigate months: Use "<" and ">" buttons to move between months

- Export calendar: Click "Export" -> Select CSV or iCal format -> Enter filename -> Click "OK"