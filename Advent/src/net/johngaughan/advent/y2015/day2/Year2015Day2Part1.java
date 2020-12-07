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
 * Year 2015, day 1, part 1.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day2Part1
implements AdventProblem {

  /** {@inheritDoc} */
  @Override
  public long calculate(final Path path) {
    long totalArea = 0;
    for (final List<Long> dimensions : new Parser().parse(path)) {
      totalArea += calculate(dimensions);
    }
    return totalArea;
  }

  /** Calculate the area needed by a single box. */
  private long calculate(final List<Long> dimensions) {
    final long x = dimensions.get(0);
    final long y = dimensions.get(1);
    final long z = dimensions.get(2);

    // Total surface area of the box.
    long area = 2 * ((x * y) + (x * z) + (y * z));

    // Add the extra area for slack.
    area += ((x * y * z) / Math.max(x, Math.max(y, z)));

    return area;
  }

}
