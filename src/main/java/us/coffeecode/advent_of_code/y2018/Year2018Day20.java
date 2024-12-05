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
package us.coffeecode.advent_of_code.y2018;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2018, day = 20, title = "A Regular Map")
@Component
public final class Year2018Day20 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Map<Point2D, Long> paths = mapPaths(pc);
    return paths.values()
                .stream()
                .mapToLong(Long::longValue)
                .max()
                .getAsLong();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final Map<Point2D, Long> paths = mapPaths(pc);
    return paths.values()
                .stream()
                .mapToLong(Long::longValue)
                .filter(i -> i >= 1_000)
                .count();
  }

  private Map<Point2D, Long> mapPaths(final PuzzleContext pc) {
    final Map<Point2D, Long> points = new HashMap<>();
    final Deque<Point2D> stack = new LinkedList<>();
    Point2D current = new Point2D(0, 0);
    points.put(current, Long.valueOf(0));
    for (final int ch : getInput(pc)) {
      if (ch == '(') {
        stack.addFirst(current);
      }
      else if (ch == ')') {
        current = stack.pollFirst();
      }
      else if (ch == '|') {
        current = stack.peekFirst();
      }
      else {
        final long distance = points.get(current)
                                    .longValue()
          + 1;
        if (ch == 'N') {
          current = new Point2D(current, 0, -1);
        }
        else if (ch == 'E') {
          current = new Point2D(current, 1, 0);
        }
        else if (ch == 'S') {
          current = new Point2D(current, 0, 1);
        }
        else /* W */ {
          current = new Point2D(current, -1, 0);
        }
        if (points.containsKey(current)) {
          points.put(current, Long.valueOf(Math.min(distance, points.get(current)
                                                                    .longValue())));
        }
        else {
          points.put(current, Long.valueOf(distance));
        }
      }
    }
    return points;
  }

  /** Get the input data for this solution. */
  private int[] getInput(final PuzzleContext pc) {
    final String input = il.fileAsString(pc);
    return input.substring(1, input.length() - 1)
                .codePoints()
                .toArray();
  }

}
