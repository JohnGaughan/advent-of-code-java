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
package us.coffeecode.advent_of_code.y2015;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Range2D;

@AdventOfCodeSolution(year = 2015, day = 6)
@Component
public final class Year2015Day06 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final boolean[][] grid = new boolean[SIZE][SIZE];
    for (final Action action : il.linesAsObjects(pc, Action::new)) {
      for (final int y : action.range.getRangeYInclusive()) {
        for (final int x : action.range.getRangeXInclusive()) {
          grid[y][x] = action.command.apply(grid[y][x]);
        }
      }
    }
    long lit = 0;
    for (final boolean[] row : grid) {
      for (final boolean b : row) {
        if (b) {
          ++lit;
        }
      }
    }
    return lit;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int[][] grid = new int[SIZE][SIZE];
    for (final Action action : il.linesAsObjects(pc, Action::new)) {
      for (final int y : action.range.getRangeYInclusive()) {
        for (final int x : action.range.getRangeXInclusive()) {
          grid[y][x] = action.command.apply(grid[y][x]);
        }
      }
    }
    long brightness = 0;
    for (final int[] row : grid) {
      for (final int i : row) {
        brightness += i;
      }
    }
    return brightness;
  }

  private static final int SIZE = 1_000;

  /** Enumeration of commands to change light statuses. */
  private static enum Command {

    OFF {

      @Override
      public boolean apply(final boolean value) {
        return false;
      }

      @Override
      public int apply(final int value) {
        return Math.max(value - 1, 0);
      }
    },
    ON {

      @Override
      public boolean apply(final boolean value) {
        return true;
      }

      @Override
      public int apply(final int value) {
        return value + 1;
      }
    },
    TOGGLE {

      @Override
      public boolean apply(final boolean value) {
        return !value;
      }

      @Override
      public int apply(final int value) {
        return value + 2;
      }
    };

    public abstract boolean apply(final boolean value);

    public abstract int apply(final int value);
  }

  /** Represents one discrete action to perform on the lights. */
  private static final class Action {

    private static final String TURN_ON = "turn on";

    private static final String TURN_OFF = "turn off";

    private static final String TOGGLE = "toggle";

    private static final String COORDINATE_SPLIT = " through ";

    final Command command;

    final Range2D range;

    /** Constructs a <code>Action</code> from a line in the input text file. */
    Action(final String line) {
      final int startOfSecondToken;

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

      // Figure out the delimiters for the remainder of the tokens.
      final int firstComma = line.indexOf(',', startOfSecondToken);
      final int endOfSecondNumber = line.indexOf(COORDINATE_SPLIT, firstComma);
      final int startOfThirdNumber = endOfSecondNumber + COORDINATE_SPLIT.length();
      final int secondComma = line.indexOf(',', endOfSecondNumber);

      // Parse the coordinates.
      final int x1 = Integer.parseInt(line.substring(startOfSecondToken, firstComma));
      final int y1 = Integer.parseInt(line.substring(firstComma + 1, endOfSecondNumber));
      final int x2 = Integer.parseInt(line.substring(startOfThirdNumber, secondComma));
      final int y2 = Integer.parseInt(line.substring(secondComma + 1));
      range = new Range2D(x1, y1, x2, y2);
    }

    @Override
    public String toString() {
      return "[" + command + "," + range + "]";
    }

  }

}
