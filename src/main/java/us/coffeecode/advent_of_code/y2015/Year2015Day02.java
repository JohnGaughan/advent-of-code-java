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
package us.coffeecode.advent_of_code.y2015;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.Point3D;

@AdventOfCodeSolution(year = 2015, day = 2, title = "I Was Told There Would Be No Math")
@Component
public final class Year2015Day02 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return getInput(pc).stream()
                       .mapToLong(this::calculateWrappingPaperArea)
                       .sum();
  }

  /** Calculate the area needed by a single box. */
  private long calculateWrappingPaperArea(final Point3D d) {
    // Total surface area of the box.
    final long area = ((d.getX() * d.getY()) + (d.getX() * d.getZ()) + (d.getY() * d.getZ())) << 1;

    // Add the extra area for slack.
    return area + d.getX() * d.getY() * d.getZ() / Math.max(d.getX(), Math.max(d.getY(), d.getZ()));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return getInput(pc).stream()
                       .mapToLong(this::calculateRibbonLength)
                       .sum();
  }

  /** Calculate the ribbon length needed by a single box. */
  private long calculateRibbonLength(final Point3D d) {
    // Perimeters
    final long p1 = (d.getX() << 1) + (d.getY() << 1);
    final long p2 = (d.getX() << 1) + (d.getZ() << 1);
    final long p3 = (d.getY() << 1) + (d.getZ() << 1);

    // Shortest perimeter
    final long length = Math.min(p1, Math.min(p2, p3));

    // Extra for the bow
    return length + d.getX() * d.getY() * d.getZ();
  }

  /** Get the input data for this solution. */
  private Collection<Point3D> getInput(final PuzzleContext pc) {
    return il.linesAsObjects(pc, s -> {
      final int[] a = Arrays.stream(SPLIT.split(s))
                            .mapToInt(Integer::parseInt)
                            .toArray();
      return new Point3D(a[0], a[1], a[2]);
    });
  }

  private static final Pattern SPLIT = Pattern.compile("x");
}
