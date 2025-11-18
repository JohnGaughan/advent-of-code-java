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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2022, day = 9)
@Component
public class Year2022Day09 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, 2);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, 10);
  }

  private long calculate(final PuzzleContext pc, final int knotQuantity) {
    final Point2D[] knots = new Point2D[knotQuantity];
    Arrays.fill(knots, Point2D.ORIGIN);
    final Set<Point2D> visited = new HashSet<>();
    visited.add(knots[0]);
    for (final Movement move : il.linesAsObjects(pc, Movement::valueOf)) {
      for (int i = 0; i < move.steps; ++i) {
        // Move the head.
        if ('U' == move.direction) {
          knots[0] = new Point2D(knots[0].getX(), knots[0].getY() + 1);
        }
        else if ('D' == move.direction) {
          knots[0] = new Point2D(knots[0].getX(), knots[0].getY() - 1);
        }
        else if ('L' == move.direction) {
          knots[0] = new Point2D(knots[0].getX() - 1, knots[0].getY());
        }
        else {
          knots[0] = new Point2D(knots[0].getX() + 1, knots[0].getY());
        }
        // Move the rest of the knots.
        for (int k = 1; k < knots.length; ++k) {
          // Still touching: don't move.
          if (touches(knots[k - 1], knots[k])) {
            knots[k] = knots[k];
          }
          // Same column: move to immediately behind the previous knot
          else if (knots[k - 1].getX() == knots[k].getX()) {
            if (knots[k - 1].getY() > knots[k].getY()) {
              knots[k] = new Point2D(knots[k].getX(), knots[k - 1].getY() - 1);
            }
            else {
              knots[k] = new Point2D(knots[k].getX(), knots[k - 1].getY() + 1);
            }
          }
          // Same row: move to immediately behind the previous knot
          else if (knots[k - 1].getY() == knots[k].getY()) {
            if (knots[k - 1].getX() > knots[k].getX()) {
              knots[k] = new Point2D(knots[k - 1].getX() - 1, knots[k].getY());
            }
            else {
              knots[k] = new Point2D(knots[k - 1].getX() + 1, knots[k].getY());
            }
          }
          // Find the matching diagonal move that brings the knots adjacent.
          else {
            for (final Point2D candidate : knots[k].getDiagonalNeighbors()) {
              if (touches(candidate, knots[k - 1])) {
                knots[k] = candidate;
                break;
              }
            }
          }
        }
        visited.add(knots[knots.length - 1]);
      }
    }
    return visited.size();
  }

  boolean touches(final Point2D a, final Point2D b) {
    return a.equals(b) || a.isAdjacent(b);
  }

  private record Movement(int direction, int steps) {

    private static final Pattern SPLIT = Pattern.compile(" ");

    static Movement valueOf(final String s) {
      final String[] tokens = SPLIT.split(s);
      return new Movement(tokens[0].codePointAt(0), Integer.parseInt(tokens[1]));
    }
  }

}
