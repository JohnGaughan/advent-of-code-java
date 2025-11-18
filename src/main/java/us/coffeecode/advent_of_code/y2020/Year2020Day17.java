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
package us.coffeecode.advent_of_code.y2020;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2020, day = 17)
@Component
public final class Year2020Day17 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int iterations = 6;
    boolean[][][] state = getInput(pc, iterations);
    for (int i = 0; i < iterations; ++i) {
      state = transform(state);
    }
    return countActiveCells(state);
  }

  /** Perform one iteration and return the result. */
  private boolean[][][] transform(final boolean[][][] state) {
    final boolean[][][] newState = new boolean[state.length][state[0].length][state[0][0].length];
    for (int x = 0; x < state.length; ++x) {
      for (int y = 0; y < state[x].length; ++y) {
        for (int z = 0; z < state[x][y].length; ++z) {
          final int neighbors = countActiveNeighbors(state, x, y, z);
          if (state[x][y][z]) {
            newState[x][y][z] = neighbors == 2 || neighbors == 3;
          }
          else if (neighbors == 3) {
            newState[x][y][z] = true;
          }
        }
      }
    }
    return newState;
  }

  /** Count the number of active neighbors in all three dimensions. */
  private int countActiveNeighbors(final boolean[][][] state, final int x, final int y, final int z) {
    int count = 0;
    final boolean x_up = x < state.length - 1;
    final boolean x_down = x > 0;
    final boolean y_up = y < state[0].length - 1;
    final boolean y_down = y > 0;
    final boolean z_up = z < state[0][0].length - 1;
    final boolean z_down = z > 0;

    // Six adjacent locations differ in one dimension
    if (x_up && state[x + 1][y][z]) {
      ++count;
    }
    if (x_down && state[x - 1][y][z]) {
      ++count;
    }
    if (y_up && state[x][y + 1][z]) {
      ++count;
    }
    if (y_down && state[x][y - 1][z]) {
      ++count;
    }
    if (z_up && state[x][y][z + 1]) {
      ++count;
    }
    if (z_down && state[x][y][z - 1]) {
      ++count;
    }

    // Twelve adjacent locations differ in two dimensions
    if (x_up && y_up && state[x + 1][y + 1][z]) {
      ++count;
    }
    if (x_down && y_up && state[x - 1][y + 1][z]) {
      ++count;
    }
    if (x_up && y_down && state[x + 1][y - 1][z]) {
      ++count;
    }
    if (x_down && y_down && state[x - 1][y - 1][z]) {
      ++count;
    }
    if (x_up && z_up && state[x + 1][y][z + 1]) {
      ++count;
    }
    if (x_down && z_up && state[x - 1][y][z + 1]) {
      ++count;
    }
    if (x_up && z_down && state[x + 1][y][z - 1]) {
      ++count;
    }
    if (x_down && z_down && state[x - 1][y][z - 1]) {
      ++count;
    }
    if (y_up && z_up && state[x][y + 1][z + 1]) {
      ++count;
    }
    if (y_down && z_up && state[x][y - 1][z + 1]) {
      ++count;
    }
    if (y_up && z_down && state[x][y + 1][z - 1]) {
      ++count;
    }
    if (y_down && z_down && state[x][y - 1][z - 1]) {
      ++count;
    }

    // Eight adjacent locations differ in three dimensions
    if (x_up && y_up && z_up && state[x + 1][y + 1][z + 1]) {
      ++count;
    }
    if (x_down && y_up && z_up && state[x - 1][y + 1][z + 1]) {
      ++count;
    }
    if (x_up && y_down && z_up && state[x + 1][y - 1][z + 1]) {
      ++count;
    }
    if (x_down && y_down && z_up && state[x - 1][y - 1][z + 1]) {
      ++count;
    }
    if (x_up && y_up && z_down && state[x + 1][y + 1][z - 1]) {
      ++count;
    }
    if (x_down && y_up && z_down && state[x - 1][y + 1][z - 1]) {
      ++count;
    }
    if (x_up && y_down && z_down && state[x + 1][y - 1][z - 1]) {
      ++count;
    }
    if (x_down && y_down && z_down && state[x - 1][y - 1][z - 1]) {
      ++count;
    }

    return count;
  }

  private long countActiveCells(final boolean[][][] state) {
    long count = 0;
    for (int x = 0; x < state.length; ++x) {
      for (int y = 0; y < state[x].length; ++y) {
        for (int z = 0; z < state[x][y].length; ++z) {
          if (state[x][y][z]) {
            ++count;
          }
        }
      }
    }
    return count;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int iterations = 6;
    boolean[][][] state3d = getInput(pc, iterations);
    // Convert the 3D space into a 4D space.
    boolean[][][][] state = new boolean[state3d.length][state3d[0].length][state3d[0][0].length][state3d[0][0].length];
    for (int x = 0; x < state3d.length; ++x) {
      for (int y = 0; y < state3d[x].length; ++y) {
        for (int z = 0; z < state3d[x][y].length; ++z) {
          state[x][y][z][z] = state3d[x][y][z];
        }
      }
    }
    for (int i = 0; i < iterations; ++i) {
      state = transform(state);
    }
    return countActiveCells(state);
  }

  /** Perform one iteration and return the result. */
  private boolean[][][][] transform(final boolean[][][][] state) {
    final boolean[][][][] newState = new boolean[state.length][state[0].length][state[0][0].length][state[0][0][0].length];
    for (int x = 0; x < state.length; ++x) {
      for (int y = 0; y < state[x].length; ++y) {
        for (int z = 0; z < state[x][y].length; ++z) {
          for (int w = 0; w < state[x][y][z].length; ++w) {
            final int neighbors = countActiveNeighbors(state, x, y, z, w);
            if (state[x][y][z][w]) {
              newState[x][y][z][w] = neighbors == 2 || neighbors == 3;
            }
            else if (neighbors == 3) {
              newState[x][y][z][w] = true;
            }
          }
        }
      }
    }
    return newState;
  }

  /** Count the number of active neighbors in all three dimensions. */
  private int countActiveNeighbors(final boolean[][][][] state, final int x, final int y, final int z, final int w) {
    int count = 0;
    final boolean x_up = x < state.length - 1;
    final boolean x_down = x > 0;
    final boolean y_up = y < state[0].length - 1;
    final boolean y_down = y > 0;
    final boolean z_up = z < state[0][0].length - 1;
    final boolean z_down = z > 0;
    final boolean w_up = w < state[0][0][0].length - 1;
    final boolean w_down = w > 0;

    // Eight adjacent locations differ in one dimension
    if (x_up && state[x + 1][y][z][w]) {
      ++count;
    }
    if (x_down && state[x - 1][y][z][w]) {
      ++count;
    }

    if (y_up && state[x][y + 1][z][w]) {
      ++count;
    }
    if (y_down && state[x][y - 1][z][w]) {
      ++count;
    }

    if (z_up && state[x][y][z + 1][w]) {
      ++count;
    }
    if (z_down && state[x][y][z - 1][w]) {
      ++count;
    }

    if (w_up && state[x][y][z][w + 1]) {
      ++count;
    }
    if (w_down && state[x][y][z][w - 1]) {
      ++count;
    }

    // Twenty-four adjacent locations differ in two dimensions
    if (x_up && y_up && state[x + 1][y + 1][z][w]) {
      ++count;
    }
    if (x_down && y_up && state[x - 1][y + 1][z][w]) {
      ++count;
    }
    if (x_up && y_down && state[x + 1][y - 1][z][w]) {
      ++count;
    }
    if (x_down && y_down && state[x - 1][y - 1][z][w]) {
      ++count;
    }

    if (x_up && z_up && state[x + 1][y][z + 1][w]) {
      ++count;
    }
    if (x_down && z_up && state[x - 1][y][z + 1][w]) {
      ++count;
    }
    if (x_up && z_down && state[x + 1][y][z - 1][w]) {
      ++count;
    }
    if (x_down && z_down && state[x - 1][y][z - 1][w]) {
      ++count;
    }

    if (x_up && w_up && state[x + 1][y][z][w + 1]) {
      ++count;
    }
    if (x_down && w_up && state[x - 1][y][z][w + 1]) {
      ++count;
    }
    if (x_up && w_down && state[x + 1][y][z][w - 1]) {
      ++count;
    }
    if (x_down && w_down && state[x - 1][y][z][w - 1]) {
      ++count;
    }

    if (y_up && z_up && state[x][y + 1][z + 1][w]) {
      ++count;
    }
    if (y_down && z_up && state[x][y - 1][z + 1][w]) {
      ++count;
    }
    if (y_up && z_down && state[x][y + 1][z - 1][w]) {
      ++count;
    }
    if (y_down && z_down && state[x][y - 1][z - 1][w]) {
      ++count;
    }

    if (y_up && w_up && state[x][y + 1][z][w + 1]) {
      ++count;
    }
    if (y_down && w_up && state[x][y - 1][z][w + 1]) {
      ++count;
    }
    if (y_up && w_down && state[x][y + 1][z][w - 1]) {
      ++count;
    }
    if (y_down && w_down && state[x][y - 1][z][w - 1]) {
      ++count;
    }

    if (z_up && w_up && state[x][y][z + 1][w + 1]) {
      ++count;
    }
    if (z_down && w_up && state[x][y][z - 1][w + 1]) {
      ++count;
    }
    if (z_up && w_down && state[x][y][z + 1][w - 1]) {
      ++count;
    }
    if (z_down && w_down && state[x][y][z - 1][w - 1]) {
      ++count;
    }

    // Thirty-two adjacent locations differ in three dimensions
    if (x_up && y_up && z_up && state[x + 1][y + 1][z + 1][w]) {
      ++count;
    }
    if (x_down && y_up && z_up && state[x - 1][y + 1][z + 1][w]) {
      ++count;
    }
    if (x_up && y_down && z_up && state[x + 1][y - 1][z + 1][w]) {
      ++count;
    }
    if (x_down && y_down && z_up && state[x - 1][y - 1][z + 1][w]) {
      ++count;
    }
    if (x_up && y_up && z_down && state[x + 1][y + 1][z - 1][w]) {
      ++count;
    }
    if (x_down && y_up && z_down && state[x - 1][y + 1][z - 1][w]) {
      ++count;
    }
    if (x_up && y_down && z_down && state[x + 1][y - 1][z - 1][w]) {
      ++count;
    }
    if (x_down && y_down && z_down && state[x - 1][y - 1][z - 1][w]) {
      ++count;
    }

    if (x_up && y_up && w_up && state[x + 1][y + 1][z][w + 1]) {
      ++count;
    }
    if (x_down && y_up && w_up && state[x - 1][y + 1][z][w + 1]) {
      ++count;
    }
    if (x_up && y_down && w_up && state[x + 1][y - 1][z][w + 1]) {
      ++count;
    }
    if (x_down && y_down && w_up && state[x - 1][y - 1][z][w + 1]) {
      ++count;
    }
    if (x_up && y_up && w_down && state[x + 1][y + 1][z][w - 1]) {
      ++count;
    }
    if (x_down && y_up && w_down && state[x - 1][y + 1][z][w - 1]) {
      ++count;
    }
    if (x_up && y_down && w_down && state[x + 1][y - 1][z][w - 1]) {
      ++count;
    }
    if (x_down && y_down && w_down && state[x - 1][y - 1][z][w - 1]) {
      ++count;
    }

    if (x_up && z_up && w_up && state[x + 1][y][z + 1][w + 1]) {
      ++count;
    }
    if (x_down && z_up && w_up && state[x - 1][y][z + 1][w + 1]) {
      ++count;
    }
    if (x_up && z_down && w_up && state[x + 1][y][z - 1][w + 1]) {
      ++count;
    }
    if (x_down && z_down && w_up && state[x - 1][y][z - 1][w + 1]) {
      ++count;
    }
    if (x_up && z_up && w_down && state[x + 1][y][z + 1][w - 1]) {
      ++count;
    }
    if (x_down && z_up && w_down && state[x - 1][y][z + 1][w - 1]) {
      ++count;
    }
    if (x_up && z_down && w_down && state[x + 1][y][z - 1][w - 1]) {
      ++count;
    }
    if (x_down && z_down && w_down && state[x - 1][y][z - 1][w - 1]) {
      ++count;
    }

    if (y_up && z_up && w_up && state[x][y + 1][z + 1][w + 1]) {
      ++count;
    }
    if (y_down && z_up && w_up && state[x][y - 1][z + 1][w + 1]) {
      ++count;
    }
    if (y_up && z_down && w_up && state[x][y + 1][z - 1][w + 1]) {
      ++count;
    }
    if (y_down && z_down && w_up && state[x][y - 1][z - 1][w + 1]) {
      ++count;
    }
    if (y_up && z_up && w_down && state[x][y + 1][z + 1][w - 1]) {
      ++count;
    }
    if (y_down && z_up && w_down && state[x][y - 1][z + 1][w - 1]) {
      ++count;
    }
    if (y_up && z_down && w_down && state[x][y + 1][z - 1][w - 1]) {
      ++count;
    }
    if (y_down && z_down && w_down && state[x][y - 1][z - 1][w - 1]) {
      ++count;
    }

    // Sixteen adjacent locations differ in three dimensions
    if (x_up && y_up && z_up && w_up && state[x + 1][y + 1][z + 1][w + 1]) {
      ++count;
    }
    if (x_down && y_up && z_up && w_up && state[x - 1][y + 1][z + 1][w + 1]) {
      ++count;
    }
    if (x_up && y_down && z_up && w_up && state[x + 1][y - 1][z + 1][w + 1]) {
      ++count;
    }
    if (x_down && y_down && z_up && w_up && state[x - 1][y - 1][z + 1][w + 1]) {
      ++count;
    }
    if (x_up && y_up && z_down && w_up && state[x + 1][y + 1][z - 1][w + 1]) {
      ++count;
    }
    if (x_down && y_up && z_down && w_up && state[x - 1][y + 1][z - 1][w + 1]) {
      ++count;
    }
    if (x_up && y_down && z_down && w_up && state[x + 1][y - 1][z - 1][w + 1]) {
      ++count;
    }
    if (x_down && y_down && z_down && w_up && state[x - 1][y - 1][z - 1][w + 1]) {
      ++count;
    }

    if (x_up && y_up && z_up && w_down && state[x + 1][y + 1][z + 1][w - 1]) {
      ++count;
    }
    if (x_down && y_up && z_up && w_down && state[x - 1][y + 1][z + 1][w - 1]) {
      ++count;
    }
    if (x_up && y_down && z_up && w_down && state[x + 1][y - 1][z + 1][w - 1]) {
      ++count;
    }
    if (x_down && y_down && z_up && w_down && state[x - 1][y - 1][z + 1][w - 1]) {
      ++count;
    }
    if (x_up && y_up && z_down && w_down && state[x + 1][y + 1][z - 1][w - 1]) {
      ++count;
    }
    if (x_down && y_up && z_down && w_down && state[x - 1][y + 1][z - 1][w - 1]) {
      ++count;
    }
    if (x_up && y_down && z_down && w_down && state[x + 1][y - 1][z - 1][w - 1]) {
      ++count;
    }
    if (x_down && y_down && z_down && w_down && state[x - 1][y - 1][z - 1][w - 1]) {
      ++count;
    }

    return count;
  }

  private int countActiveCells(final boolean[][][][] state) {
    int count = 0;
    for (int x = 0; x < state.length; ++x) {
      for (int y = 0; y < state[x].length; ++y) {
        for (int z = 0; z < state[x][y].length; ++z) {
          for (int w = 0; w < state[x][y][z].length; ++w) {
            if (state[x][y][z][w]) {
              ++count;
            }
          }
        }
      }
    }
    return count;
  }

  /** Get the input data for this solution. */
  private boolean[][][] getInput(final PuzzleContext pc, final int iterations) {
    final List<String> lines = il.lines(pc);
    final int bufferSize = 2 * iterations;
    final int width = lines.getFirst()
                           .length()
      + bufferSize;
    final int height = lines.size() + bufferSize;
    final int depth = 1 + bufferSize;
    final boolean[][][] output = new boolean[width][height][depth];
    for (int x = 0; x < lines.size(); ++x) {
      final String line = lines.get(x);
      for (int y = 0; y < line.length(); ++y) {
        int ch = line.codePointAt(y);
        output[x + iterations][y + iterations][iterations] = (ch == '#');
      }
    }
    return output;
  }

}
