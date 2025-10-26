package calendar.controller;

import java.util.regex.Pattern;

public class EditCommand extends CommandFactory {
  private static final Pattern E_FROM_TO_REPEATS_N = Pattern.compile(
      "^edit event (.*?) from (\\S+) to (\\S+) repeats (\\S+) for (\\d+) times$");

  @Override
  protected void parseCommand(String commandLine) {

  }
}
