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

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.DigitConverter;

@AdventOfCodeSolution(year = 2018, day = 10, title = "The Stars Align")
@Component
public final class Year2018Day10 {

  @Autowired
  private InputLoader il;

  @Autowired
  private DigitConverter dc;

  @Solver(part = 1)
  public String calculatePart1(final PuzzleContext pc) {
    return calculate(pc).message;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc).iterations;
  }

  private Result calculate(final PuzzleContext pc) {
    final Iterable<Point> points = il.linesAsObjects(pc, Point::new);
    int xVariance = -1;
    int yVariance = -1;
    long iterations = -1;

    int boost = 10_000;
    for (final Point p : points) {
      p.x += boost * p.dx;
      p.y += boost * p.dy;
    }

    for (int i = 10_000; i < Integer.MAX_VALUE; ++i) {
      int minX = Integer.MAX_VALUE;
      int minY = Integer.MAX_VALUE;
      int maxX = Integer.MIN_VALUE;
      int maxY = Integer.MIN_VALUE;
      for (final Point p : points) {
        p.x += p.dx;
        p.y += p.dy;
        minX = Math.min(p.x, minX);
        minY = Math.min(p.y, minY);
        maxX = Math.max(p.x, maxX);
        maxY = Math.max(p.y, maxY);
      }
      int newXvariance = Math.abs(maxX - minX);
      int newYvariance = Math.abs(maxY - minY);
      if (((newXvariance > xVariance) || (newYvariance > yVariance)) && (i > boost)) {
        // Points are starting to move further apart: undo this iteration and quit.
        for (final Point p : points) {
          p.x -= p.dx;
          p.y -= p.dy;
        }
        iterations = i;
        break;
      }
      xVariance = newXvariance;
      yVariance = newYvariance;
    }
    return new Result(toString(points), iterations);
  }

  private String toString(final Iterable<Point> points) {
    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (final Point p : points) {
      minX = Math.min(p.x, minX);
      minY = Math.min(p.y, minY);
      maxX = Math.max(p.x, maxX);
      maxY = Math.max(p.y, maxY);
    }
    boolean[][] grid = new boolean[maxY - minY + 1][maxX - minX + 1];
    for (final Point p : points) {
      grid[p.y - minY][p.x - minX] = true;
    }
    final StringBuilder str = new StringBuilder();
    for (int x_offset = 0; x_offset < grid[0].length; x_offset += 8) {
      try {
        str.appendCodePoint(dc.toCodePoint(grid, x_offset, 6));
      }
      catch (final NoSuchElementException ex) {
        // Do nothing
      }
    }
    return str.toString();
  }

  private static record Result(String message, long iterations) {}

  private static final class Point {

    int x;

    int y;

    final int dx;

    final int dy;

    Point(final String input) {
      final int x1 = input.indexOf('<') + 1;
      final int x2 = input.indexOf(',', x1);
      final int x3 = input.indexOf('>', x2);
      final int y1 = input.indexOf('<', x3) + 1;
      final int y2 = input.indexOf(',', y1);
      final int y3 = input.indexOf('>', y2);
      x = Integer.parseInt(input.substring(x1, x2)
                                .trim());
      y = Integer.parseInt(input.substring(x2 + 1, x3)
                                .trim());
      dx = Integer.parseInt(input.substring(y1, y2)
                                 .trim());
      dy = Integer.parseInt(input.substring(y2 + 1, y3)
                                 .trim());
    }
  }

}
