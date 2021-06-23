/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2021  John Gaughan
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
package us.coffeecode.advent_of_code.y2017;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/19">Year 2017, day 19</a>. This problem asks us to navigate an ASCII art
 * graph that is really one long, snaking route. Part one asks us which letters we see along the route, part two asks
 * for the total number of steps taken.
 * </p>
 * <p>
 * The input is well-formed and even has a space buffer around it, making navigating around edges easy. Move around the
 * graph, and find the one valid path each time we turn. Count the steps taken and track the letters seen along the way,
 * and return the answer as appropriate for each part.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day19 {

  public String calculatePart1() {
    return calculate().letters;
  }

  public long calculatePart2() {
    return calculate().steps;
  }

  private Result calculate() {
    int[][] grid = getInput();

    // Start at the first pipe in the first row and go down.
    int y = 0;
    int x = 0;
    // The packet starts off-screen, so we start navigation after taking one step.
    int steps = 1;
    Direction d = Direction.S;
    while (grid[y][x] != '|') {
      ++x;
    }

    // Now move through the maze until there is nowhere to go.
    final List<Integer> lettersFound = new ArrayList<>();

    while (true) {
      // Corner: find the next direction and go that way.
      if (grid[y][x] == '+') {
        if (d == Direction.N || d == Direction.S) {
          if (grid[y][x - 1] == '-') {
            d = Direction.W;
            --x;
          }
          else {
            d = Direction.E;
            ++x;
          }
        }
        else {
          if (grid[y - 1][x] == '|') {
            d = Direction.N;
            --y;
          }
          else {
            d = Direction.S;
            ++y;
          }
        }
      }
      else {
        if (Character.isLetter(grid[y][x])) {
          lettersFound.add(Integer.valueOf(grid[y][x]));

          // Is this the end of the line?
          if (d == Direction.N && grid[y - 1][x] == ' ') {
            break;
          }
          else if (d == Direction.S && grid[y + 1][x] == ' ') {
            break;
          }
          else if (d == Direction.W && grid[y][x - 1] == ' ') {
            break;
          }
          else if (d == Direction.E && grid[y][x + 1] == ' ') {
            break;
          }
        }
        // Keep going in the same direction
        if (d == Direction.N) {
          --y;
        }
        else if (d == Direction.S) {
          ++y;
        }
        else if (d == Direction.E) {
          ++x;
        }
        else {
          --x;
        }
      }
      ++steps;
    }
    return new Result(steps, Utils.codePointsToString(lettersFound));
  }

  /** Get the input data for this solution. */
  private int[][] getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2017, 19)).stream().map(s -> s.codePoints().toArray()).toArray(
        int[][]::new);
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static enum Direction {
    N,
    S,
    E,
    W;
  }

  private static final class Result {

    final int steps;

    final String letters;

    Result(final int _steps, final String _letters) {
      steps = _steps;
      letters = _letters;
    }
  }

}
