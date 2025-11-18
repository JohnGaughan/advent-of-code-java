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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2023, day = 14)
@Component
public class Year2023Day14 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int[][] grid = il.linesAsCodePoints(pc);
    north(grid);
    return score(grid);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int cycles = pc.getInt("cycles");
    final int[][] grid = il.linesAsCodePoints(pc);
    final Map<Integer, Integer> cache = new HashMap<>(256);
    for (int i = 0; i < cycles; ++i) {
      cycle(grid);
      final Integer key = hash(grid);
      if (cache.containsKey(key)) {
        final int remaining = (cycles - i - 1) % (i - cache.get(key)
                                                           .intValue());
        for (int j = 0; j < remaining; ++j) {
          cycle(grid);
        }
        break;
      }
      else {
        cache.put(key, Integer.valueOf(i));
      }
    }
    return score(grid);
  }

  /** Calculate the score for the grid. */
  private long score(final int[][] grid) {
    long score = 0;
    for (int y = 0; y < grid.length; ++y) {
      score += (grid.length - y) * Arrays.stream(grid[y])
                                         .filter(i -> i == 'O')
                                         .count();
    }
    return score;
  }

  /** Slightly custom hash code implementation because there are hash collisions with the input data. */
  private Integer hash(final int[][] grid) {
    int hash = 0;
    for (int y = 0; y < grid.length; ++y) {
      for (int x = 0; x < grid[y].length; ++x) {
        hash = hash * 89 + grid[y][x];
      }
    }
    return Integer.valueOf(hash);
  }

  /** Process the grid through one cycle of four shifts. */
  private void cycle(final int[][] grid) {
    north(grid);
    west(grid);
    south(grid);
    east(grid);
  }

  private void north(final int[][] grid) {
    for (int y = 1; y < grid.length; ++y) {
      for (int x = 0; x < grid[y].length; ++x) {
        if (grid[y][x] == 'O') {
          int y_dest = -1;
          for (int y0 = y - 1; y0 >= 0; --y0) {
            if (grid[y0][x] == '.') {
              y_dest = y0;
            }
            else {
              break;
            }
          }
          if (y_dest >= 0) {
            grid[y_dest][x] = 'O';
            grid[y][x] = '.';
          }
        }
      }
    }
  }

  private void west(final int[][] grid) {
    for (int y = 0; y < grid.length; ++y) {
      for (int x = 1; x < grid[y].length; ++x) {
        if (grid[y][x] == 'O') {
          int x_dest = -1;
          for (int x0 = x - 1; x0 >= 0; --x0) {
            if (grid[y][x0] == '.') {
              x_dest = x0;
            }
            else {
              break;
            }
          }
          if (x_dest >= 0) {
            grid[y][x_dest] = 'O';
            grid[y][x] = '.';
          }
        }
      }
    }
  }

  private void south(final int[][] grid) {
    for (int y = grid.length - 2; y >= 0; --y) {
      for (int x = 0; x < grid[y].length; ++x) {
        if (grid[y][x] == 'O') {
          int y_dest = -1;
          for (int y0 = y + 1; y0 < grid.length; ++y0) {
            if (grid[y0][x] == '.') {
              y_dest = y0;
            }
            else {
              break;
            }
          }
          if (y_dest >= 0) {
            grid[y_dest][x] = 'O';
            grid[y][x] = '.';
          }
        }
      }
    }
  }

  private void east(final int[][] grid) {
    for (int y = 0; y < grid.length; ++y) {
      for (int x = grid[y].length - 2; x >= 0; --x) {
        if (grid[y][x] == 'O') {
          int x_dest = -1;
          for (int x0 = x + 1; x0 < grid[y].length; ++x0) {
            if (grid[y][x0] == '.') {
              x_dest = x0;
            }
            else {
              break;
            }
          }
          if (x_dest >= 0) {
            grid[y][x_dest] = 'O';
            grid[y][x] = '.';
          }
        }
      }
    }
  }

}
