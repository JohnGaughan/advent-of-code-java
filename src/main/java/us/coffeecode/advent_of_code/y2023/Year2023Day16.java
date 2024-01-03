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

import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2023, day = 16, title = "The Floor Will Be Lava")
@Component
public class Year2023Day16 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final int[][] grid = il.linesAs2dIntArray(pc, s -> s.codePoints().toArray());
    return countEnergized(grid, new Point2D(0, 0), Direction.E);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int[][] grid = il.linesAs2dIntArray(pc, s -> s.codePoints().toArray());
    long answer = Long.MIN_VALUE;
    for (int y = 0; y < grid.length; ++y) {
      answer = Math.max(answer, countEnergized(grid, new Point2D(0, y), Direction.E));
      answer = Math.max(answer, countEnergized(grid, new Point2D(grid[y].length - 1, y), Direction.W));
    }
    for (int x = 0; x < grid[0].length; ++x) {
      answer = Math.max(answer, countEnergized(grid, new Point2D(x, 0), Direction.S));
      answer = Math.max(answer, countEnergized(grid, new Point2D(x, grid.length - 1), Direction.N));
    }
    return answer;
  }

  /** Count the energized grid locations given the starting location and beam direction. Does not modify the grid. */
  private long countEnergized(final int[][] grid, final Point2D startLocation, final Direction startDirection) {
    final Deque<Visit> queue = new LinkedList<>();
    // These are different because one point may be visited from different directions. Both will energize the point, but
    // they count as different visits for pruning the search space because they have different outcomes.
    final Set<Point2D> energized = new HashSet<>();
    final Set<Visit> visited = new HashSet<>();

    // Inside the main loop we only ever enqueue decision points, that is, not '.'. The start location may be a '.', so
    // find the first decision point.
    {
      Point2D p = startLocation;
      // The order of adding to energized is reversed here compared to the main loop, and this is deliberate.
      energized.add(p);
      while (p.isIn(grid) && (p.get(grid) == '.')) {
        p = startDirection.next(p);
        energized.add(p);
      }
      if (p.isIn(grid)) {
        for (final Direction newDir : REDIRECTIONS.get(new RedirectKey(startDirection, p.get(grid)))) {
          final Visit visitedKey = new Visit(newDir, p);
          visited.add(visitedKey);
          queue.offer(new Visit(newDir, newDir.next(p)));
        }
      }
    }
    while (!queue.isEmpty()) {
      final Visit current = queue.removeFirst();
      Point2D p = current.p;
      while (p.isIn(grid) && (p.get(grid) == '.')) {
        energized.add(p);
        p = current.dir.next(p);
      }
      if (!p.isIn(grid)) {
        continue;
      }
      energized.add(p);
      // Decision time!
      for (final Direction newDir : REDIRECTIONS.get(new RedirectKey(current.dir, p.get(grid)))) {
        final Visit visitedKey = new Visit(newDir, p);
        if (!visited.contains(visitedKey)) {
          visited.add(visitedKey);
          queue.offer(new Visit(newDir, newDir.next(p)));
        }
      }
    }
    return energized.size();
  }

  private enum Direction {

    N(0, -1),
    E(1, 0),
    S(0, 1),
    W(-1, 0);

    final int dx, dy;

    private Direction(final int _dx, final int _dy) {
      dx = _dx;
      dy = _dy;
    }

    /** Advance the given point in the direction indicated by this object. */
    Point2D next(final Point2D current) {
      return current.add(dx, dy);
    }
  }

  /** Represents one visit of a beam at a point, traveling in a specific direction. */
  private record Visit(Direction dir, Point2D p) {}

  /** Key used to map an incoming direction and grid character to one or more outgoing directions. */
  private record RedirectKey(Direction dir, int ch) {}

  /** Given a combination of direction and grid character, map it to one or two directions to redirect the beam. */
  private static final Map<RedirectKey, Iterable<Direction>> REDIRECTIONS;

  static {
    final Iterable<Direction> N = List.of(Direction.N);
    final Iterable<Direction> E = List.of(Direction.E);
    final Iterable<Direction> S = List.of(Direction.S);
    final Iterable<Direction> W = List.of(Direction.W);

    final Iterable<Direction> NS = List.of(Direction.N, Direction.S);
    final Iterable<Direction> EW = List.of(Direction.E, Direction.W);

    final Map<RedirectKey, Iterable<Direction>> map = new HashMap<>(32);

    map.put(new RedirectKey(Direction.N, '|'), N);
    map.put(new RedirectKey(Direction.N, '-'), EW);
    map.put(new RedirectKey(Direction.N, '/'), E);
    map.put(new RedirectKey(Direction.N, '\\'), W);

    map.put(new RedirectKey(Direction.E, '|'), NS);
    map.put(new RedirectKey(Direction.E, '-'), E);
    map.put(new RedirectKey(Direction.E, '/'), N);
    map.put(new RedirectKey(Direction.E, '\\'), S);

    map.put(new RedirectKey(Direction.S, '|'), S);
    map.put(new RedirectKey(Direction.S, '-'), EW);
    map.put(new RedirectKey(Direction.S, '/'), W);
    map.put(new RedirectKey(Direction.S, '\\'), E);

    map.put(new RedirectKey(Direction.W, '|'), NS);
    map.put(new RedirectKey(Direction.W, '-'), W);
    map.put(new RedirectKey(Direction.W, '/'), S);
    map.put(new RedirectKey(Direction.W, '\\'), N);

    REDIRECTIONS = Collections.unmodifiableMap(map);
  }
}
