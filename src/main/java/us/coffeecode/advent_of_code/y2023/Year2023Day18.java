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
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2023, day = 18)
@Component
public class Year2023Day18 {

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

  /** Calculate the answer using Shoelace and Pick's algorithms. */
  private long calculate(final PuzzleContext pc) {
    final List<Point2D> vertices = new ArrayList<>(700);
    vertices.add(Point2D.ORIGIN);
    Point2D current = Point2D.ORIGIN;
    long perimeter = 0;
    for (final DigInstruction inst : il.linesAsObjects(pc, s -> parse(s, pc.getBoolean("AlternateInput")))) {
      perimeter += inst.dist;
      current = current.add(inst.dist * inst.dir.dx, inst.dist * inst.dir.dy);
      vertices.add(current);
    }
    long interiorArea = 0;
    for (int i = 0; i < vertices.size(); ++i) {
      final Point2D v2 = vertices.get(i);
      final Point2D v1 = (i == 0) ? vertices.getLast() : vertices.get(i - 1);
      // 32-bit math overflows here.
      interiorArea += ((long) v1.getX() * (long) v2.getY()) - ((long) v2.getX() * (long) v1.getY());
    }
    return 1 + (Math.abs(interiorArea) >> 1) + (perimeter >> 1);

  }

  /** Parse an input line into a dig instructions, optionally using part two's alternate parsing. */
  private DigInstruction parse(final String line, final boolean alternateInput) {
    final String[] tokens = LINE_SPLIT.split(line);
    if (alternateInput) {
      final int dist = Integer.parseInt(tokens[2].substring(2, 7), 16);
      final int dir = Integer.parseInt(tokens[2].substring(7, 8), 16);
      return new DigInstruction(Direction.forOrdinal(dir), dist);
    }
    return new DigInstruction(Direction.valueOf(tokens[0]), Integer.parseInt(tokens[1]));
  }

  /** Represents one direction the digger can dig. */
  private static enum Direction {

    R(1, 0),
    D(0, 1),
    L(-1, 0),
    U(0, -1);

    final int dx;

    final int dy;

    private Direction(final int _dx, final int _dy) {
      dx = _dx;
      dy = _dy;
    }

    /**
     * Get the direction with the integer ordinal. Enum instances are defined in the same order as the ordinals are
     * specified in the puzzle.
     */
    static Direction forOrdinal(final int ordinal) {
      for (final Direction d : values()) {
        if (d.ordinal() == ordinal) {
          return d;
        }
      }
      throw new IllegalArgumentException(Integer.toString(ordinal));
    }
  }

  /** One dig instruction tells the user to dig a specific distance in a specific direction. */
  private record DigInstruction(Direction dir, int dist) {}

  /** Splits an input line into its three tokens. */
  private static final Pattern LINE_SPLIT = Pattern.compile(" ");
}
