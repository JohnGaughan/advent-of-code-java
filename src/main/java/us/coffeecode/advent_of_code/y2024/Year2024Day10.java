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

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2024, day = 10)
@Component
public class Year2024Day10 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, (a, b, c) -> getMaxHeightReachable(a, b, c).size());
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, (a, b, c) -> countUniqueTrails(a, b, c));
  }

  /** Calculate the answer using the provided scoring function for each trailhead. */
  public long calculate(final PuzzleContext pc, final Score f) {
    final int[][] grid = il.linesAs2dIntArrayFromDigits(pc);
    long score = 0;
    for (int y = 0; y < grid.length; ++y) {
      for (int x = 0; x < grid[y].length; ++x) {
        final Point2D location = new Point2D(x, y);
        if (location.get(grid) == 0) {
          score += f.apply(grid, location, 0);
        }
      }
    }
    return score;
  }

  /** Get the set of unique high points reachable from the given location. */
  private Set<Point2D> getMaxHeightReachable(final int[][] grid, final Point2D location, final int level) {
    final Set<Point2D> reachable = new HashSet<>();
    final int nextLevel = level + 1;
    for (final Point2D nextLocation : location.getCardinalNeighbors()) {
      if (nextLocation.isIn(grid) && (nextLocation.get(grid) == nextLevel)) {
        if (nextLevel == 9) {
          reachable.add(nextLocation);
        }
        else {
          reachable.addAll(getMaxHeightReachable(grid, nextLocation, nextLevel));
        }
      }
    }
    return reachable;
  }

  /** Count the number of unique trails from the given location to the highest points. */
  private long countUniqueTrails(final int[][] grid, final Point2D location, final int level) {
    long trails = 0;
    final int nextLevel = level + 1;
    for (final Point2D nextLocation : location.getCardinalNeighbors()) {
      if (nextLocation.isIn(grid) && (nextLocation.get(grid) == nextLevel)) {
        if (nextLevel == 9) {
          ++trails;
        }
        else {
          trails += countUniqueTrails(grid, nextLocation, nextLevel);
        }
      }
    }
    return trails;
  }

  @FunctionalInterface
  private static interface Score {

    long apply(final int[][] grid, final Point2D location, final int level);
  }
}
