/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2020  John Gaughan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package net.johngaughan.advent.y2015.day6;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * File parser for year 2015, day 6.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
final class Parser {

  private static final String TURN_ON = "turn on";

  private static final String TURN_OFF = "turn off";

  private static final String TOGGLE = "toggle";

  private static final String COORDINATE_SPLIT = " through ";

  public List<Action> parse(final Path path) {
    try {
      return parse(Files.readAllLines(path));
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private List<Action> parse(final List<String> lines) {
    final List<Action> actions = new ArrayList<>(lines.size());
    for (final String line : lines) {
      actions.add(parse(line));
    }
    return actions;
  }

  private Action parse(final String line) {
    final int startOfSecondToken;
    final Command command;

    // Get the command, and figure out where the numbers start after it.
    if (line.startsWith(TURN_ON)) {
      command = Command.ON;
      startOfSecondToken = TURN_ON.length() + 1;
    }
    else if (line.startsWith(TURN_OFF)) {
      command = Command.OFF;
      startOfSecondToken = TURN_OFF.length() + 1;
    }
    else if (line.startsWith(TOGGLE)) {
      command = Command.TOGGLE;
      startOfSecondToken = TOGGLE.length() + 1;
    }
    else {
      throw new IllegalArgumentException("Unknown command on line [" + line + "]");
    }

    // Figure out the delimeters for the remainder of the tokens.
    final int firstComma = line.indexOf(',', startOfSecondToken);
    final int endOfSecondNumber = line.indexOf(COORDINATE_SPLIT, firstComma);
    final int startOfThirdNumber = endOfSecondNumber + COORDINATE_SPLIT.length();
    final int secondComma = line.indexOf(',', endOfSecondNumber);

    // Parse the coordinates.
    final int x1 = Integer.parseInt(line.substring(startOfSecondToken, firstComma));
    final int y1 = Integer.parseInt(line.substring(firstComma + 1, endOfSecondNumber));
    final int x2 = Integer.parseInt(line.substring(startOfThirdNumber, secondComma));
    final int y2 = Integer.parseInt(line.substring(secondComma + 1));

    return new Action(command, x1, x2, y1, y2);
  }

}
