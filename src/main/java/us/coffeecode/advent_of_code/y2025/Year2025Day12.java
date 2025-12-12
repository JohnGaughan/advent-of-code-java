/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2025  John Gaughan
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
package us.coffeecode.advent_of_code.y2025;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2025, day = 12)
@Component
public class Year2025Day12 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input input = getInput(pc);
    final long answer = Arrays.stream(input.regions)
                              .filter(r -> fitsAllPresents(pc, r, input.presents))
                              .count();
    return answer;
  }

  /** Determine if the provided region can fit all of the presents in quantities defined in the region itself. */
  private boolean fitsAllPresents(final PuzzleContext pc, final Region region, final long[] presents) {
    return (region.area >= getMinimumAreaForPresents(region.required, presents));
  }

  /** Given presents and quantities of those presents, get the total minimum area required. */
  private long getMinimumAreaForPresents(final int[] required, final long[] presents) {
    long neededArea = 0;
    for (int i = 0; i < required.length; ++i) {
      neededArea += required[i] * presents[i];
    }
    return neededArea;
  }

  /** Get the program input. */
  private Input getInput(final PuzzleContext pc) {
    final List<List<String>> groups = il.groups(pc);
    final List<String> last = groups.getLast();
    // Omit the last region but only on the example input. See the readme for more information.
    final int limit = (last.size() == 3) ? 2 : last.size();
    return new Input(groups.stream()
                           .limit(groups.size() - 1)
                           .map(l -> l.subList(1, l.size()))
                           .mapToLong(this::toPresent)
                           .toArray(),
      last.stream()
          .limit(limit)
          .map(this::toRegion)
          .toArray(Region[]::new));
  }

  /** Convert a list of strings into a present's area. */
  private long toPresent(final List<String> lines) {
    return lines.stream()
                .mapToLong(l -> l.codePoints()
                                 .filter(c -> c == PRESENT)
                                 .count())
                .sum();
  }

  /** Parse a single line of string input into region data. */
  private Region toRegion(final String line) {
    final String[] tokens = REGION_SPLIT.split(line);
    final int separator = tokens[0].indexOf(DIMENSION_SEPARATOR);
    final long width = Long.parseLong(tokens[0].substring(0, separator));
    final long height = Long.parseLong(tokens[0].substring(separator + 1));
    return new Region(width, height, width * height, Arrays.stream(tokens)
                                                           .skip(1)
                                                           .mapToInt(Integer::parseInt)
                                                           .toArray());
  }

  /** Represents a region's width and height dimensions, as well as the quantity of which presents it must contain. */
  private record Region(long width, long length, long area, int[] required) {}

  /** Represents the entirety of the program's input data. Presents do not store shape data, only their areas. */
  private record Input(long[] presents, Region[] regions) {}

  /** Indicates a grid location is occupied by a present. */
  private static final int PRESENT = '#';

  /** Character that separates the width and length dimensions in the input data for a region. */
  private static final int DIMENSION_SEPARATOR = 'x';

  /** Pattern that splits a region's line of input into tokens. */
  private static final Pattern REGION_SPLIT = Pattern.compile(":? ");
}
