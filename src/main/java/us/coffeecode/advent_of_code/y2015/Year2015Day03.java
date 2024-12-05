/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2020  John Gaughan
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
package us.coffeecode.advent_of_code.y2015;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2015, day = 3, title = "Perfectly Spherical Houses in a Vacuum")
@Component
public final class Year2015Day03 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Set<Point2D> visited = new HashSet<>();
    int x = 0;
    int y = 0;

    // Visit the first house.
    visited.add(new Point2D(x, y));

    // Now travel around and visit other houses.
    for (final Direction d : il.fileAsObjectsFromCodePoints(pc, Direction::valueOf)) {
      if (Direction.UP == d) {
        ++x;
      }
      else if (Direction.DOWN == d) {
        --x;
      }
      else if (Direction.LEFT == d) {
        --y;
      }
      else if (Direction.RIGHT == d) {
        ++y;
      }
      visited.add(new Point2D(x, y));
    }

    return visited.size();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Set<Point2D> visited = new HashSet<>();
    final int x[] = new int[2];
    final int y[] = new int[2];
    int visitor = 0;

    // Visit the first house.
    visited.add(new Point2D(0, 0));

    // Now travel around and visit other houses.
    for (final Direction d : il.fileAsObjectsFromCodePoints(pc, Direction::valueOf)) {
      if (Direction.UP == d) {
        ++x[visitor];
      }
      else if (Direction.DOWN == d) {
        --x[visitor];
      }
      else if (Direction.LEFT == d) {
        --y[visitor];
      }
      else if (Direction.RIGHT == d) {
        ++y[visitor];
      }
      visited.add(new Point2D(x[visitor], y[visitor]));
      visitor = (visitor + 1) % 2;
    }

    return visited.size();
  }

  /** Enumeration of movement directions. */
  private static enum Direction {

    UP('^'),
    DOWN('v'),
    LEFT('<'),
    RIGHT('>');

    public static Direction valueOf(final int codePoint) {
      return Arrays.stream(values())
                   .filter(d -> d._codePoint == codePoint)
                   .findFirst()
                   .get();
    }

    private final int _codePoint;

    Direction(final int codePoint) {
      _codePoint = codePoint;
    }

  }

}
