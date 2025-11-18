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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2023, day = 17)
@Component
public class Year2023Day17 {

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
    final int[][] grid = il.linesAs2dIntArrayFromDigits(pc);
    final Queue<Path> paths = new PriorityQueue<>(1 << (grid.length > 50 ? 13 : 9));
    final Map<VisitKey, Long> visited = new HashMap<>(1 << (grid.length > 50 ? 21 : 10));
    final Point2D target = new Point2D(grid[0].length - 1, grid.length - 1);
    final int minTravel = pc.getInt("MinTravel");
    paths.offer(new Path(List.of(new Point2D(0, 0)), null, 0, 0, target, minTravel, pc.getInt("MaxTravel")));
    long best = Long.MAX_VALUE;
    while (!paths.isEmpty()) {
      final Path current = paths.poll();
      if (current.score + target.getManhattanDistance(current.path.getLast()) > best) {
        // This is a dead-end part of the search tree that cannot improve on the best score.
        continue;
      }
      for (final Path next : current.next(grid)) {
        final VisitKey key = new VisitKey(next.path.getLast(), next.direction, next.facingQuantity);
        // Due to path weights, we can stop if this location was already visited using the criteria in the key. At best,
        // the next path can match the previous attempt but cannot do better than it.
        if (!visited.containsKey(key)) {
          visited.put(key, Long.valueOf(next.score));
          paths.offer(next);
        }
        if ((minTravel <= next.facingQuantity) && target.equals(next.path.getLast())) {
          best = Math.min(best, next.score);
        }
      }
    }
    return best;
  }

  /**
   * Key used to identify unique visit states. Unlike normal Dijkstra, we need to know what direction the crucible is
   * facing and how many times it has moved in that direction because it affects how the crucible can move in the
   * future.
   */
  private record VisitKey(Point2D location, Direction facing, int facingQuantity) {}

  /** Direction indicates one way the crucible can face. */
  private static enum Direction {

    N(0, -1),
    E(1, 0),
    S(0, 1),
    W(-1, 0);

    final int dx;

    final int dy;

    private Direction(final int _dx, final int _dy) {
      dx = _dx;
      dy = _dy;
    }
  }

  /**
   * The path stores the points traveled along with the crucible's direction and how many consecutive times it has moved
   * in that direction.
   */
  private record Path(List<Point2D> path, Direction direction, long score, int facingQuantity, Point2D target, int minTravel,
    int maxTravel)
  implements Comparable<Path> {

    /**
     * Get the allowed paths that add on to the current path. This factors in the distance traveled in a straight line,
     * as well as what is in-bounds on the grid.
     */
    Collection<Path> next(final int[][] grid) {
      final Collection<Path> result = new ArrayList<>(3);
      for (final Direction d : Direction.values()) {
        final Point2D next = path.getLast()
                                 .add(d.dx, d.dy);
        // Candidate point must be in the grid, not already on the path, and meet the min/max travel requirements.
        if (isAllowed(d) && next.isIn(grid) && !path.contains(next)) {
          final List<Point2D> newPoints = new ArrayList<>(path.size() + 1);
          newPoints.addAll(path);
          newPoints.add(next);
          final int newFacingQuantity = ((d == direction) ? facingQuantity + 1 : 1);
          result.add(new Path(newPoints, d, score + next.get(grid), newFacingQuantity, target, minTravel, maxTravel));
        }
      }
      return result;
    }

    /**
     * Get whether the specified direction is allowed for the next move. This takes into account the minimum and maximum
     * allowed travel in a straight line.
     */
    private boolean isAllowed(final Direction d) {
      if (direction == null) {
        // First move is always allowed.
        return true;
      }
      else if (direction == d) {
        // Same direction must travel no more than the max after the next move.
        return (facingQuantity + 1) <= maxTravel;
      }
      // Different direction must have already traveled the minimum distance before making the next move.
      return minTravel <= facingQuantity;
    }

    /**
     * Get the weight used to sort this path in a priority queue, with lower values appearing toward the head of the
     * queue.
     */
    private long getWeight() {
      return path.getLast()
                 .getManhattanDistance(target)
        + score;
    }

    @Override
    public int compareTo(final Path o) {
      return Long.compare(getWeight(), o.getWeight());
    }
  }
}
