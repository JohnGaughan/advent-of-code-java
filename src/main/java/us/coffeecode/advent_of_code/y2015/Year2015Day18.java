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

@AdventOfCodeSolution(year = 2015, day = 18, title = "Like a GIF For Your Yard")
@Component
public final class Year2015Day18 {

  private static final int TRUTH = '#';

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    boolean[][] grid = il.linesAs2dBooleanArray(pc, TRUTH);
    for (int i = 0; i < pc.getInt("steps"); ++i) {
      grid = transform(grid, false);
    }
    return countLightsOn(grid);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    boolean[][] grid = il.linesAs2dBooleanArray(pc, TRUTH);
    // Need to start with the corners alive.
    grid[0][0] = true;
    grid[0][grid[0].length - 1] = true;
    grid[grid.length - 1][0] = true;
    grid[grid.length - 1][grid[0].length - 1] = true;
    for (int i = 0; i < pc.getInt("steps"); ++i) {
      grid = transform(grid, true);
    }
    return countLightsOn(grid);
  }

  /** Transform the cells into a new generation. */
  private boolean[][] transform(final boolean[][] input, final boolean cornersAlwaysLive) {
    final boolean[][] output = new boolean[input.length][input[0].length];
    for (int y = 0; y < input.length; ++y) {
      for (int x = 0; x < input[y].length; ++x) {
        // Count neighbors.
        final boolean hasAbove = y > 0;
        final boolean hasBelow = y < input.length - 1;
        final boolean hasLeft = x > 0;
        final boolean hasRight = x < input[y].length - 1;
        int neighbors = 0;
        if (hasAbove) {
          if (hasLeft && input[y - 1][x - 1]) {
            ++neighbors;
          }
          if (input[y - 1][x]) {
            ++neighbors;
          }
          if (hasRight && input[y - 1][x + 1]) {
            ++neighbors;
          }
        }
        if (hasLeft && input[y][x - 1]) {
          ++neighbors;
        }
        if (hasRight && input[y][x + 1]) {
          ++neighbors;
        }
        if (hasBelow) {
          if (hasLeft && input[y + 1][x - 1]) {
            ++neighbors;
          }
          if (input[y + 1][x]) {
            ++neighbors;
          }
          if (hasRight && input[y + 1][x + 1]) {
            ++neighbors;
          }
        }
        if (input[y][x]) {
          output[y][x] = neighbors == 2 || neighbors == 3;
        }
        else {
          output[y][x] = neighbors == 3;
        }
      }
    }
    if (cornersAlwaysLive) {
      output[0][0] = true;
      output[0][output[0].length - 1] = true;
      output[output.length - 1][0] = true;
      output[output.length - 1][output[0].length - 1] = true;
    }
    return output;
  }

  /** Count the number of cells currently alive. */
  private long countLightsOn(final boolean[][] input) {
    long result = 0;
    for (final boolean[] row : input) {
      for (final boolean light : row) {
        if (light) {
          ++result;
        }
      }
    }
    return result;
  }

}
