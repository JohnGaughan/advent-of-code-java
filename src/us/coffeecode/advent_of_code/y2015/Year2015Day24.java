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

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import us.coffeecode.advent_of_code.Utils;

/**
 * <p>
 * <a href="https://adventofcode.com/2015/day/24">Year 2015, day 24</a>. This problem is about finding combinations of
 * numbers that meet specific criteria. Specifically, find a combination from an array of integers that adds up to
 * one-third of the total sum and has the least number of integers in the combination. For example: if one combination
 * contains three integers and another has four, use the one with three. Among those that tie for the least number of
 * elements, find the one with the lowest product. Furthermore, the input is broken up into partitions: part one
 * requires the full array of integers to be broken up into three groups that all have the same sum, while part two has
 * four groups.
 * </p>
 * <p>
 * The input has useful properties. The numbers are all unique, which simplifies some logic. Among the combinations with
 * the fewest elements in the set, none of them result in the remaining, unused numbers being unable to form two (or
 * three) combinations that are unable to add up to the proper sum. The solution I came up with determines the
 * theoretical minimum number of elements that can form a combination. It then calculates the permutations where the set
 * has that size: if none are found, increment the set size by one and repeat until there are combinations. Then filter
 * any that result in the remaining numbers not forming two or three more sets: however, the input data is such that
 * this never actually happens. Finally, compute the product of each set and take the lowest one.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@coffeecode.us&gt;
 */
public final class Year2015Day24 {

  public long calculatePart1() {
    return calculateAnswer(3);
  }

  public long calculatePart2() {
    return calculateAnswer(4);
  }

  /** Common code for getting the answer based on the number of partitions. */
  private long calculateAnswer(final int partitions) {
    return getSolutionsWithLowestCount(getInput(), partitions).stream().mapToLong(
      a -> Arrays.stream(a).reduce((x, y) -> x * y).getAsLong()).min().getAsLong();
  }

  /** Get all solutions that have the lowest quantity of numbers. */
  private Collection<long[]> getSolutionsWithLowestCount(final long[] values, final int partitions) {
    final long partitionSize = Arrays.stream(values).sum() / partitions;
    // Get the minimum quantity of numbers in a solution.
    int minQty = 0;
    int sum = 0;
    for (int i = values.length - 1; i >= 0; --i) {
      sum += values[i];
      if (sum >= partitionSize) {
        minQty = values.length - i;
        break;
      }
    }
    for (int i = minQty; i < values.length; ++i) {
      final Collection<long[]> solutions = getSubArraysAddingUpTo(values, new long[0], partitionSize, i);
      for (final Iterator<long[]> iter = solutions.iterator(); iter.hasNext();) {
        final long[] used = iter.next();
        // This filters numbers in "used" from "values" - this works because numbers are unique in the input.
        final long[] filtered = Arrays.stream(values).filter(x -> Arrays.binarySearch(used, x) < 0).toArray();
        // This is not required for the problem's input, but included for correctness.
        if (!canSumTo(filtered, new long[0], partitionSize)) {
          iter.remove();
        }
      }
      if (!solutions.isEmpty()) {
        return solutions;
      }
    }
    return Collections.emptySet();
  }

  /** Recursive function that gets subarrays adding up to the given sum. */
  private Collection<long[]> getSubArraysAddingUpTo(final long[] values, final long used[], final long sum, final int qty) {
    final Collection<long[]> results = new ArrayList<>();
    for (int i = 0; i < values.length; ++i) {
      final long[] newUsed = new long[used.length + 1];
      System.arraycopy(used, 0, newUsed, 0, used.length);
      newUsed[newUsed.length - 1] = values[i];
      final long newSum = sum - values[i];
      final int newQty = qty - 1;

      if (newSum < 0) {
        break;
      }
      else if (newSum == 0) {
        if (newQty == 0) {
          results.add(newUsed);
        }
      }
      else if (newQty == 0) {
        break;
      }
      else {
        final long[] newValues = new long[values.length - i - 1];
        System.arraycopy(values, i + 1, newValues, 0, newValues.length);
        results.addAll(getSubArraysAddingUpTo(newValues, newUsed, newSum, newQty));
      }
    }

    return results;
  }

  /** Get whether there is any combination that meets the sum requirement. */
  private boolean canSumTo(final long[] values, final long used[], final long sum) {
    for (int i = 0; i < values.length; ++i) {
      final long[] newUsed = new long[used.length + 1];
      System.arraycopy(used, 0, newUsed, 0, used.length);
      newUsed[newUsed.length - 1] = values[i];
      final long newSum = sum - values[i];

      if (newSum < 0) {
        break;
      }
      else if (newSum == 0) {
        return true;
      }
      else {
        final long[] newValues = new long[values.length - i - 1];
        System.arraycopy(values, i + 1, newValues, 0, newValues.length);
        if (canSumTo(newValues, newUsed, newSum)) {
          return true;
        }
      }
    }
    return false;
  }

  /** Get the input data for this solution. */
  private long[] getInput() {
    try {
      return Files.readAllLines(Utils.getInput(2015, 24)).stream().mapToLong(Long::parseLong).sorted().toArray();
    }
    catch (final RuntimeException ex) {
      throw ex;
    }
    catch (final Exception ex) {
      throw new RuntimeException(ex);
    }
  }

}
