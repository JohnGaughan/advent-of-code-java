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

import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2016/day/20">Year 2016, day 20</a>. This problem asks us to perform some analysis
 * on ranges of numbers. Part one asks for the first valid number not in any range, while part two requests the total
 * number of integers within an overall range not included in any of the range rules.
 * </p>
 * <p>
 * The solution is fairly simple. Read in the input and construct Range objects. Store them in a sorted data structure
 * so ordering is already done for us. Next, merge. This is simple: take the first range, and keep merging it with the
 * next range until we find a range that cannot be merged. For part one, we do this once, then we can get the value one
 * greater than the upper bound on the first range (or zero, although the input has a range starting at zero). For part
 * two, keep doing this and storing the results in another set. This ends up containing all merged ranges where there is
 * no overlap. Then we can simply iterate those ranges and subtract their size from the total address space, 2^32.
 * </p>
 * <p>
 * Copyright (c) 2021 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2016Day20 {

  public long calculatePart1() {
    final SortedSet<Range> ranges = new TreeSet<>(getInput());
    Range lowest = getLowest(ranges);
    return lowest.lower > 0 ? 0 : lowest.upper + 1;
  }

  public long calculatePart2() {
    final SortedSet<Range> inputRanges = new TreeSet<>(getInput());
    final SortedSet<Range> mergedRanges = new TreeSet<>();
    // Keep getting the next merged range until the input is exhausted.
    while (!inputRanges.isEmpty()) {
      mergedRanges.add(getLowest(inputRanges));
    }
    // Now we can subtract the size of each range from the total address space.
    long addressSpace = 4_294_967_296L;
    for (Range range : mergedRanges) {
      addressSpace -= range.size();
    }
    return addressSpace;
  }

  private Range getLowest(final SortedSet<Range> ranges) {
    Range lowest = ranges.first();
    ranges.remove(lowest);
    final Iterator<Range> iter = ranges.iterator();
    while (iter.hasNext()) {
      Range next = iter.next();
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

  /** Get the input data for this solution. */
  private List<Range> getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2016, 20)).stream().map(Range::new).collect(Collectors.toList());
    }
    catch (RuntimeException ex) {
      throw ex;
    }
    catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private static final class Range
  implements Comparable<Range> {

    final long lower;

    final long upper;

    private final int hashCode;

    Range(final String input) {
      final int separator = input.indexOf('-');
      lower = Long.parseLong(input.substring(0, separator));
      upper = Long.parseLong(input.substring(separator + 1));
      hashCode = Objects.hash(Long.valueOf(lower), Long.valueOf(upper));
    }

    Range(final long _lower, final long _upper) {
      lower = _lower;
      upper = _upper;
      hashCode = Objects.hash(Long.valueOf(lower), Long.valueOf(upper));
    }

    boolean canMergeWith(final Range other) {
      // There is an actual overlap
      if (upper >= other.lower && lower <= other.upper) {
        return true;
      }
      // Ranges are non-overlapping but adjacent
      if (upper == other.lower - 1 || lower == other.upper + 1) {
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
    public boolean equals(final Object obj) {
      if (this == obj) {
        return true;
      }
      else if (!(obj instanceof Range)) {
        return false;
      }
      Range r = (Range) obj;
      return lower == r.lower && upper == r.upper;
    }

    @Override
    public int hashCode() {
      return hashCode;
    }

    @Override
    public String toString() {
      return Long.toString(lower) + "-" + upper;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(final Range o) {
      final int low = Long.compare(lower, o.lower);
      final int high = Long.compare(upper, o.upper);
      return low == 0 ? high : low;
    }

  }

}
