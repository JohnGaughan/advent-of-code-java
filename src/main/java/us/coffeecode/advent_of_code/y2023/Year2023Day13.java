/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2023  John Gaughan
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
package us.coffeecode.advent_of_code.y2023;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2023, day = 13)
@Component
public class Year2023Day13 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc);
  }

  private long calculate(final PuzzleContext pc) {
    final int differences = pc.getInt("differences");
    return il.groupsAsObjects(pc, group -> group.stream()
                                                .map(line -> line.codePoints()
                                                                 .toArray())
                                                .toArray(int[][]::new)) //
             .stream()
             .mapToLong(grid -> score(grid, differences))
             .sum();
  }

  /** Determine the score of a board where there are a specific number of differences required in a reflection. */
  private long score(final int[][] grid, final int differences) {
    for (int y = 0; y < grid.length - 1; ++y) {
      if (isReflectedHorizontally(grid, y, differences)) {
        return 100 * (y + 1);
      }
    }
    for (int x = 0; x < grid[0].length - 1; ++x) {
      if (isReflectedVertically(grid, x, differences)) {
        return x + 1;
      }
    }
    throw new IllegalArgumentException("Bad input: no reflection");
  }

  /** Get whether the board is reflected vertically between this column and the one to its right. */
  boolean isReflectedVertically(final int[][] grid, final int x, final int differences) {
    int actualDifferences = 0;
    for (int x0 = x, x1 = x + 1; (x0 >= 0) && (x1 < grid[0].length); --x0, ++x1) {
      for (int y = 0; y < grid.length; ++y) {
        if (grid[y][x0] != grid[y][x1]) {
          ++actualDifferences;
        }
        if (actualDifferences > differences) {
          return false;
        }
      }
    }
    return actualDifferences == differences;
  }

  /** Get whether the board is reflected horizontally between this row and the one below it. */
  boolean isReflectedHorizontally(final int[][] grid, final int y, final int differences) {
    int actualDifferences = 0;
    for (int y0 = y, y1 = y + 1; (y0 >= 0) && (y1 < grid.length); --y0, ++y1) {
      for (int x = 0; x < grid[y].length; ++x) {
        if (grid[y0][x] != grid[y1][x]) {
          ++actualDifferences;
        }
        if (actualDifferences > differences) {
          return false;
        }
      }
    }
    return actualDifferences == differences;
  }

}
