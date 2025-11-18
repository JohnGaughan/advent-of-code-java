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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2024, day = 6)
@Component
public class Year2024Day06 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input input = getInput(pc);
    final Collection<Visit> visits = simulate(input).visits;
    return visits.stream()
                 .map(v -> v.location)
                 .distinct()
                 .count();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Input input = getInput(pc);
    final SimulationResult baseline = simulate(input);
    final Set<Point2D> candidates = baseline.visits.stream()
                                                   .map(v -> v.location)
                                                   .filter(p -> !input.location.equals(p))
                                                   .collect(Collectors.toSet());
    long loops = 0;
    for (final Point2D candidate : candidates) {
      candidate.set(input.grid, OBSTRUCTION);
      final SimulationResult result = simulate(input);
      candidate.set(input.grid, EMPTY);
      if (result.reason == EndCondition.LOOP) {
        ++loops;
      }
    }
    return loops;
  }

  private SimulationResult simulate(final Input input) {
    // Size it large to avoid numerous, expensive rehashing operations
    final Set<Visit> visits = new HashSet<>(2 << 12);
    Visit current = new Visit(input.location, input.facing);
    // Keep going until the guard exits the area or enters a loop.
    while (true) {
      if (!current.location.isIn(input.grid)) {
        return new SimulationResult(visits, EndCondition.EXIT);
      }
      else if (visits.contains(current)) {
        return new SimulationResult(visits, EndCondition.LOOP);
      }
      visits.add(current);
      final Point2D nextLocation = current.location.add(current.facing.getDelta());
      if (nextLocation.isIn(input.grid) && (nextLocation.get(input.grid) == OBSTRUCTION)) {
        current = new Visit(current.location, Facing.next(current.facing));
      }
      else {
        current = new Visit(nextLocation, current.facing);
      }
    }
  }

  private Input getInput(final PuzzleContext pc) {
    final int[][] grid = il.linesAsCodePoints(pc);
    Point2D start = null;
    Facing startDir = null;
    outer: for (int y = 0; y < grid.length; ++y) {
      for (int x = 0; x < grid[y].length; ++x) {
        if ((grid[y][x] != EMPTY) && (grid[y][x] != OBSTRUCTION)) {
          start = new Point2D(x, y);
          startDir = Facing.valueOf(grid[y][x]);
          grid[y][x] = EMPTY;
          break outer;
        }
      }
    }
    if (start == null) {
      throw new IllegalStateException("Starting point not found");
    }
    return new Input(grid, start, startDir);
  }

  private static final int EMPTY = '.';

  private static final int OBSTRUCTION = '#';

  private enum Facing {

    NORTH('^', new Point2D(0, -1)),
    SOUTH('v', new Point2D(0, 1)),
    EAST('>', new Point2D(1, 0)),
    WEST('<', new Point2D(-1, 0));

    private final int ch;

    private final Point2D delta;

    public static final Facing valueOf(final int codePoint) {
      for (final Facing f : values()) {
        if (f.ch == codePoint) {
          return f;
        }
      }
      throw new IllegalArgumentException("Invalid character [" + Character.toString(codePoint) + "]");
    }

    public static final Facing next(final Facing f) {
      if (f == NORTH) {
        return EAST;
      }
      else if (f == EAST) {
        return SOUTH;
      }
      else if (f == SOUTH) {
        return WEST;
      }
      return NORTH;
    }

    private Facing(final int _ch, final Point2D _delta) {
      ch = _ch;
      delta = _delta;
    }

    public Point2D getDelta() {
      return delta;
    }
  }

  private enum EndCondition {
    EXIT,
    LOOP;
  }

  private record SimulationResult(Set<Visit> visits, EndCondition reason) {}

  private record Visit(Point2D location, Facing facing) {}

  private record Input(int[][] grid, Point2D location, Facing facing) {}
}
