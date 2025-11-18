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
package us.coffeecode.advent_of_code.y2016;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2016, day = 1)
@Component
public final class Year2016Day01 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    Heading heading = Heading.NORTH;
    Point2D point = new Point2D(0, 0);
    for (final Instruction instruction : getInput(pc)) {
      if (instruction.isLeft()) {
        heading = heading.left();
      }
      else {
        heading = heading.right();
      }
      if (heading == Heading.NORTH) {
        point = new Point2D(point.getX(), point.getY() + instruction.getDistance());
      }
      else if (heading == Heading.EAST) {
        point = new Point2D(point.getX() + instruction.getDistance(), point.getY());
      }
      else if (heading == Heading.WEST) {
        point = new Point2D(point.getX() - instruction.getDistance(), point.getY());
      }
      else {
        point = new Point2D(point.getX(), point.getY() - instruction.getDistance());
      }
    }
    return point.getManhattanDistance();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    Heading heading = Heading.NORTH;
    Point2D point = new Point2D(0, 0);
    final Set<Point2D> seen = new HashSet<>();
    seen.add(point);
    outer: for (final Instruction instruction : getInput(pc)) {
      if (instruction.isLeft()) {
        heading = heading.left();
      }
      else {
        heading = heading.right();
      }
      for (int i = 0; i < instruction.getDistance(); ++i) {
        if (heading == Heading.NORTH) {
          point = new Point2D(point.getX(), point.getY() + 1);
        }
        else if (heading == Heading.EAST) {
          point = new Point2D(point.getX() + 1, point.getY());
        }
        else if (heading == Heading.WEST) {
          point = new Point2D(point.getX() - 1, point.getY());
        }
        else {
          point = new Point2D(point.getX(), point.getY() - 1);
        }
        if (seen.contains(point)) {
          break outer;
        }
        seen.add(point);
      }
    }
    return point.getManhattanDistance();
  }

  private static final Pattern SPLIT = Pattern.compile(", ");

  /** Get the input data for this solution. */
  private Iterable<Instruction> getInput(final PuzzleContext pc) {
    return Arrays.stream(SPLIT.split(il.fileAsString(pc)))
                 .map(Instruction::make)
                 .toList();
  }

  private static enum Heading {

    NORTH,
    EAST,
    WEST,
    SOUTH;

    private static final Map<Heading, Heading> LEFT = new HashMap<>();

    private static final Map<Heading, Heading> RIGHT = new HashMap<>();

    static {
      LEFT.put(NORTH, WEST);
      LEFT.put(EAST, NORTH);
      LEFT.put(WEST, SOUTH);
      LEFT.put(SOUTH, EAST);
      RIGHT.put(NORTH, EAST);
      RIGHT.put(EAST, SOUTH);
      RIGHT.put(WEST, NORTH);
      RIGHT.put(SOUTH, WEST);
    }

    /** Get the heading to the left of this one. */
    Heading left() {
      return LEFT.get(this);
    }

    /** Get the heading to the right of this one. */
    Heading right() {
      return RIGHT.get(this);
    }

  }

  private static record Instruction(char direction, int distance) {

    static Instruction make(final String input) {
      return new Instruction(input.charAt(0), Integer.parseInt(input.substring(1)));
    }

    boolean isLeft() {
      return direction == 'L';
    }

    int getDistance() {
      return distance;
    }

  }

}
