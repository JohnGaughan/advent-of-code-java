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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 21)
@Component
public final class Year2017Day21 {

  private static final boolean[][] INITIAL_STATE =
    new boolean[][] { { false, false, true }, { true, false, true }, { false, true, true } };

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
    final int iterations = pc.getInt("iterations");
    final Mappings maps = getInput(pc);
    boolean[][] grid = INITIAL_STATE;
    for (int i = 0; i < iterations; ++i) {
      grid = iterate(grid, maps);
    }
    return pixelCount(grid);
  }

  private long pixelCount(final boolean[][] grid) {
    long count = 0;
    for (boolean[] line : grid) {
      for (boolean element : line) {
        if (element) {
          ++count;
        }
      }
    }
    return count;
  }

  private boolean[][] iterate(final boolean[][] grid, final Mappings maps) {
    final int preSize;
    final int postSize;
    final Map<BooleanGrid, BooleanGrid> mappings;
    if (grid.length % 2 == 0) {
      preSize = 2;
      postSize = 3;
      mappings = maps.twos;
    }
    else {
      preSize = 3;
      postSize = 4;
      mappings = maps.threes;
    }
    final int newDimensions = grid.length * postSize / preSize;
    final boolean[][] newGrid = new boolean[newDimensions][newDimensions];
    // Iterate over each section of the input grid.
    for (int y_input = 0; y_input < grid.length; y_input += preSize) {
      final int y_output = y_input * postSize / preSize;
      for (int x_input = 0; x_input < grid[y_input].length; x_input += preSize) {
        final BooleanGrid key = new BooleanGrid(grid, y_input, x_input, preSize);
        final BooleanGrid value = mappings.get(key);
        final int x_output = x_input * postSize / preSize;
        for (int y = 0; y < postSize; ++y) {
          for (int x = 0; x < postSize; ++x) {
            newGrid[y_output + y][x_output + x] = value.grid[y][x];
          }
        }
      }
    }
    return newGrid;
  }

  private static final Pattern LINE_SEPARATOR = Pattern.compile(" => ");

  private static final Pattern GRID_SEPARATOR = Pattern.compile("/");

  /** Get the input data for this solution. */
  private Mappings getInput(final PuzzleContext pc) {
    final Mappings mappings = new Mappings();
    for (final String line : il.lines(pc)) {
      final String[] halves = LINE_SEPARATOR.split(line);
      boolean[][] key = toGrid(halves[0]);
      final BooleanGrid value = new BooleanGrid(toGrid(halves[1]));
      final Map<BooleanGrid, BooleanGrid> map = (key.length == 2) ? mappings.twos : mappings.threes;
      // There are eight variations on the input grid, including the original.
      map.put(new BooleanGrid(key), value);
      key = transpose(key);
      map.put(new BooleanGrid(key), value);
      key = flip(key);
      map.put(new BooleanGrid(key), value);
      key = transpose(key);
      map.put(new BooleanGrid(key), value);
      key = flip(key);
      map.put(new BooleanGrid(key), value);
      key = transpose(key);
      map.put(new BooleanGrid(key), value);
      key = flip(key);
      map.put(new BooleanGrid(key), value);
      key = transpose(key);
      map.put(new BooleanGrid(key), value);
    }
    return mappings;
  }

  private static boolean[][] toGrid(final String lineHalf) {
    final String[] lines = GRID_SEPARATOR.split(lineHalf);
    final boolean[][] grid = new boolean[lines.length][lines.length];
    for (int y = 0; y < lines.length; ++y) {
      for (int x = 0; x < lines.length; ++x) {
        if (lines[y].codePointAt(x) == '#') {
          grid[y][x] = true;
        }
      }
    }
    return grid;
  }

  private boolean[][] flip(final boolean[][] array) {
    final boolean[][] output = new boolean[array.length][array.length];
    for (int y = 0; y < array.length; ++y) {
      final int y1 = array.length - y - 1;
      for (int x = 0; x < array[y].length; ++x) {
        output[y1][x] = array[y][x];
      }
    }
    return output;
  }

  private boolean[][] transpose(final boolean[][] array) {
    final boolean[][] output = new boolean[array.length][array.length];
    for (int y = 0; y < array.length; ++y) {
      final int y1 = array.length - y - 1;
      for (int x = 0; x < array[y].length; ++x) {
        final int x1 = array[y].length - x - 1;
        // X and Y are switched here because that's what a matrix transpose does.
        output[y1][x1] = array[x][y];
      }
    }
    return output;
  }

  private static final class BooleanGrid {

    final boolean[][] grid;

    private Integer hashCode;

    BooleanGrid(final boolean[][] _grid) {
      grid = _grid;
    }

    BooleanGrid(final boolean[][] _grid, final int y1, final int x1, final int size) {
      grid = new boolean[size][size];
      for (int y = 0; y < grid.length; ++y) {
        for (int x = 0; x < grid[y].length; ++x) {
          grid[y][x] = _grid[y1 + y][x1 + x];
        }
      }
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (obj instanceof BooleanGrid o) {
        return Arrays.deepEquals(grid, o.grid);
      }
      return false;
    }

    @Override
    public int hashCode() {
      if (hashCode == null) {
        hashCode = Integer.valueOf(Arrays.deepHashCode(grid));
      }
      return hashCode.intValue();
    }

  }

  private static final class Mappings {

    // Make these hash maps large. Memory is cheap, and they seem to have enough collisions to impact performance when
    // their sizes are just big enough.

    final Map<BooleanGrid, BooleanGrid> twos = new HashMap<>(128);

    final Map<BooleanGrid, BooleanGrid> threes = new HashMap<>(2_048);
  }

}
