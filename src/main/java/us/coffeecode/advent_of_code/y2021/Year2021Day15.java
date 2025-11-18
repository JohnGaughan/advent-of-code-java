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
package us.coffeecode.advent_of_code.y2021;

import java.util.PriorityQueue;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2021, day = 15)
@Component
public final class Year2021Day15 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return getRisk(il.linesAs2dIntArrayFromDigits(pc));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int map[][] = il.linesAs2dIntArrayFromDigits(pc);

    // Expand the map per the instructions first.

    final int EXPAND = 5;

    final int[][] expandedMap = new int[map.length * EXPAND][map[0].length * EXPAND];

    for (int y0 = 0; y0 < EXPAND; ++y0) {
      final int y1 = y0 * map.length;
      for (int x0 = 0; x0 < EXPAND; ++x0) {
        final int x1 = x0 * map[0].length;
        final int increment = y0 + x0;
        for (int y = 0; y < map.length; ++y) {
          for (int x = 0; x < map[y].length; ++x) {
            int value = increment + map[y][x];
            while (value > 9) {
              value -= 9;
            }
            expandedMap[y1 + y][x1 + x] = value;
          }
        }
      }
    }

    return getRisk(expandedMap);
  }

  private long getRisk(final int[][] map) {
    // Initialize cumulative risk map, which also serves to track unvisited nodes. Use an unachievable risk metric to
    // indicate unvisited.
    final long[][] risk = new long[map.length][map[0].length];
    for (int y = 0; y < risk.length; ++y) {
      for (int x = 0; x < risk[y].length; ++x) {
        risk[y][x] = Long.MAX_VALUE;
      }
    }

    // Set the starting risk conditions.
    risk[0][0] = 0;
    risk[0][1] = map[0][1];
    risk[1][0] = map[1][0];

    final Point2D goal = new Point2D(map[0].length - 1, map.length - 1);

    // Priority queue always returns the square with the lowest cumulative risk, using whatever square is closer to the
    // goal as a tie breaker. This means that we always update the best candidate at any given step. Once we find the
    // goal, we do not need to check that a second path is better: at best, it can tie.
    final Queue<Path> queue = new PriorityQueue<>();
    queue.add(new Path(new Point2D(1, 0), map[0][1]));
    queue.add(new Path(new Point2D(0, 1), map[1][0]));

    while (!queue.isEmpty()) {
      // Get the next best path
      final Path current = queue.remove();

      // Try moving in all four directions.
      for (final Point2D neighbor : current.end.getCardinalNeighbors()) {
        if (neighbor.equals(goal)) {
          // Found the best path!
          return current.risk + neighbor.get(map);
        }
        // Validate that this neighbor is a valid coordinate.
        else if (neighbor.isIn(map)) {
          final long nextRisk = current.risk + neighbor.get(map);
          // If we already visited this location with a lower score, let this path die out.
          if (nextRisk < neighbor.get(risk)) {
            neighbor.set(risk, nextRisk);
            queue.add(new Path(neighbor, nextRisk));
          }
        }
      }
    }

    return 0;
  }

  private record Path(Point2D end, long risk)
  implements Comparable<Path> {

    @Override
    public int compareTo(final Path o) {
      // The best candidate at any step has the lowest risk, and is the closest if there is a tie. The priority queue
      // will always select the path that is potentially the best, although future branches might not be.
      if (risk == o.risk) {
        return Integer.compare(end.getManhattanDistance(), o.end.getManhattanDistance());
      }
      return Long.compare(risk, o.risk);
    }
  }

}
