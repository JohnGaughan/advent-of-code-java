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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2023, day = 11, title = "Cosmic Expansion")
@Component
public class Year2023Day11 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, 2);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, 1_000_000);
  }

  /** Perform the calculation using the specified expansion factor. */
  private long calculate(final PuzzleContext pc, final long factor) {
    final int[][] array = il.linesAs2dIntArray(pc, s -> s.codePoints()
                                                         .toArray());
    final boolean[] rows = expandRows(array);
    final boolean[] cols = expandColumns(array);
    final List<Point2D> galaxies = new ArrayList<>(1024);
    for (int y = 0; y < array.length; ++y) {
      for (int x = 0; x < array[y].length; ++x) {
        if (array[y][x] == '#') {
          galaxies.add(new Point2D(x, y));
        }
      }
    }
    long sum = 0;
    for (int i = 0; i < galaxies.size() - 1; ++i) {
      for (int j = i + 1; j < galaxies.size(); ++j) {
        final Point2D a = galaxies.get(i);
        final Point2D b = galaxies.get(j);
        sum += a.getManhattanDistance(b) + getExpansion(array, rows, cols, factor, a, b);
      }
    }
    return sum;
  }

  /** Get the total expansion between two points across both rows and columns. */
  private long getExpansion(final int[][] board, final boolean[] rows, final boolean[] cols, final long factor, final Point2D a, final Point2D b) {
    long expansion = 0;
    for (int x = Math.min(a.getX(), b.getX()) + 1; x < Math.max(a.getX(), b.getX()); ++x) {
      if (cols[x]) {
        ++expansion;
      }
    }
    for (int y = Math.min(a.getY(), b.getY()) + 1; y < Math.max(a.getY(), b.getY()); ++y) {
      if (rows[y]) {
        ++expansion;
      }
    }
    return expansion * (factor - 1);
  }

  /** Determine which row indices need to be expanded. */
  private boolean[] expandRows(final int[][] array) {
    final boolean[] expanded = new boolean[array.length];
    for (int y = 0; y < array.length; ++y) {
      if (Arrays.stream(array[y])
                .noneMatch(i -> i == '#')) {
        expanded[y] = true;
      }
    }
    return expanded;
  }

  /** Determine which column indices need to be expanded. */
  private boolean[] expandColumns(final int[][] array) {
    final boolean[] expanded = new boolean[array[0].length];
    for (int x = 0; x < array[0].length; ++x) {
      boolean found = false;
      for (int y = 0; y < array.length; ++y) {
        if (array[y][x] == '#') {
          found = true;
          break;
        }
      }
      if (!found) {
        expanded[x] = true;
      }
    }
    return expanded;
  }

}
