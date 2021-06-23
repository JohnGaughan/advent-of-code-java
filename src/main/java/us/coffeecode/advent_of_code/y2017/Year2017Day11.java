/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2021  John Gaughan
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
package us.coffeecode.advent_of_code.y2017;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2017/day/11">Year 2017, day 11</a>. This problem is about calculating distances on
 * a hexagonal grid. Given a set of steps, part one asks how far from origin we end up. Part two asks the farthest
 * distance at any point while traveling.
 * </p>
 * <p>
 * The crux of this problem is the hexagonal grid. Knowing how to move around and calculate distances is a little tricky
 * at first, but there is a <a href="https://www.redblobgames.com/grids/hexagons/">very useful web site</a> that I have
 * used a few times for various coding exercises that explains how to do this. Specifically, using axial coordinates
 * allows us to use simple Cartesian formulas to calculate distances. The short version is in order to handle left and
 * right, we need to skew the horizontal axis along either the NW/SE or NE/SW direction. Once we do that, the math is
 * trivial.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2017Day11 {

  public long calculatePart1() {
    int x = 0;
    int y = 0;
    for (Direction d : getInput()) {
      x += d.dx;
      y += d.dy;
    }
    return Math.abs(x) + Math.abs(y);
  }

  public long calculatePart2() {
    int x = 0;
    int y = 0;
    int farthest = 0;
    for (Direction d : getInput()) {
      x += d.dx;
      y += d.dy;
      final int distance = Math.abs(x) + Math.abs(y);
      farthest = Math.max(distance, farthest);
    }
    return farthest;
  }

  /** Get the input data for this solution. */
  private List<Direction> getInput() {
    try {
      return Arrays.stream(SEPARATOR.split(Files.readString(Utils.getInput(2017, 11)).trim())).map(
        Direction::valueOf).collect(Collectors.toList());
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final Pattern SEPARATOR = Pattern.compile(",");

  private static enum Direction {

    n(1, 0),
    ne(0, 1),
    se(-1, 1),
    s(-1, 0),
    sw(0, -1),
    nw(1, -1);

    final int dx;

    final int dy;

    Direction(final int _dx, final int _dy) {
      dx = _dx;
      dy = _dy;
    }
  }

}
