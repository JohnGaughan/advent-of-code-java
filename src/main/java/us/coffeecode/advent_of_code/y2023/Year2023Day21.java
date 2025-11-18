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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2023, day = 21)
@Component
public class Year2023Day21 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int[][] grid = il.linesAsCodePoints(pc);
    final int stepTarget = pc.getInt("Steps");
    final Point2D start = getStart(grid);
    return countEndpoints(grid, start, 0, stepTarget);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int[][] grid = il.linesAsCodePoints(pc);
    final Point2D start = getStart(grid);
    final int stepTarget = pc.getInt("Steps");

    final long x = stepTarget / grid.length;
    final long x_minus_1 = x - 1;
    final int r = stepTarget % grid.length;

    // Get the number of locations within the starting grid.
    final long odd = countEndpoints(grid, start, 0, stepTarget);
    final long even = countEndpoints(grid, start, 1, stepTarget);

    final int cardinal_steps = stepTarget - grid.length + 1;
    final long north = countEndpoints(grid, new Point2D(grid[0].length >> 1, grid.length - 1), cardinal_steps, stepTarget);
    final long south = countEndpoints(grid, new Point2D(grid[0].length >> 1, 0), cardinal_steps, stepTarget);
    final long west = countEndpoints(grid, new Point2D(grid[0].length - 1, grid.length >> 1), cardinal_steps, stepTarget);
    final long east = countEndpoints(grid, new Point2D(0, grid.length >> 1), cardinal_steps, stepTarget);

    final int small_steps = cardinal_steps + r + 1;
    final int large_steps = small_steps - grid.length;

    final long nw_small = countEndpoints(grid, new Point2D(grid[0].length - 1, grid.length - 1), small_steps, stepTarget);
    final long nw_large = countEndpoints(grid, new Point2D(grid[0].length - 1, grid.length - 1), large_steps, stepTarget);

    final long ne_small = countEndpoints(grid, new Point2D(0, grid.length - 1), small_steps, stepTarget);
    final long ne_large = countEndpoints(grid, new Point2D(0, grid.length - 1), large_steps, stepTarget);

    final long sw_small = countEndpoints(grid, new Point2D(grid[0].length - 1, 0), small_steps, stepTarget);
    final long sw_large = countEndpoints(grid, new Point2D(grid[0].length - 1, 0), large_steps, stepTarget);

    final long se_small = countEndpoints(grid, new Point2D(0, 0), small_steps, stepTarget);
    final long se_large = countEndpoints(grid, new Point2D(0, 0), large_steps, stepTarget);

    final long small = nw_small + ne_small + sw_small + se_small;
    final long large = nw_large + ne_large + sw_large + se_large;

    return (even * x * x) + (odd * x_minus_1 * x_minus_1) + north + south + west + east + (small * x) + (large * x_minus_1);
  }

  /**
   * Calculate the number of valid endpoints given the starting location, the number of steps already taken, and the
   * maximum number of steps.
   */
  public long countEndpoints(final int[][] grid, final Point2D start, final int stepsSoFar, final int maxSteps) {
    start.set(grid, GARDEN);
    final Set<Point2D> visited = new HashSet<>(1 << 17);
    final Set<Point2D> ends = new HashSet<>(1 << 17);
    final Queue<State> queue = new LinkedList<>();
    queue.offer(new State(start, stepsSoFar));
    while (!queue.isEmpty()) {
      final State current = queue.poll();
      // If the cardinality of the steps taken and total steps is the same, then we are at an end or can come back.
      if ((maxSteps & 1) == (current.stepsTaken & 1)) {
        ends.add(current.location);
      }
      if (current.stepsTaken < maxSteps) {
        for (final Point2D neighbor : current.location.getCardinalNeighbors()) {
          if (neighbor.isIn(grid) && (neighbor.get(grid) == GARDEN)) {
            final State next = new State(neighbor, current.stepsTaken + 1);
            if (!visited.contains(next.location)) {
              visited.add(next.location);
              queue.offer(next);
            }
          }
        }
      }
    }
    return ends.size();
  }

  private record State(Point2D location, int stepsTaken) {}

  private Point2D getStart(final int[][] grid) {
    for (int y = 0; y < grid.length; ++y) {
      for (int x = 0; x < grid[y].length; ++x) {
        if (grid[y][x] == START) {
          return new Point2D(x, y);
        }
      }
    }
    throw new IllegalArgumentException();
  }

  private static final int GARDEN = '.';

  private static final int START = 'S';
}
