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
package net.johngaughan.advent_of_code.y2015;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/2">Year 2015, day 2</a>. This problem involves inspecting a large number
 * of boxes (perfect right rectangular prisms). For part one, we need to figure out how much wrapping paper the boxes
 * need: this is the surface area plus a little extra. For part two, we need to know how much ribbon: this is the
 * perimeter of its smallest side plus some extra for the bow.
 * </p>
 * <p>
 * This was a simple streams exercise where the stream operations take dimensions as input, and transform each one into
 * a numerical answer for that input. Then sum up the numbers to get the requested answer.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2015Day02 {

  public long calculatePart1(final Path path) {
    return parse(path).stream().mapToLong(l -> calculateWrappingPaperArea(l)).sum();
  }

  /** Calculate the area needed by a single box. */
  private long calculateWrappingPaperArea(final Dimensions d) {
    // Total surface area of the box.
    long area = 2 * ((d.x * d.y) + (d.x * d.z) + (d.y * d.z));

    // Add the extra area for slack.
    return area + ((d.x * d.y * d.z) / Math.max(d.x, Math.max(d.y, d.z)));
  }

  public long calculatePart2(final Path path) {
    return parse(path).stream().mapToLong(l -> calculateRibbonLength(l)).sum();
  }

  /** Calculate the ribbon length needed by a single box. */
  private long calculateRibbonLength(final Dimensions d) {
    // Perimeters
    final long p1 = (2 * d.x) + (2 * d.y);
    final long p2 = (2 * d.x) + (2 * d.z);
    final long p3 = (2 * d.y) + (2 * d.z);

    // Shortest perimeter
    long length = Math.min(p1, Math.min(p2, p3));

    // Extra for the bow
    return length + (d.x * d.y * d.z);
  }

  /** Parse the file located at the provided path location. */
  private List<Dimensions> parse(final Path path) {
    try {
      return Files.readAllLines(path).stream().map(Dimensions::new).collect(Collectors.toList());
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Dimensions {

    /** Pattern used to split box dimensions on an input line. */
    private static final Pattern SPLIT = Pattern.compile("x");

    public final long x;

    public final long y;

    public final long z;

    Dimensions(final String line) {
      String[] s = SPLIT.split(line);
      x = Long.parseLong(s[0]);
      y = Long.parseLong(s[1]);
      z = Long.parseLong(s[2]);
    }
  }

}
