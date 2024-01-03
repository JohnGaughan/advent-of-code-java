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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 3, title = "Spiral Memory")
@Component
public final class Year2017Day03 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final long input = il.fileAsLong(pc);
    long layer = (long) Math.ceil(0.5d * Math.sqrt(input) - 0.5);
    // Catch divide by zero in example input where we get the first digit in the spiral.
    final long remainder = (layer == 0) ? 0 : (input - 1) % (layer << 1);
    return layer + Math.abs(remainder - layer);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final long input = il.fileAsLong(pc);
    final int GRID_SIZE = 11;
    final long[][] grid = new long[GRID_SIZE][GRID_SIZE];
    final int[][] offsets = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };
    int x = GRID_SIZE / 2;
    int y = GRID_SIZE / 2;
    // Fill in the first two elements
    grid[y][x] = 1;
    ++x;
    grid[y][x] = 1;
    // Now iterate around the spiral.
    while (true) {
      // Advance to the next cell. To go up, the cell to the left must be filled and the up cell must be unfilled.
      if ((grid[y][x - 1] > 0) && (grid[y - 1][x] == 0)) {
        --y;
      }
      // To go left, the cell to the bottom must be filled and the left cell unfilled.
      else if ((grid[y + 1][x] > 0) && (grid[y][x - 1] == 0)) {
        --x;
      }
      // To go down, the cell to the right must be filled and the down cell unfilled.
      else if ((grid[y][x + 1] > 0) && (grid[y + 1][x] == 0)) {
        ++y;
      }
      // Otherwise, go right - this also covers starting a new row.
      else {
        ++x;
      }

      // Add up all neighbors into this cell - unfilled neighbors will be zero, which is okay.
      for (final int[] offset : offsets) {
        grid[y][x] += (grid[y + offset[0]][x + offset[1]]);
      }

      // If this value is great than the input, it is the answer.
      if (grid[y][x] > input) {
        return grid[y][x];
      }
    }
    // The above loop never terminates. Instead, it either returns a value or throws an array index exception.
  }

}
