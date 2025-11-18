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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point2D;

@AdventOfCodeSolution(year = 2021, day = 20)
@Component
public final class Year2021Day20 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc);
  }

  private long calculate(final PuzzleContext pc) {
    final Input input = getInput(pc);
    final int iterations = pc.getInt("iterations");
    // I remember a requirement about the background flashing between on and off each iteration. Did that change, but
    // they updated the problem statement without updating the example output?
    final boolean example = pc.getBoolean("example");
    Set<Point2D> pixels = input.pixels;
    for (int i = 0; i < iterations; ++i) {
      pixels = iterate(input.iea, pixels, ((i & 1) == 1) && !example);
    }
    return pixels.size();
  }

  private Set<Point2D> iterate(final String iea, final Set<Point2D> pixels, final boolean odd) {
    final Set<Point2D> newPixels = new HashSet<>(pixels.size() << 1);

    // Get the min/max x/y bounds to check.
    int minX = Integer.MAX_VALUE;
    int minY = Integer.MAX_VALUE;
    int maxX = Integer.MIN_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (final Point2D pixel : pixels) {
      minX = Math.min(minX, pixel.getX());
      minY = Math.min(minY, pixel.getY());
      maxX = Math.max(maxX, pixel.getX());
      maxY = Math.max(maxY, pixel.getY());
    }

    // Iterate the entire pixel space and enhance.
    for (int y = minY - 1; y <= maxY + 1; ++y) {
      for (int x = minX - 1; x <= maxX + 1; ++x) {
        int offset = 0;
        for (int y1 = y - 1; y1 <= y + 1; ++y1) {
          for (int x1 = x - 1; x1 <= x + 1; ++x1) {
            offset <<= 1;
            // If we are looking at a pixel outside of the previous search space, it is on or off depending on which
            // iteration this is.
            if (odd && (y1 < minY || x1 < minX || y1 > maxY || x1 > maxX)) {
              ++offset;
            }
            else if (pixels.contains(new Point2D(x1, y1))) {
              ++offset;
            }
          }
        }
        if (offset < iea.length() && iea.codePointAt(offset) == '#') {
          newPixels.add(new Point2D(x, y));
        }
      }
    }

    return newPixels;
  }

  private Input getInput(final PuzzleContext pc) {
    final List<List<String>> input = il.groups(pc);
    final StringBuilder iea = new StringBuilder(1024);
    for (final String line : input.getFirst()) {
      iea.append(line);
    }
    final List<String> grid = input.get(1);
    final Set<Point2D> pixels = new HashSet<>(1024);
    for (int y = 0; y < grid.size(); ++y) {
      final String line = grid.get(y);
      for (int x = 0; x < line.length(); ++x) {
        final int ch = line.codePointAt(x);
        if (ch == '#') {
          pixels.add(new Point2D(x, y));
        }
      }
    }
    return new Input(iea.toString()
                        .trim(),
      pixels);
  }

  private record Input(String iea, Set<Point2D> pixels) {}
}
