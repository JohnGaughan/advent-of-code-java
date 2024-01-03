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

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2017, day = 11, title = "Hex Ed")
@Component
public final class Year2017Day11 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    long x = 0;
    long y = 0;
    for (final Direction d : il.fileAsObjectsFromSplit(pc, SEPARATOR, Direction::valueOf)) {
      x += d.dx;
      y += d.dy;
    }
    return (Math.abs(x) + Math.abs(y)) >> 1;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    long x = 0;
    long y = 0;
    long farthest = 0;
    for (final Direction d : il.fileAsObjectsFromSplit(pc, SEPARATOR, Direction::valueOf)) {
      x += d.dx;
      y += d.dy;
      final long distance = (Math.abs(x) + Math.abs(y)) >> 1;
      farthest = Math.max(distance, farthest);
    }
    return farthest;
  }

  private static final Pattern SEPARATOR = Pattern.compile(",");

  private static enum Direction {

    // This uses doubled coordinates, avoiding the need to offset each column. However, we need to cut distances in half
    // to compensate. Note that each direction has a magnitude of two: ignoring sign, each of the two deltas add up to
    // two.

    n(0, -2),
    ne(1, -1),
    se(1, 1),
    s(0, 2),
    sw(-1, 1),
    nw(-1, -1);

    final int dx;

    final int dy;

    Direction(final int _dx, final int _dy) {
      dx = _dx;
      dy = _dy;
    }
  }

}
