/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2024  John Gaughan
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
package us.coffeecode.advent_of_code.y2024;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2024, day = 4)
@Component
public class Year2024Day04 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int[][] grid = il.linesAsCodePoints(pc);
    long count = 0;
    for (int y = 0; y < grid.length; ++y) {
      for (int x = 0; x < grid[y].length; ++x) {
        for (final Point2D dir : DIRECTIONS) {
          if (matches_part1(grid, new Point2D(x, y), dir)) {
            ++count;
          }
        }
      }
    }
    return count;
  }

  private boolean matches_part1(final int[][] grid, final Point2D p0, final Point2D dir) {
    Point2D p = p0;
    for (int i = 0; i < NEEDLE_P1.length; ++i) {
      if (!p.isIn(grid) || (p.get(grid) != NEEDLE_P1[i])) {
        return false;
      }
      p = p.add(dir);
    }
    return true;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int[][] grid = il.linesAsCodePoints(pc);
    long count = 0;
    for (int y = 1; y < grid.length - 1; ++y) {
      for (int x = 1; x < grid[y].length - 1; ++x) {
        if (matches_part2(grid, new Point2D(x, y))) {
          ++count;
        }
      }
    }
    return count;
  }

  private boolean matches_part2(final int[][] grid, final Point2D pt) {
    if (pt.get(grid) == 'A') {
      final Point2D nw = pt.add(NW);
      final Point2D se = pt.add(SE);
      if (((nw.get(grid) == 'M') && (se.get(grid) == 'S')) || ((nw.get(grid) == 'S') && (se.get(grid) == 'M'))) {
        final Point2D ne = pt.add(NE);
        final Point2D sw = pt.add(SW);
        return ((ne.get(grid) == 'M') && (sw.get(grid) == 'S')) || ((ne.get(grid) == 'S') && (sw.get(grid) == 'M'));
      }
    }
    return false;
  }

  private static final Point2D N = new Point2D(0, -1);

  private static final Point2D S = new Point2D(0, 1);

  private static final Point2D E = new Point2D(1, 0);

  private static final Point2D W = new Point2D(-1, 0);

  private static final Point2D NW = new Point2D(-1, -1);

  private static final Point2D NE = new Point2D(1, -1);

  private static final Point2D SW = new Point2D(-1, 1);

  private static final Point2D SE = new Point2D(1, 1);

  private static final Point2D[] DIRECTIONS = new Point2D[] { N, S, E, W, NW, NE, SW, SE };

  private static final int[] NEEDLE_P1 = new int[] { 'X', 'M', 'A', 'S' };
}
