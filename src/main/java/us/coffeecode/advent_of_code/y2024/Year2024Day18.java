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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2024, day = 18)
@Component
public class Year2024Day18 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int[][] grid = makeGrid(pc);
    getWalls(pc).stream()
                .limit(pc.getInt("inputLimit"))
                .forEach(p -> p.set(grid, '#'));
    return calculate(grid);
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    final List<Point2D> walls = getWalls(pc);
    int low = 0;
    int high = walls.size();
    int search = (high >> 1);
    while (true) {
      // Check for a solution at the current search location.
      final int[][] grid = makeGrid(pc);
      walls.stream()
           // "search" is a 0-based index, but here we need the number of elements which is off by one.
           .limit(search + 1)
           .forEach(p -> p.set(grid, '#'));
      final long score = calculate(grid);

      // Update lower or upper bound depending on the success of the current search element.
      if (score > 0) {
        low = search;
      }
      else {
        high = search;
      }

      // Bounds converged as far as they are able: found the solution.
      if (low == high - 1) {
        return toString(walls.get(high));
      }

      // Update next search location to the middle of the unknown range
      search = ((low + high) >> 1);
    }
  }

  /** Convert a point to the string format specified in the requirements. */
  private String toString(final Point2D p) {
    return p.getX() + "," + p.getY();
  }

  /**
   * Get the minimum steps needed to find the end of the maze, or a negative number of steps if a solution is not
   * possible. Uses Dijkstra algorithm because it is BFS and never revisits nodes. Technically this is slightly modified
   * Dijkstra because it does not score each node, instead keeping it in the state object.
   */
  private long calculate(final int[][] grid) {
    final Point2D start = new Point2D(0, 0);
    final Point2D end = new Point2D(grid[0].length - 1, grid.length - 1);
    final Queue<State> queue = new PriorityQueue<>();
    queue.add(new State(start, 0));
    final Set<Point2D> visited = new HashSet<>();
    while (!queue.isEmpty()) {
      final State currentState = queue.poll();
      // It is possible for two states to attempt to visit the same location and sit in the queue together.
      if (visited.contains(currentState.location)) {
        continue;
      }
      // No other path can beat this one, and we do not care about ties.
      else if (currentState.location.equals(end)) {
        return currentState.score;
      }
      visited.add(currentState.location);
      final int nextScore = currentState.score + 1;
      for (final Point2D neighbor : currentState.location.getCardinalNeighbors()) {
        // If the neighbor is not visited and not a wall, add it as a future state to process.
        if (!visited.contains(neighbor) && neighbor.isIn(grid) && (neighbor.get(grid) == '.')) {
          queue.add(new State(neighbor, nextScore));
        }
      }
    }
    return Long.MIN_VALUE;
  }

  /** Make a new grid for the given puzzle context. */
  private int[][] makeGrid(final PuzzleContext pc) {
    final int size = pc.getInt("size");
    final int[][] grid = new int[size][size];
    for (int y = 0; y < grid.length; ++y) {
      for (int x = 0; x < grid[y].length; ++x) {
        grid[y][x] = '.';
      }
    }
    return grid;
  }

  /** Get an immutable list of wall locations in input order. */
  private List<Point2D> getWalls(final PuzzleContext pc) {
    return il.linesAsObjects(pc, this::makePoint)
             .stream()
             .toList();
  }

  /** Convert a single line of input into a Point2D object. */
  private Point2D makePoint(final String line) {
    final int[] coordinates = Arrays.stream(SPLIT.split(line))
                                    .mapToInt(Integer::valueOf)
                                    .toArray();
    return new Point2D(coordinates[0], coordinates[1]);
  }

  /** Represents one state of maze traversal. */
  private record State(Point2D location, int score)
  implements Comparable<State> {

    @Override
    public int compareTo(final State o) {
      return Integer.compare(score, o.score);
    }
  }

  /** Split a single line of input into X,Y coordinates. */
  private static final Pattern SPLIT = Pattern.compile(",");
}
