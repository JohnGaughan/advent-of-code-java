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
package net.johngaughan.advent_of_code.y2015;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/6">Year 2015, day 6</a>. Given an array of a million lights in a 1,000 by
 * 1,000 grid, turn sections of them on, off, or toggle their state. Part one asks how many lights are lit after
 * executing the input commands in order. Part two changes things up by giving each light a brightness level where on
 * and off increase and decrease the light level (minimum of zero), while toggling it increases the brightness by two.
 * It then asks the total brightness across all lights.
 * </p>
 * <p>
 * Simple solution to a puzzle where brute force is the only option. Nothing much to say here: just iterate through the
 * input and see what happens.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day06 {

  public long calculatePart1(final Path path) {
    final boolean[][] grid = new boolean[1000][1000];
    for (final Action action : parse(path)) {
      for (int i = action.x1; i <= action.x2; ++i) {
        for (int j = action.y1; j <= action.y2; ++j) {
          if (action.command == Command.ON) {
            grid[i][j] = true;
          }
          else if (action.command == Command.OFF) {
            grid[i][j] = false;
          }
          else if (action.command == Command.TOGGLE) {
            grid[i][j] = !grid[i][j];
          }
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

  public long calculatePart2(final Path path) {
    final int[][] grid = new int[1000][1000];
    for (final Action action : parse(path)) {
      for (int i = action.x1; i <= action.x2; ++i) {
        for (int j = action.y1; j <= action.y2; ++j) {
          if (action.command == Command.ON) {
            ++grid[i][j];
          }
          else if (action.command == Command.OFF) {
            --grid[i][j];
            if (grid[i][j] < 0) {
              grid[i][j] = 0;
            }
          }
          else if (action.command == Command.TOGGLE) {
            grid[i][j] += 2;
          }
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

  /** Parse the file located at the provided path location. */
  private List<Action> parse(final Path path) {
    try {
      return Files.readAllLines(path).stream().map(s -> new Action(s)).collect(Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /** Enumeration of commands to change light statuses. */
  private static enum Command {
                               OFF,
                               ON,
                               TOGGLE;
  }

  /** Represents one discrete action to perform on the lights. */
  private static final class Action {

    private static final String TURN_ON = "turn on";

    private static final String TURN_OFF = "turn off";

    private static final String TOGGLE = "toggle";

    private static final String COORDINATE_SPLIT = " through ";

    final Command command;

    final int x1;

    final int x2;

    final int y1;

    final int y2;

    /** Constructs a <code>Action</code>. */
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
      x1 = Integer.parseInt(line.substring(startOfSecondToken, firstComma));
      y1 = Integer.parseInt(line.substring(firstComma + 1, endOfSecondNumber));
      x2 = Integer.parseInt(line.substring(startOfThirdNumber, secondComma));
      y2 = Integer.parseInt(line.substring(secondComma + 1));
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return "[" + command + ",(" + x1 + "," + x2 + "),(" + y1 + "," + y2 + ")]";
    }

  }

}
