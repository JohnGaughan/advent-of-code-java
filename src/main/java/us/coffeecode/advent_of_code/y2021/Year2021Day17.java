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
package us.coffeecode.advent_of_code.y2021;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;
import us.coffeecode.advent_of_code.util.Range2D;

@AdventOfCodeSolution(year = 2021, day = 17, title = "Trick Shot")
@Component
public final class Year2021Day17 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Range2D range = toRange(il.fileAsIntsFromDigitGroups(pc));
    int y = -1 - range.getY1();
    return y * (y + 1) >> 1;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Range2D range = toRange(il.fileAsIntsFromDigitGroups(pc));
    final Point2D origin = new Point2D(0, 0);
    long hits = 0;
    final int min_dx = getMinDX(range);
    for (int dy = range.getY1(); dy < Math.abs(range.getY1()); ++dy) {
      for (int dx = min_dx; dx <= range.getX2(); ++dx) {
        Trajectory t = new Trajectory(origin, dx, dy);
        while (!t.isPast(range)) {
          final Point2D nextLocation = new Point2D(t.point, t.v_x, t.v_y);
          t = new Trajectory(nextLocation, t.v_x > 0 ? t.v_x - 1 : 0, t.v_y - 1);
          if (range.containsInclusive(nextLocation)) {
            ++hits;
            break;
          }
        }
      }
    }
    return hits;
  }

  /** Get the minimum X velocity that can possibly hit the range. */
  private int getMinDX(final Range2D range) {
    int n = 1;
    while (((n * (n + 1)) >> 1) < range.getX1()) {
      ++n;
    }
    return n;
  }

  private record Trajectory(Point2D point, int v_x, int v_y) {

    boolean isPast(final Range2D range) {
      return (point.getY() < range.getY1()) || (range.getX2() < point.getX());
    }
  }

  private Range2D toRange(final int[] c) {
    return new Range2D(c[0], c[2], c[1], c[3]);
  }

}
