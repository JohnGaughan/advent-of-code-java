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
import us.coffeecode.advent_of_code.util.LongRange;

@AdventOfCodeSolution(year = 2025, day = 5)
@Component
public class Year2025Day05 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final Input input = getInput(pc);
    return Arrays.stream(input.ingredients)
                 .filter(n -> isFresh(n, input.ranges))
                 .count();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    // Merge the ranges so they are all disjoint from each other.
    return LongRange.merge(getInput(pc).ranges)
                    .stream()
                    // Convert each range to the number of elements contained in that range.
                    .mapToLong(LongRange::sizeInclusive)
                    // Add up all of those range sizes.
                    .sum();
  }

  /** Determine if the provided ingredient is contained in any of the provided ranges. */
  private boolean isFresh(final long ingredient, final List<LongRange> ranges) {
    for (final LongRange range : ranges) {
      if (range.containsInclusive(ingredient)) {
        return true;
      }
    }
    return false;
  }

  /** Parse the program input and return it in an input object. */
  private Input getInput(final PuzzleContext pc) {
    return il.groupsAsObject(pc, groups -> {
      return new Input(groups.get(0)
                             .stream()
                             .map(RANGE_SPLIT::split)
                             .map(s -> new LongRange(s[0], s[1]))
                             .toList(),
        groups.get(1)
              .stream()
              .mapToLong(Long::parseLong)
              .toArray());
    });
  }

  /** Pattern that splits a line into two tokens representing a range. */
  private static final Pattern RANGE_SPLIT = Pattern.compile("\\-");

  /** Represents the full program input. */
  private record Input(List<LongRange> ranges, long[] ingredients) {}
}
