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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2024, day = 20)
@Component
public class Year2024Day20 {

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

  /** Calculate the answer based on the parameters stored in the parameters.properties file. */
  private long calculate(final PuzzleContext pc) {
    final int skip = pc.getInt("skip");
    final int threshold = pc.getInt("threshold");
    final List<Point2D> path = getInput(pc);
    long answer = 0;
    for (int i = 0; i < path.size() - threshold; ++i) {
      answer += countCheats(path, i, skip, threshold);
    }
    return answer;
  }

  /**
   * Count the number of useful cheats starting at the location in the path identified by the provided location index.
   * Cheats may skip steps up to the "skip" parameter, and must have net savings (including time spent skipping) of at
   * least the "threshold" parameter.
   */
  private long countCheats(final List<Point2D> path, final int locationIndex, final int skip, final int threshold) {
    long result = 0;
    final Point2D location = path.get(locationIndex);
    // Examine only those points in the path that have the potential to save time.
    for (int cheatIndex = locationIndex + threshold; cheatIndex < path.size(); ++cheatIndex) {
      final Point2D cheatLocation = path.get(cheatIndex);
      final int distance = location.getManhattanDistance(cheatLocation);
      // If the distance is at most the number of allowable skip squares
      if (distance <= skip) {
        // Time saving starts with the difference between indices, which is the normal time to advance to the second
        // point. Then subtract the Cartesian distance, representing time spent cheating to reach that location. This is
        // the net savings.
        final long saved = (cheatIndex - locationIndex - distance);
        // If the net savings are at least the desired threshold, then this is a useful cheat.
        if (saved >= threshold) {
          ++result;
        }
      }
    }
    return result;
  }

  /**
   * Get the input as a list of points starting with the start of the "maze" and the final point being its end. The time
   * required to reach a point is that point's index in the list.
   */
  private List<Point2D> getInput(final PuzzleContext pc) {
    final int[][] grid = il.linesAsCodePoints(pc);
    final List<Point2D> path = new ArrayList<>(10_000);
    outer: for (int y = 0; y < grid.length; ++y) {
      for (int x = 0; x < grid[y].length; ++x) {
        if (grid[y][x] == 'S') {
          final Point2D start = new Point2D(x, y);
          path.add(start);
          break outer;
        }
      }
    }
    if (path.isEmpty()) {
      throw new IllegalStateException("Missing path start");
    }

    while (true) {
      final Point2D location = path.getLast();
      if (location.get(grid) == 'E') {
        break;
      }
      final Point2D previous = ((path.size() == 1) ? null : path.get(path.size() - 2));
      for (final Point2D neighbor : location.getCardinalNeighbors()) {
        if (!neighbor.equals(previous) && (neighbor.get(grid) != '#')) {
          path.add(neighbor);
          break;
        }
      }
    }
    return path;
  }
}
