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

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.function.TriPredicate;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2022, day = 12)
@Component
public class Year2022Day12 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, (start, end) -> start, (candidate, current, map) -> (candidate.get(map) < current.get(map) + 2),
      (candidate, end, map) -> (candidate.equals(end)));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, (start, end) -> end, (candidate, current, map) -> (candidate.get(map) > current.get(map) - 2),
      (candidate, end, map) -> (candidate.get(map) == 'a'));
  }

  private long calculate(final PuzzleContext pc, final BiFunction<Point2D, Point2D, Point2D> getStart, final TriPredicate<Point2D, Point2D, int[][]> moveTest, final TriPredicate<Point2D, Point2D, int[][]> endTest) {
    final int[][] map = il.linesAsCodePoints(pc);
    final Point2D start = findFirst(map, 'S');
    final Point2D end = findFirst(map, 'E');
    start.set(map, 'a');
    end.set(map, 'z');
    final Deque<Point2D> visiting = new LinkedList<>();
    final Map<Point2D, Long> visited = new HashMap<>();
    visiting.addLast(getStart.apply(start, end));
    visited.put(getStart.apply(start, end), Long.valueOf(0));
    while (!visiting.isEmpty()) {
      final Point2D current = visiting.removeFirst();
      final Long cost = Long.valueOf(visited.get(current)
                                            .longValue()
        + 1);
      for (final Point2D candidate : current.getCardinalNeighbors()) {
        if (candidate.isIn(map) && moveTest.test(candidate, current, map) && !visited.containsKey(candidate)) {
          if (endTest.test(candidate, end, map)) {
            return cost.longValue();
          }
          visiting.addLast(candidate);
          visited.put(candidate, cost);
        }
      }
    }
    throw new IllegalStateException("Did not visit target coordinate");
  }

  private static Point2D findFirst(final int[][] map, final int codePoint) {
    for (int y = 0; y < map.length; ++y) {
      for (int x = 0; x < map[y].length; ++x) {
        if (map[y][x] == codePoint) {
          return new Point2D(x, y);
        }
      }
    }
    throw new IllegalStateException();
  }

}
