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
package us.coffeecode.advent_of_code.y2018;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2018, day = 11)
@Component
public final class Year2018Day11 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public String calculatePart1(final PuzzleContext pc) {
    final int[] results = calculate(getInput(pc), 3);
    return results[0] + "," + results[1];
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    final int[][] grid = getInput(pc);
    int[] best = new int[] { Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE };
    for (int i = 1; i < SIZE; ++i) {
      int[] result = calculate(grid, i);
      if (result[2] > best[2]) {
        best = result;
      }
    }
    return best[0] + "," + best[1] + "," + best[3];
  }

  private int[] calculate(final int[][] grid, final int size) {
    int[] best = new int[] { Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE };
    for (int y = 0; y < grid.length - size - 1; ++y) {
      for (int x = 0; x < grid[y].length - size - 1; ++x) {
        int sum = grid[y][x] - grid[y + size][x] - grid[y][x + size] + grid[y + size][x + size];
        if (sum > best[2]) {
          best = new int[] { x + 1, y + 1, sum, size };
        }
      }
    }
    return best;
  }

  private static final int SIZE = 301;

  /** Get the input data for this solution. */
  private int[][] getInput(final PuzzleContext pc) {
    final int gridSerial = il.fileAsInt(pc);
    final int[][] grid = new int[SIZE][SIZE];
    for (int y = grid.length - 2; y >= 0; --y) {
      for (int x = grid[y].length - 2; x >= 0; --x) {
        final int rackId = x + 11;
        final int v = ((((rackId * (y + 1) + gridSerial) * rackId) / 100) % 10) - 5;
        grid[y][x] = v + grid[y + 1][x] + grid[y][x + 1] - grid[y + 1][x + 1];
      }
    }
    return grid;
  }

}
