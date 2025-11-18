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
package us.coffeecode.advent_of_code.y2016;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2016, day = 20)
@Component
public final class Year2016Day20 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    final SortedSet<Range> ranges = new TreeSet<>(il.linesAsObjects(pc, Range::make));
    final Range lowest = getLowest(ranges);
    return (lowest.lower > 0) ? 0 : (lowest.upper + 1);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final SortedSet<Range> inputRanges = new TreeSet<>(il.linesAsObjects(pc, Range::make));
    final SortedSet<Range> mergedRanges = new TreeSet<>();
    // Keep getting the next merged range until the input is exhausted.
    while (!inputRanges.isEmpty()) {
      mergedRanges.add(getLowest(inputRanges));
    }
    // Now we can subtract the size of each range from the total address space.
    long addressSpace = 1L << 32;
    for (final Range range : mergedRanges) {
      addressSpace -= range.size();
    }
    return addressSpace;
  }

  private Range getLowest(final SortedSet<Range> ranges) {
    Range lowest = ranges.first();
    ranges.remove(lowest);
    final Iterator<Range> iter = ranges.iterator();
    while (iter.hasNext()) {
      final Range next = iter.next();
      if (lowest.canMergeWith(next)) {
        lowest = lowest.merge(next);
        iter.remove();
      }
      else {
        // Input is sorted: nothing later in the set can merge, either.
        break;
      }
    }
    return lowest;
  }

  private static record Range(long lower, long upper)
  implements Comparable<Range> {

    static Range make(final String input) {
      final int separator = input.indexOf('-');
      return new Range(Long.parseLong(input.substring(0, separator)), Long.parseLong(input.substring(separator + 1)));
    }

    boolean canMergeWith(final Range other) {
      // There is an actual overlap
      if ((upper >= other.lower) && (lower <= other.upper)) {
        return true;
      }
      // Ranges are non-overlapping but adjacent
      if ((upper == other.lower - 1) || (lower == other.upper + 1)) {
        return true;
      }
      return false;
    }

    Range merge(final Range other) {
      return new Range(Math.min(lower, other.lower), Math.max(upper, other.upper));
    }

    long size() {
      return upper - lower + 1;
    }

    @Override
    public int compareTo(final Range o) {
      final int low = Long.compare(lower, o.lower);
      final int high = Long.compare(upper, o.upper);
      return (low == 0) ? high : low;
    }

  }

}
