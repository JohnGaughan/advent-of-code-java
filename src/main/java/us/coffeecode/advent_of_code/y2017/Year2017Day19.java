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

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 19, title = "A Series of Tubes")
@Component
public final class Year2017Day19 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public String calculatePart1(final PuzzleContext pc) {
    return calculate(pc).letters;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc).steps;
  }

  private Result calculate(final PuzzleContext pc) {
    final int[][] grid = il.linesAsCodePoints(pc);

    // Start at the first pipe in the first row and go down.
    int y = 0;
    int x = 0;
    // The packet starts off-screen, so we start navigation after taking one step.
    long steps = 1;
    Direction d = Direction.S;
    while (grid[y][x] != '|') {
      ++x;
    }

    // Now move through the maze until there is nowhere to go.
    final Collection<Integer> lettersFound = new ArrayList<>();

    while (true) {
      // Corner: find the next direction and go that way.
      if (grid[y][x] == '+') {
        if ((d == Direction.N) || (d == Direction.S)) {
          if ((grid[y][x - 1] == '-') || Character.isAlphabetic(grid[y][x - 1])) {
            d = Direction.W;
            --x;
          }
          else {
            d = Direction.E;
            ++x;
          }
        }
        else {
          if ((grid[y - 1][x] == '|') || Character.isAlphabetic(grid[y - 1][x])) {
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
          if ((d == Direction.N) && (grid[y - 1][x] == ' ')) {
            break;
          }
          else if ((d == Direction.S) && (grid[y + 1][x] == ' ')) {
            break;
          }
          else if ((d == Direction.W) && (grid[y][x - 1] == ' ')) {
            break;
          }
          else if ((d == Direction.E) && (grid[y][x + 1] == ' ')) {
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
    final int[] codePoints = lettersFound.stream()
                                         .mapToInt(Integer::intValue)
                                         .toArray();
    return new Result(steps, new String(codePoints, 0, codePoints.length));
  }

  private static enum Direction {
    N,
    S,
    E,
    W;
  }

  private static record Result(long steps, String letters) {}
}
