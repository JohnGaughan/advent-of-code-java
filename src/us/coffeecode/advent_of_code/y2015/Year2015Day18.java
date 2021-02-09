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

import java.nio.file.Files;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/18">Year 2015, day 18</a>. This problem is an implementation of
 * <a href="https://en.wikipedia.org/wiki/Conway's_Game_of_Life">Conway's Game of Life</a>. Part one runs the simulation
 * for 100 iterations. Part two changes the rules slightly by stipulating that the four cells in the corners always
 * live.
 * </p>
 * <p>
 * Another fairly straightforward implementation. Set up the grid, then run the simulation for a hundred generations.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2015Day18 {

  /** Number of iterations to run. */
  private final int ITERATIONS = 100;

  /** Array size. Used for clarity, and input is enforced to be a grid this size. */
  private final int SIZE = 100;

  public int calculatePart1() {
    boolean[][] grid = getInput();
    for (int i = 0; i < ITERATIONS; ++i) {
      grid = transform(grid, false);
    }
    return countLightsOn(grid);
  }

  public int calculatePart2() {
    boolean[][] grid = getInput();
    // Need to start with the corners alive.
    grid[0][0] = true;
    grid[0][SIZE - 1] = true;
    grid[SIZE - 1][0] = true;
    grid[SIZE - 1][SIZE - 1] = true;
    for (int i = 0; i < ITERATIONS; ++i) {
      grid = transform(grid, true);
    }
    return countLightsOn(grid);
  }

  /** Transform the cells into a new generation. */
  private boolean[][] transform(final boolean[][] input, final boolean cornersAlwaysLive) {
    final boolean[][] output = new boolean[SIZE][SIZE];
    for (int i = 0; i < SIZE; ++i) {
      for (int j = 0; j < SIZE; ++j) {
        // Count neighbors.
        final boolean hasAbove = i > 0;
        final boolean hasBelow = i < SIZE - 1;
        final boolean hasLeft = j > 0;
        final boolean hasRight = j < SIZE - 1;
        int neighbors = 0;
        if (hasAbove) {
          if (hasLeft && input[i - 1][j - 1]) {
            ++neighbors;
          }
          if (input[i - 1][j]) {
            ++neighbors;
          }
          if (hasRight && input[i - 1][j + 1]) {
            ++neighbors;
          }
        }
        if (hasLeft && input[i][j - 1]) {
          ++neighbors;
        }
        if (hasRight && input[i][j + 1]) {
          ++neighbors;
        }
        if (hasBelow) {
          if (hasLeft && input[i + 1][j - 1]) {
            ++neighbors;
          }
          if (input[i + 1][j]) {
            ++neighbors;
          }
          if (hasRight && input[i + 1][j + 1]) {
            ++neighbors;
          }
        }
        if (input[i][j]) {
          output[i][j] = neighbors == 2 || neighbors == 3;
        }
        else {
          output[i][j] = neighbors == 3;
        }
      }
    }
    if (cornersAlwaysLive) {
      output[0][0] = true;
      output[0][SIZE - 1] = true;
      output[SIZE - 1][0] = true;
      output[SIZE - 1][SIZE - 1] = true;
    }
    return output;
  }

  /** Count the number of cells currently alive. */
  private int countLightsOn(final boolean[][] input) {
    int result = 0;
    for (final boolean[] row : input) {
      for (final boolean light : row) {
        if (light) {
          ++result;
        }
      }
    }
    return result;
  }

  /** Get the input data for this solution. */
  private boolean[][] getInput() {
    try {
      final boolean[][] grid =
        Files.readAllLines(Utils.getInput(2015, 18)).stream().map(s -> getInput(s)).toArray(boolean[][]::new);
      if (grid.length != SIZE) {
        throw new IllegalArgumentException("Not a " + SIZE + "x" + SIZE + " grid");
      }
      return grid;
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /** Parse a single line into a boolean array. */
  private boolean[] getInput(final String line) {
    if (line.length() != SIZE) {
      throw new IllegalArgumentException("Not a " + SIZE + "x" + SIZE + " grid");
    }
    final boolean[] result = new boolean[SIZE];
    for (int i = 0; i < result.length; ++i) {
      result[i] = line.codePointAt(i) == '#';
    }
    return result;
  }

}
