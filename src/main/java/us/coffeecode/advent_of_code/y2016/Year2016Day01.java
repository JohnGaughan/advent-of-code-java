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

import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/1">Year 2016, day 1</a>. This problem asks us to traverse through a
 * coordinate space and find distances to two locations.
 * </p>
 * <p>
 * This is a fairly simply problem with a simple solution. For part one, just navigate until there are no more
 * instructions, then find the distance to origin. For part two, we need to track each intermediate step along the way
 * and stop as soon as we encounter a location twice. This means iterating through each step instead of short cutting to
 * the end of a move.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day01 {

  public long calculatePart1() {
    Heading heading = Heading.NORTH;
    Coordinate location = new Coordinate(0, 0);
    for (final Instruction instruction : getInput()) {
      if (instruction.isLeft()) {
        heading = heading.left();
      }
      else {
        heading = heading.right();
      }
      if (heading == Heading.NORTH) {
        location = new Coordinate(location.x, location.y + instruction.getDistance());
      }
      else if (heading == Heading.EAST) {
        location = new Coordinate(location.x + instruction.getDistance(), location.y);
      }
      else if (heading == Heading.WEST) {
        location = new Coordinate(location.x - instruction.getDistance(), location.y);
      }
      else {
        location = new Coordinate(location.x, location.y - instruction.getDistance());
      }
    }
    return location.getDistanceToOrigin();
  }

  public long calculatePart2() {
    Heading heading = Heading.NORTH;
    Coordinate location = new Coordinate(0, 0);
    final Set<Coordinate> seen = new HashSet<>();
    seen.add(location);
    outer: for (final Instruction instruction : getInput()) {
      if (instruction.isLeft()) {
        heading = heading.left();
      }
      else {
        heading = heading.right();
      }
      for (int i = 0; i < instruction.getDistance(); ++i) {
        if (heading == Heading.NORTH) {
          location = new Coordinate(location.x, location.y + 1);
        }
        else if (heading == Heading.EAST) {
          location = new Coordinate(location.x + 1, location.y);
        }
        else if (heading == Heading.WEST) {
          location = new Coordinate(location.x - 1, location.y);
        }
        else {
          location = new Coordinate(location.x, location.y - 1);
        }
        if (seen.contains(location)) {
          break outer;
        }
        seen.add(location);
      }
    }
    return location.getDistanceToOrigin();
  }

  private static final Pattern SPLIT = Pattern.compile(", ");

  /** Get the input data for this solution. */
  private List<Instruction> getInput() {
    try {
      return Arrays.stream(SPLIT.split(Files.readString(Utils.getInput(2016, 1)).trim())).map(Instruction::new).collect(
        Collectors.toList());
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Coordinate {

    final int x;

    final int y;

    Coordinate(final int _x, final int _y) {
      x = _x;
      y = _y;
    }

    /**
     * Get the distance to origin. This is the total distance traveling along 90 degree grid angles as opposed to a
     * straight line.
     */
    int getDistanceToOrigin() {
      return Math.abs(x) + Math.abs(y);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (!(obj instanceof Coordinate)) {
        return false;
      }
      else {
        final Coordinate o = (Coordinate) obj;
        return o.x == x && o.y == y;
      }
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
      // Input data doesn't have anything close to 16-bits-large numbers, so this does not have false positives.
      return x + (y << 16);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return "(" + x + "," + y + ")";
    }

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

  private static final class Instruction {

    private final char direction;

    private final int distance;

    Instruction(final String input) {
      direction = input.charAt(0);
      distance = Integer.parseInt(input.substring(1));
    }

    boolean isLeft() {
      return direction == 'L';
    }

    int getDistance() {
      return distance;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return "(" + Character.toString(direction) + "," + distance + ")";
    }
  }

}
