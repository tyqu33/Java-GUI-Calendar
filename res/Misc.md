# 1 Iteration in HW6

1) A GUI view (JframeCalendarView) has been added, along with Features interface and GuiCalendarController class in
   controller.
2) A EventWrapper has been added to wrap up an Event with the name and timezone of its belonging calendar.
3) Added convertTimezone(ZoneId oldZone, ZoneId newZone) method to CalendarInterface. This fixes a bug 
   where changing a calendar's timezone would not update event times, causing events to display at incorrect local times

# 2 Code Smell Fix from HW5 Feedback

We have fixed some code smells from the feedback of HW5 and added tests to HW6.

1) We have fixed the code smell that methods in CalendarInterface have a too long argument list. In detail, we added
   EventContext to incorporate the 6 event properties, and replaced regarding argument lists in methods with it.

a) CalendarInterface and Calendar applies EventContext.
b) Parsing command using regex (CreateCommand, EditCommand) in controller applies EventContext.
c) MultiCalendarManager and JframeCalendarView applies EventContext.

d) regarding test files: mock models (MockModel, SecondMockModel, ThirdMockModel, ForthMockModel)
tests (CalendarTest, ModelTest, CalendarEntityTest, ManagerModelTest, ViewCommandsTest)
In these test files, calls to according methods using EventContext have been justified.

2) We added tests missing from HW5 self evaluation.

# 3 Stick to MVC Design Pattern

We still stick to the basic pattern of MVC. For GUI, We apply a Features interface for clarification of what function
GUI needs. We have added another controller GuiCalendarController to manage the designation between JframeCalendarView
and model (MultiCalendarManager).

# 4 GUI Features supported

The GUI supports:

1) Create a new calendar for a particular timezone.
2) Select a calendar; create, edit, and view events in the left panel for the selected calendar.
3) The calendar name on the left-top corner shows which calendar users are on when interacting with the GUI. The day
   cells in the month view shows days with events, and days blank without events.
4) Initially there's a default calendar with the name "Default", and the timezone the same as their system setting's
   timezone. If users don't create another calendar with designated name and timezone, they are operating on Default
   calendar.
5) Select on a day cell in month view, the left panel "Events on Selected Date" will list the events on that day.
6) Create a single event using "Create Event" button, or create an event series using "Create Event Series" button. For
   event series, a user can specify the weekdays on which the event will repeat and the frequency in
   terms of number of occurrences or until an end date.
7) Select a day cell in the month view, pick one event on the list, and edit the event. Users can choose to edit only 
   that single occurrence or edit the entire series.
8) Search events with the keyword of event name, using the "Search" button on the right-top corner. The result will show
   all events with name matching the keyword, regardless which calendar they belong to. Edit button has also been
   offered in the result list.
9) Edit calendar properties (name and timezone) using the "Edit" button next to the calendar name. When the timezone is
   changed, all event times in the calendar are automatically converted to the new timezone.
10) Navigate between months using the previous ("<") and next (">") buttons to view different months.
11) Switch between different calendars using the dropdown menu labeled "Switch to:" to view and manage events in
    different calendars.
12) Export the current calendar to either CSV or iCal format using the "Export" button.

