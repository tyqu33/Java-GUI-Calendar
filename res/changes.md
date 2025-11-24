# 1 Interface Update for CalendarInterface

We have added functions in CalendarInterface (initially created in HW4) to support operations needed in HW5.

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


