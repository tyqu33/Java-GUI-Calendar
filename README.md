# Java GUI Calendar

A desktop calendar application in Java that supports multiple calendars across different
timezones, single and recurring events, and export to CSV / iCal. The same core model is
driven by three interchangeable front-ends: a **Swing GUI**, an **interactive command-line**
mode, and a **headless scripting** mode.

Built as a learning project to practice object-oriented design principles — MVC separation,
the Command and Builder patterns, and interface-driven, testable code.

## Features

- **Multiple calendars**, each in a user-chosen timezone, with a default calendar in the
  system timezone
- **Events**: create / edit / view single events and recurring **event series**
- **Month view** with navigation between months; click a day to see its events
- **Search** for events by keyword across all calendars
- **Export** a calendar to **CSV** or **iCal (.ics)**
- **Three run modes** sharing one model: GUI, interactive CLI, and headless (batch script)
- Graceful validation and user-facing error messages

## Architecture

The application follows a clean **Model–View–Controller** split, with the *same model* reused
across all three front-ends:

```
          ┌─────────────┐        ┌──────────────────────────┐        ┌────────────────┐
 user ──▶ │    View     │ ─────▶ │        Controller        │ ─────▶ │     Model      │
          │ Swing / CLI │        │  Command pattern:        │        │ Calendars,     │
          │ / headless  │ ◀───── │  Create/Edit/Show/Export │ ◀───── │ Events, Series │
          └─────────────┘        └──────────────────────────┘        └────────────────┘
```

Design choices worth noting:

- **One model, three views.** `MultiCalendarManager` and the calendar/event model are UI-agnostic;
  the GUI, interactive CLI, and headless runner are just different views over the same core. Adding
  a new front-end requires no model changes.
- **Command pattern in the controller.** Each user action is a discrete command
  (`CreateCommand`, `EditCommand`, `ShowCommand`, `ExportCommand`, `CreateCalendarCommand`,
  `UseCalendarCommand`, …), which keeps the controller open to extension and easy to test.
- **Builder pattern** for the domain objects (`Event.EventBuilder`, `EventSeries.EventSeriesBuilder`,
  `CalendarEntity.CalendarEntityBuilder`) to construct immutable-style events with many optional fields.
- **Interface-driven** throughout (`CalendarInterface`, `EventInterface`,
  `MultiCalendarManagerInterface`, `Features`, …) so components depend on abstractions, which also
  makes mocking straightforward in tests.
- **Pluggable exporters** (`CsvExporter`, `IcalExporter`) behind a common surface.

## Project structure

```
src/main/java/calendar/
├── model/          # Calendar, MultiCalendarManager, EventKey  (core domain + interfaces)
├── event/          # Event, EventSeries, EventContext, builders (single & recurring events)
├── calendarentity/ # CalendarEntity + builder (a calendar's identity/timezone)
├── controller/     # CalendarController, GuiCalendarController, and the *Command classes
├── view/           # Swing GUI + text views
├── util/           # CsvExporter, IcalExporter
└── enums/          # EventStatus, UserStatus
CalendarRunner       # application entry point (selects the run mode)

config/checkstyle/   # Checkstyle rules
docs/                # generated Javadoc
```

## Build & run

Requires a JDK (Java 11+) and Gradle (a Gradle wrapper is included).

```bash
# Build
./gradlew build           # produces build/libs/calendar-1.0.jar

# GUI mode (also runnable by double-clicking the jar)
java -jar build/libs/calendar-1.0.jar

# Interactive CLI mode
java -jar build/libs/calendar-1.0.jar --mode interactive

# Headless mode — run a batch of commands from a script file
java -jar build/libs/calendar-1.0.jar --mode headless commands.txt
```

A full walkthrough of the GUI and the command syntax is in [USEME.md](USEME.md).

## Testing

Unit tests cover the model, controller, events/series, and exporters, using **mock views and
mock models** (`MockGuiView`, `*MockModel`) to test the controller and view logic in isolation —
e.g. `ModelTest`, `ControllerTest`, `GuiCalendarControllerTest`, `EventSeriesTest`,
`CsvExporterTest`, `IcalExporterTest`.

```bash
./gradlew test
```

## Tech stack

Java · Swing · Gradle · JUnit · Checkstyle
