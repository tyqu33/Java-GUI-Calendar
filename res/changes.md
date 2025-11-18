# 1 Interface Update for CalendarInterface

We have added functions in CalendarInterface (initially created in HW4) to support operations needed in HW5.

# 2 Bug fix for Calendar Implementation

We have fixed some abysmal bugs from the feedback of HW4 and added tests to HW5. In detail, we have fixed the bug where
editing an event series given the start date/time and no end date/time will now locate the series correctly without
error of taking the end date/time as 17:00. And We have fixed the bug where editing part of an event series given the
edit start date/time after the first event and leave some events unchanged to work correctly.

# 3 Stick to MVC Design Pattern

We still stick to the basic pattern of MVC as we put the command parsing and operation designation in the controller,
the new CalendarEntityInterface and MultiCalendarManagerInterface as the model part, and the view as the output channel.

# 4 Composition to Reuse

We apply the CalendarInterface as a member in our new implementation to support sublevel operations in a calendar, on
top of which we apply name and timezone to wrap it up as a calendar entity at the main level of HW5.