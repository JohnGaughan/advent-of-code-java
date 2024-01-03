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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2021, day = 5, title = "Hydrothermal Venture")
@Component
public final class Year2021Day05 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, s -> (s.dx == 0) || (s.dy == 0));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, s -> true);
  }

  private long calculate(final PuzzleContext pc, final Predicate<Segment> filter) {
    final Map<Point2D, Boolean> points = new HashMap<>(1 << 18);
    il.linesAsObjects(pc, this::parse).stream().filter(filter).forEach(seg -> {
      int x = seg.start.getX();
      int y = seg.start.getY();
      for (int i = 0; i < seg.length; ++i) {
        final Point2D key = new Point2D(x, y);
        points.computeIfPresent(key, (k, v) -> TRUE);
        points.putIfAbsent(key, FALSE);
        x += seg.dx;
        y += seg.dy;
      }
    });
    return points.values().stream().filter(b -> TRUE.equals(b)).count();
  }

  private Segment parse(final String line) {
    final int comma1 = line.indexOf(',');
    final int pointSeparator = line.indexOf(' ');
    final int comma2 = line.indexOf(',', pointSeparator);

    final int x1 = Integer.parseInt(line.substring(0, comma1));
    final int y1 = Integer.parseInt(line.substring(comma1 + 1, pointSeparator));
    final int x2 = Integer.parseInt(line.substring(pointSeparator + 4, comma2));
    final int y2 = Integer.parseInt(line.substring(comma2 + 1));

    final Point2D start = new Point2D(x1, y1);
    final int length = 1 + ((x1 == x2) ? Math.abs(y1 - y2) : Math.abs(x1 - x2));
    if (x1 == x2) {
      return new Segment(start, 0, y1 < y2 ? 1 : -1, length);
    }
    else if (y1 == y2) {
      return new Segment(start, x1 < x2 ? 1 : -1, 0, length);
    }
    else if (x1 < x2) {
      if (y1 < y2) {
        return new Segment(start, 1, 1, length);
      }
      return new Segment(start, 1, -1, length);
    }
    if (y1 < y2) {
      return new Segment(start, -1, 1, length);
    }
    return new Segment(start, -1, -1, length);
  }

  private static record Segment(Point2D start, int dx, int dy, int length) {}
}
