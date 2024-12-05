/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2022  John Gaughan
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
package us.coffeecode.advent_of_code.y2022;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2022, day = 23, title = "Unstable Diffusion")
@Component
public class Year2022Day23 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Queue<Direction> directions = Direction.get();
    final Collection<Point2D> elves = getInput(pc);
    for (int i = 0; i < 10; ++i) {
      round(elves, directions);
      directions.add(directions.remove());
    }
    int minX = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (final Point2D p : elves) {
      minX = Math.min(minX, p.getX());
      maxX = Math.max(maxX, p.getX());
      minY = Math.min(minY, p.getY());
      maxY = Math.max(maxY, p.getY());
    }
    return (maxX - minX + 1) * (maxY - minY + 1) - elves.size();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Queue<Direction> directions = Direction.get();
    final Collection<Point2D> elves = getInput(pc);
    long i = 1;
    while (i < 2_000) {
      if (round(elves, directions)) {
        return i;
      }
      ++i;
      directions.add(directions.remove());
    }
    throw new RuntimeException("Could not find the answer in a reasonable amount of time");
  }

  /** Perform one round of the elf diffusion dance. */
  private boolean round(final Collection<Point2D> elves, final Iterable<Direction> directions) {
    // Maps target points to the elves that want to move there.
    final Map<Point2D, Point2D> moves = new HashMap<>(elves.size());

    // First half of the round: record where each elf wants to move.
    for (final Point2D elf : elves) {
      // If no neighbors, don't do anything.
      if (!hasIntersection(elf.getAllNeighbors(), elves)) {
        continue;
      }

      // See if it should move. If so, get its destination and record the move.
      for (final Direction dir : directions) {
        if (dir.test(elf, elves)) {
          final Point2D destination = dir.getMove(elf);
          // Only two elves can possibly move to the same destination. If the second elf want to move to the common
          // destination, simply remove that map entry and nobody will move there.
          if (moves.containsKey(destination)) {
            moves.remove(destination);
          }
          else {
            moves.put(destination, elf);
          }
          break;
        }
      }
    }

    // Second half of the round: move the elves.
    for (final var entry : moves.entrySet()) {
      elves.add(entry.getKey());
      elves.remove(entry.getValue());
    }
    return moves.isEmpty();
  }

  /** Determine if the collections have any intersection. */
  private static boolean hasIntersection(final Collection<Point2D> small, final Collection<Point2D> large) {
    for (final Point2D p : small) {
      if (large.contains(p)) {
        return true;
      }
    }
    return false;
  }

  private Collection<Point2D> getInput(final PuzzleContext pc) {
    final Collection<Point2D> elves = new HashSet<>(1 << 12);
    final List<String> input = il.lines(pc);
    for (int y = 0; y < input.size(); ++y) {
      final int[] line = input.get(y)
                              .codePoints()
                              .toArray();
      for (int x = 0; x < line.length; ++x) {
        if (line[x] == '#') {
          elves.add(new Point2D(x, y));
        }
      }
    }
    return elves;
  }

  private static enum Direction {

    NORTH(0, -1),
    SOUTH(0, 1),
    WEST(-1, 0),
    EAST(1, 0);

    public static Queue<Direction> get() {
      final Queue<Direction> result = new LinkedList<>();
      result.add(NORTH);
      result.add(SOUTH);
      result.add(WEST);
      result.add(EAST);
      return result;
    }

    private final int dx;

    private final int dy;

    private Direction(final int _dx, final int _dy) {
      dx = _dx;
      dy = _dy;
    }

    boolean test(final Point2D p, final Collection<Point2D> points) {
      final Point2D target = p.add(dx, dy);
      if (points.contains(target)) {
        return false;
      }
      // Moving north or south: look west and east.
      else if (dx == 0) {
        if (hasIntersection(target.getXNeighbors(), points)) {
          return false;
        }
      }
      // Moving west or east: look north and south.
      else if (hasIntersection(target.getYNeighbors(), points)) {
        return false;
      }
      return true;
    }

    Point2D getMove(final Point2D p) {
      return p.add(dx, dy);
    }
  }
}
