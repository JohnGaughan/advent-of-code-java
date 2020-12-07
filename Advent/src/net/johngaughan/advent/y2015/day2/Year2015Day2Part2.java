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
package net.johngaughan.advent.y2015.day2;

import java.nio.file.Path;
import java.util.List;

import net.johngaughan.advent.AdventProblem;

/**
 * <p>
 * Year 2015, day 1, part 2.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day2Part2
implements AdventProblem {

  /** {@inheritDoc} */
  @Override
  public long calculate(final Path path) {
    long totalLength = 0;
    for (final List<Long> dimensions : new Parser().parse(path)) {
      totalLength += calculate(dimensions);
    }
    return totalLength;
  }

  /** Calculate the ribbon length needed by a single box. */
  private long calculate(final List<Long> dimensions) {
    final long x = dimensions.get(0);
    final long y = dimensions.get(1);
    final long z = dimensions.get(2);

    // Perimeters
    final long p1 = (2 * x) + (2 * y);
    final long p2 = (2 * x) + (2 * z);
    final long p3 = (2 * y) + (2 * z);

    // Shortest perimeter
    long length = Math.min(p1, Math.min(p2, p3));

    // Extra for the bow
    length += x * y * z;

    return length;
  }

}
