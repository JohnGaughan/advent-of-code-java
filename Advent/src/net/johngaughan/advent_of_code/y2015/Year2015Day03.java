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
package net.johngaughan.advent_of_code.y2015;

import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import net.johngaughan.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/3">Year 2015, day 3</a>. This problem deals with Santa visiting houses on
 * a grid. Given directions to move in the four cardinal directions, Santa moves around and visits each house in turn.
 * Part one asks how many houses he visits. Part two adds a second Santa, and they take turns moving. How many houses
 * does that team visit?
 * </p>
 * <p>
 * There is no easy reduction here, since intermediate steps need to be tracked in order to know exactly how many houses
 * Santa and his robot visited. My approach here was simply to iterate over the directions and track the current
 * coordinates and save them off in a set. Since we do not care about repeat visits, a set is ideal to track the unique
 * coordinates visited. For part two, I track two sets of coordinates and alternate which one is modified via a small
 * array.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day03 {

  public int calculatePart1() {
    final Set<Coordinates> visited = new HashSet<>();
    int x = 0;
    int y = 0;

    // Visit the first house.
    visited.add(new Coordinates(x, y));

    // Now travel around and visit other houses.
    for (final Direction d : getInput()) {
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
      visited.add(new Coordinates(x, y));
    }

    return visited.size();
  }

  public int calculatePart2() {
    final Set<Coordinates> visited = new HashSet<>();
    final int x[] = new int[2];
    final int y[] = new int[2];
    int visitor = 0;

    // Visit the first house.
    visited.add(new Coordinates(0, 0));

    // Now travel around and visit other houses.
    for (final Direction d : getInput()) {
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
      visited.add(new Coordinates(x[visitor], y[visitor]));
      visitor = (visitor + 1) % 2;
    }

    return visited.size();
  }

  /** Get the input data for this solution. */
  private List<Direction> getInput() {
    try {
      return Files.readString(Utils.getInput(2015, 3)).trim().codePoints().mapToObj(Direction::valueOf).collect(
        Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  /** Enumeration of movement directions. */
  private static enum Direction {

    UP('^'),
    DOWN('v'),
    LEFT('<'),
    RIGHT('>');

    public static Direction valueOf(final int codePoint) {
      for (final Direction d : values()) {
        if (d._codePoint == codePoint) {
          return d;
        }
      }
      throw new IllegalArgumentException(
        "Input code point [" + codePoint + "] does not correlate with any instance of this enum");
    }

    private final int _codePoint;

    Direction(final int codePoint) {
      _codePoint = codePoint;
    }

  }

  /** Represents a single pair of coordinates. */
  private static final class Coordinates {

    private final int x;

    private final int y;

    private final int hashCode;

    /** Constructs a <code>Coordinates</code>. */
    public Coordinates(final int xx, final int yy) {
      x = xx;
      y = yy;
      hashCode = Objects.hash(x, y);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (!(obj instanceof Coordinates)) {
        return false;
      }
      else {
        final Coordinates o = (Coordinates) obj;
        return x == o.x && y == o.y;
      }
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
      return hashCode;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
      return "(" + x + "," + y + ")";
    }
  }

}
