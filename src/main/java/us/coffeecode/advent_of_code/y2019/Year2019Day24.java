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
package us.coffeecode.advent_of_code.y2019;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2019, day = 24)
@Component
public final class Year2019Day24 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    boolean[][] grid = il.linesAs2dBooleanArray(pc, '#');
    final Set<Long> cache = new HashSet<>();
    for (int i = 0; i < 1 << 10; ++i) {
      final Long score = biodiversityRating(grid);
      if (cache.contains(score)) {
        return score.longValue();
      }
      cache.add(score);
      grid = generation(grid);
    }
    return 0;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int iterations = pc.getInt("iterations");
    Map<Integer, boolean[][]> grids = new HashMap<>();
    grids.put(Integer.valueOf(0), il.linesAs2dBooleanArray(pc, '#'));
    for (int i = 1; i <= iterations; ++i) {
      grids = generation(grids, i);
    }
    return countBugs(grids);
  }

  private Long biodiversityRating(final boolean[][] grid) {
    long score = 0;
    for (int y = SIZE - 1; y >= 0; --y) {
      for (int x = SIZE - 1; x >= 0; --x) {
        score <<= 1;
        if (grid[y][x]) {
          ++score;
        }
      }
    }
    return Long.valueOf(score);
  }

  private long countBugs(final Map<Integer, boolean[][]> grids) {
    long count = 0;
    for (final boolean[][] grid : grids.values()) {
      for (final boolean[] row : grid) {
        for (final boolean b : row) {
          if (b) {
            ++count;
          }
        }
      }
    }
    return count;
  }

  /** Calculate a new generation for a single, non-nested grid (part one). */
  private boolean[][] generation(final boolean[][] grid) {
    final boolean[][] newGrid = new boolean[SIZE][SIZE];
    for (int y = 0; y < SIZE; ++y) {
      for (int x = 0; x < SIZE; ++x) {
        int neighbors = 0;
        if ((y > 0) && grid[y - 1][x]) {
          ++neighbors;
        }
        if ((y < SIZE - 1) && grid[y + 1][x]) {
          ++neighbors;
        }
        if ((x > 0) && grid[y][x - 1]) {
          ++neighbors;
        }
        if ((x < SIZE - 1) && grid[y][x + 1]) {
          ++neighbors;
        }
        if (grid[y][x] && (neighbors != 1)) {
          newGrid[y][x] = false;
        }
        else if (!grid[y][x] && ((neighbors == 1) || (neighbors == 2))) {
          newGrid[y][x] = true;
        }
        else {
          newGrid[y][x] = grid[y][x];
        }
      }
    }
    return newGrid;
  }

  /** Calculate a new generation for all grid levels, including new one that need to be added. */
  private Map<Integer, boolean[][]> generation(final Map<Integer, boolean[][]> grids, final int time) {
    final Map<Integer, boolean[][]> newGrids = new HashMap<>();
    for (int i = -time; i <= time; ++i) {
      newGrids.put(Integer.valueOf(i), getNextGeneration(grids, i));
    }
    return newGrids;
  }

  /** Calculate a new generation of a single grid level. */
  private boolean[][] getNextGeneration(final Map<Integer, boolean[][]> grids, final int level) {
    final boolean[][] newGrid = new boolean[SIZE][SIZE];

    final Integer outsideKey = Integer.valueOf(level + 1);
    final Integer insideKey = Integer.valueOf(level - 1);
    final Integer thisKey = Integer.valueOf(level);

    // Get the count of boundary cells for the grid outside this one.
    int[] outerCount = new int[4];
    if (grids.containsKey(outsideKey)) {
      final boolean[][] outsideGrid = grids.get(outsideKey);
      if (outsideGrid[CENTER - 1][CENTER]) {
        outerCount[UP] = 1;
      }
      if (outsideGrid[CENTER + 1][CENTER]) {
        outerCount[DOWN] = 1;
      }
      if (outsideGrid[CENTER][CENTER - 1]) {
        outerCount[LEFT] = 1;
      }
      if (outsideGrid[CENTER][CENTER + 1]) {
        outerCount[RIGHT] = 1;
      }
    }

    // Get the count of boundary cells for the grid inside this one.
    int[] innerCount = new int[4];
    if (grids.containsKey(insideKey)) {
      final boolean[][] insideGrid = grids.get(insideKey);
      for (int x = 0; x < SIZE; ++x) {
        if (insideGrid[0][x]) {
          ++innerCount[UP];
        }
        if (insideGrid[SIZE - 1][x]) {
          ++innerCount[DOWN];
        }
      }
      for (int y = 0; y < SIZE; ++y) {
        if (insideGrid[y][0]) {
          ++innerCount[LEFT];
        }
        if (insideGrid[y][SIZE - 1]) {
          ++innerCount[RIGHT];
        }
      }
    }

    // Iterating on a level that already exists.
    if (grids.containsKey(thisKey)) {
      final boolean[][] grid = grids.get(thisKey);
      for (int y = 0; y < SIZE; ++y) {
        for (int x = 0; x < SIZE; ++x) {
          // Always skip the center cell which represents an inner grid.
          if ((y == CENTER) && (x == CENTER)) {
            continue;
          }
          int neighbors = 0;

          // Count up neighbors.
          if (y == 0) {
            neighbors += outerCount[UP];
          }
          else if ((y == CENTER + 1) && (x == CENTER)) {
            neighbors += innerCount[DOWN];
          }
          else if (grid[y - 1][x]) {
            ++neighbors;
          }

          // Count down neighbors.
          if (y == SIZE - 1) {
            neighbors += outerCount[DOWN];
          }
          else if ((y == CENTER - 1) && (x == CENTER)) {
            neighbors += innerCount[UP];
          }
          else if (grid[y + 1][x]) {
            ++neighbors;
          }

          // Count left neighbors.
          if (x == 0) {
            neighbors += outerCount[LEFT];
          }
          else if ((y == CENTER) && (x == CENTER + 1)) {
            neighbors += innerCount[RIGHT];
          }
          else if (grid[y][x - 1]) {
            ++neighbors;
          }

          // Count right neighbors.
          if (x == SIZE - 1) {
            neighbors += outerCount[RIGHT];
          }
          else if ((y == CENTER) && (x == CENTER - 1)) {
            neighbors += innerCount[LEFT];
          }
          else if (grid[y][x + 1]) {
            ++neighbors;
          }

          // Set this cell.
          if (grid[y][x] && (neighbors != 1)) {
            newGrid[y][x] = false;
          }
          else if (!grid[y][x] && ((neighbors == 1) || (neighbors == 2))) {
            newGrid[y][x] = true;
          }
          else {
            newGrid[y][x] = grid[y][x];
          }
        }
      }
    }

    // Creating a new outside level
    else if (grids.containsKey(insideKey)) {
      if ((innerCount[UP] == 1) || (innerCount[UP] == 2)) {
        newGrid[CENTER - 1][CENTER] = true;
      }
      if ((innerCount[DOWN] == 1) || (innerCount[DOWN] == 2)) {
        newGrid[CENTER + 1][CENTER] = true;
      }
      if ((innerCount[LEFT] == 1) || (innerCount[LEFT] == 2)) {
        newGrid[CENTER][CENTER - 1] = true;
      }
      if ((innerCount[RIGHT] == 1) || (innerCount[RIGHT] == 2)) {
        newGrid[CENTER][CENTER + 1] = true;
      }
    }

    // Creating a new inside level
    else {
      // Corners do not need special handling due to one or two neighbors needing to be bugs.
      if (outerCount[UP] == 1) {
        for (int x = 0; x < SIZE; ++x) {
          newGrid[0][x] = true;
        }
      }
      if (outerCount[DOWN] == 1) {
        for (int x = 0; x < SIZE; ++x) {
          newGrid[SIZE - 1][x] = true;
        }
      }
      if (outerCount[LEFT] == 1) {
        for (int y = 0; y < SIZE; ++y) {
          newGrid[y][0] = true;
        }
      }
      if (outerCount[RIGHT] == 1) {
        for (int y = 0; y < SIZE; ++y) {
          newGrid[y][SIZE - 1] = true;
        }
      }
    }

    return newGrid;
  }

  private static final int SIZE = 5;

  private static final int CENTER = SIZE >> 1;

  private static final int UP = 0;

  private static final int DOWN = 1;

  private static final int LEFT = 2;

  private static final int RIGHT = 3;
}
