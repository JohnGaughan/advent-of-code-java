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

import java.nio.file.Files;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2018/day/11">Year 2018, day 11</a>. This problem asks us to calculate values and
 * store them in a grid, then find the square in the grid with the highest sum. Part one uses a fixed size square with
 * length 3, while in part two the length varies.
 * </p>
 * <p>
 * The naive approach is to store the value of each grid element in the grid, then continuously calculate sums and
 * compare. This works okay for part one, but is very inefficient for part two. Instead, a
 * <a href="https://en.wikipedia.org/wiki/Summed-area_table">summed-area table</a> is a much better data structure.
 * Since origin is the upper left of the table, start in the opposite corner and use a table size one row and column too
 * large. Each square is equal to its computed value plus the square to the right and below, minus the square to the
 * diagonal lower right. Each square contains the sum of itself and all squares to the right and below: the part we
 * subtract is an overlapped area, so we need to subtract it once after adding it twice.
 * </p>
 * <p>
 * Now when we want to know the sum of an area of the table, we start with the upper-left square. Take its value and
 * subtract the square to the right just past the square we want, and the same with the square below. Then add in the
 * square to the lower right just past the area we want, since it was subtracted twice. This gives us the sum of the
 * area with three arithmetic operations. This is extremely efficient and allows this program to complete in a very
 * small amount of time.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2018Day11 {

  public String calculatePart1() {
    final int[] results = calculate(getInput(), 3);
    return results[0] + "," + results[1];
  }

  public String calculatePart2() {
    final int[][] grid = getInput();
    int[] best = new int[] { Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE };
    for (int i = 1; i < 301; ++i) {
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

  /** Get the input data for this solution. */
  private int[][] getInput() {
    try {
      final int gridSerial = Integer.parseInt(Files.readString(Utils.getInput(2018, 11)).trim());
      final int[][] grid = new int[301][301];
      for (int y = grid.length - 2; y >= 0; --y) {
        for (int x = grid[y].length - 2; x >= 0; --x) {
          final int rackId = x + 11;
          final int v = (rackId * (y + 1) + gridSerial) * rackId / 100 % 10 - 5;
          grid[y][x] = v + grid[y + 1][x] + grid[y][x + 1] - grid[y + 1][x + 1];
        }
      }
      return grid;
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
