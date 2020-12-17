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
package net.johngaughan.advent_of_code.y2020;

/**
 * <p>
 * <a href="https://adventofcode.com/2020/day/15">Year 2020, day 15</a>. This is a simple numbers puzzle where each
 * value in the sequence is either zero if the previous number does not exist earlier in the sequence, or it is equal to
 * the difference between its current and previous positions in the sequence. Parts one and two run this algorithm for
 * different iteration counts.
 * </p>
 * <p>
 * The naive implementation is to store everything in a list or array where the index is the iteration number: f(2020)
 * would be stored in position 2020, for example. This works fine for small numbers of iterations, but quickly grows
 * infeasible as that algorithm is <code>O(n<sup>2</sup>)</code>. Swapping the mapping so the array or list index is the
 * value seen and the value stored at that location is the last iteration it was seen turns this into an
 * <code>O(n)</code> operation.
 * </p>
 * <p>
 * Incidentally, using a list in Java rather than an array has a practical side effect of increasing the runtime
 * dramatically due to a large amount of constant operations: notably, boxing and unboxing. This is also true if using a
 * hash map to store the values, except it is worse due to additional operations such as function calls to hash keys and
 * dealing with chained entries. Using the array approach is the most efficient by an order of magnitude.
 * </p>
 * <p>
 * Copyright (c) 2020 John Gaughan
 * </p>
 *
 * @author John Gaughan &lt;john@johngaughan.net&gt;
 */
public final class Year2020Day15 {

  private static final int[] INPUT = new int[] { 1, 12, 0, 20, 8, 16 };

  public int calculatePart1() {
    return calculate(2_020);
  }

  public int calculatePart2() {
    return calculate(30_000_000);
  }

  private int calculate(final int iterations) {
    // Indices are numbers that are seen, values are the turn they were seen.
    final int[] valuesToLastSeen = new int[iterations];
    for (int i = 0; i < INPUT.length; ++i) {
      valuesToLastSeen[INPUT[i]] = i + 1;
    }

    // Iterate over each turn.
    int nextValue = 0;
    for (int i = INPUT.length + 1; i < iterations; ++i) {
      // If this value has not been seen before: mark it as seen this turn, and next value is 0.
      if (valuesToLastSeen[nextValue] == 0) {
        valuesToLastSeen[nextValue] = i;
        nextValue = 0;
      }
      else {
        // If this value has been seen before: calculate the next value, then set it to this turn.
        final int temp = valuesToLastSeen[nextValue];
        valuesToLastSeen[nextValue] = i;
        nextValue = i - temp;
      }
    }
    return nextValue;
  }

}
