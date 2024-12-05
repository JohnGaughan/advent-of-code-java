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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2015, day = 24, title = "It Hangs in the Balance")
@Component
public final class Year2015Day24 {

  private static final String PARAMETER = "partitions";

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculateAnswer(pc, pc.getInt(PARAMETER));
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculateAnswer(pc, pc.getInt(PARAMETER));
  }

  /** Common code for getting the answer based on the number of partitions. */
  private long calculateAnswer(final PuzzleContext pc, final int partitions) {
    return getSolutionsWithLowestCount(il.linesAsLongs(pc), partitions).stream()
                                                                       .mapToLong(a -> Arrays.stream(a)
                                                                                             .reduce(1, (x, y) -> x * y))
                                                                       .min()
                                                                       .getAsLong();
  }

  /** Get all solutions that have the lowest quantity of numbers. */
  private Collection<long[]> getSolutionsWithLowestCount(final long[] values, final int partitions) {
    final long partitionSize = Arrays.stream(values)
                                     .sum()
      / partitions;
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
        final long[] filtered = Arrays.stream(values)
                                      .filter(x -> Arrays.binarySearch(used, x) < 0)
                                      .toArray();
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
  private Collection<long[]> getSubArraysAddingUpTo(final long[] values, final long used[], final long residue, final int qty) {
    final Collection<long[]> results = new ArrayList<>();
    for (int i = 0; i < values.length; ++i) {
      final long[] newUsed = new long[used.length + 1];
      System.arraycopy(used, 0, newUsed, 0, used.length);
      newUsed[newUsed.length - 1] = values[i];
      final long newResidue = residue - values[i];
      final int newQty = qty - 1;

      // Went bust: further numbers will have the same result.
      if (newResidue < 0) {
        break;
      }
      // Perfect match.
      else if (newResidue == 0) {
        if (newQty == 0) {
          results.add(newUsed);
        }
      }
      // Came up short and there are more numbers to allocate, so recurse to find them.
      else if (newQty > 0) {
        final long[] newValues = new long[values.length - i - 1];
        System.arraycopy(values, i + 1, newValues, 0, newValues.length);
        results.addAll(getSubArraysAddingUpTo(newValues, newUsed, newResidue, newQty));
      }
      // No more numbers to allocate, but we can loop and try others at this level.
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

}
