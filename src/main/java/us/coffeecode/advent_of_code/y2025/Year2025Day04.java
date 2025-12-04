/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2025  John Gaughan
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
package us.coffeecode.advent_of_code.y2025;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2025, day = 4)
@Component
public class Year2025Day04 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return findAccessible(il.linesAsCodePoints(pc)).size();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    long result = 0;
    final int[][] grid = il.linesAsCodePoints(pc);
    while (true) {
      final Set<Point2D> accessible = findAccessible(grid);
      if (accessible.isEmpty()) {
        break;
      }
      result += accessible.size();
      accessible.stream()
                .forEach(p -> p.set(grid, EMPTY));
    }
    return result;
  }

  /** Process the grid by finding all locations that meet the puzzle criteria. */
  public Set<Point2D> findAccessible(final int[][] grid) {
    final Set<Point2D> accessible = new HashSet<>();
    for (int y = 0; y < grid.length; ++y) {
      for (int x = 0; x < grid[y].length; ++x) {
        final Point2D p = new Point2D(x, y);
        if ((p.get(grid) == PAPER) && (p.getAllNeighbors()
                                        .stream()
                                        .filter(n -> n.isIn(grid) && (n.get(grid) == PAPER))
                                        .count() < 4)) {
          accessible.add(p);
        }
      }
    }
    return accessible;
  }

  /** Designates a grid location occupied by paper. */
  private static final int PAPER = '@';

  /** Designates an unoccupied grid location. */
  private static final int EMPTY = '.';
}
