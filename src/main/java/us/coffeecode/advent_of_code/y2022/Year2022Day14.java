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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntUnaryOperator;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2022, day = 14, title = "Regolith Reservoir")
@Component
public class Year2022Day14 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(getMap(pc, x -> x, y -> y, a -> {}));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(getMap(pc, x -> x << 1, y -> y + 2, a -> {
      Arrays.fill(a, WALL);
    }));
  }

  private long calculate(final int[][] map) {
    long atRest = 0;
    outer: while (START.get(map) == '+') {
      Point2D particle = START;
      inner: while (true) {
        // Try falling down.
        Point2D next = new Point2D(particle.getX(), particle.getY() + 1);
        if (next.isIn(map)) {
          final int down = next.get(map);
          if (down == OPEN) {
            particle = next;
            continue;
          }
          // Try down and left.
          next = new Point2D(particle.getX() - 1, particle.getY() + 1);
          if (next.isIn(map)) {
            final int downLeft = next.get(map);
            if (downLeft == OPEN) {
              particle = next;
              continue;
            }
            // Try down and right.
            next = new Point2D(particle.getX() + 1, particle.getY() + 1);
            if (next.isIn(map)) {
              final int downRight = next.get(map);
              if (downRight == OPEN) {
                particle = next;
                continue;
              }
              // Particle cannot move anywhere: come to rest.
              particle.set(map, SAND);
              ++atRest;
              break inner;
            }
            else {
              break outer;
            }
          }
          else {
            break outer;
          }
        }
        else {
          break outer;
        }
      }
    }
    return atRest;
  }

  private static final Point2D START = new Point2D(500, 0);

  private static final int OPEN = '.';

  private static final int SAND = 'o';

  private static final int WALL = '#';

  private int[][] getMap(final PuzzleContext pc, final IntUnaryOperator xAdjust, final IntUnaryOperator yAdjust, final Consumer<int[]> fLastRow) {
    final Set<Point2D> points = new HashSet<>();
    for (final int[] line : il.linesAs2dIntArrayFromSplit(pc, SPLIT)) {
      for (int i = 2; i < line.length; i += 2) {
        points.addAll(new Point2D(line, i).range(new Point2D(line, i - 2)));
      }
    }
    final int width = 1 + xAdjust.applyAsInt(points.stream()
                                                   .mapToInt(Point2D::getX)
                                                   .max()
                                                   .getAsInt());
    final int height = 1 + yAdjust.applyAsInt(points.stream()
                                                    .mapToInt(Point2D::getY)
                                                    .max()
                                                    .getAsInt());
    final int[][] map = new int[height][width];
    for (final int[] row : map) {
      Arrays.fill(row, OPEN);
    }
    START.set(map, '+');
    for (final Point2D point : points) {
      map[point.getY()][point.getX()] = WALL;
    }
    fLastRow.accept(map[map.length - 1]);
    return map;
  }

  /** Regex matches one or more non-digit characters. */
  private static final Pattern SPLIT = Pattern.compile("\\D+");
}
