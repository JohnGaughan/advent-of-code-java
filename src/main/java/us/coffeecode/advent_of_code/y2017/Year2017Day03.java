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

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/3">Year 2017, day 3</a>. This problem concerns itself with spiral squares.
 * Part 1 asks us for a Manhattan distance of the Nth element, where N is the puzzle input. Part 2 asks for the first
 * value greater than N when storing the sums rather than the cell sequence numbers.
 * </p>
 * <p>
 * Part one is the well-defined sequence <a href="https://oeis.org/A214526">A214526</a>. The answer can be calculated
 * directly using the formula in the OEIS.
 * </p>
 * <p>
 * Part two is another sequence, <a href="https://oeis.org/A141481">A141481</a>. However, there is no known formula to
 * calculate the answer directly. Even the OEIS has a formula that iterates instead of calculating directly. The
 * simplest solution is to fill in a matrix and stop once the current value is greater than the input.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day03 {

  public long calculatePart1() {
    final int input = getInput();
    long layer = (long) Math.ceil(0.5d * Math.sqrt(input) - 0.5);
    return layer + Math.abs((input - 1) % (layer << 1) - layer);
  }

  public long calculatePart2() {
    final int input = getInput();
    final int GRID_SIZE = 11;
    final long[][] grid = new long[GRID_SIZE][GRID_SIZE];
    final int[][] offsets =
      new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };
    int x = GRID_SIZE / 2;
    int y = GRID_SIZE / 2;
    // Fill in the first two elements
    grid[y][x] = 1;
    ++x;
    grid[y][x] = 1;
    // Now iterate around the spiral.
    while (true) {
      // Advance to the next cell. To go up, the cell to the left must be filled and the up cell must be unfilled.
      if (grid[y][x - 1] > 0 && grid[y - 1][x] == 0) {
        --y;
      }
      // To go left, the cell to the bottom must be filled and the left cell unfilled.
      else if (grid[y + 1][x] > 0 && grid[y][x - 1] == 0) {
        --x;
      }
      // To go down, the cell to the right must be filled and the down cell unfilled.
      else if (grid[y][x + 1] > 0 && grid[y + 1][x] == 0) {
        ++y;
      }
      // Otherwise, go right - this also covers starting a new row.
      else {
        ++x;
      }

      // Add up all neighbors into this cell - unfilled neighbors will be zero, which is okay.
      for (final int[] offset : offsets) {
        grid[y][x] += grid[y + offset[0]][x + offset[1]];
      }

      // If this value is great than the input, it is the answer.
      if (grid[y][x] > input) {
        return grid[y][x];
      }
    }
    // The above loop never terminates. Instead, it either returns a value or throws an array index exception.
  }

  /** Get the input data for this solution. */
  private int getInput() {
    try {
      return Integer.parseInt(Files.readString(Utils.getInput(2017, 3)).trim());
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
