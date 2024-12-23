/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2024  John Gaughan
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
package us.coffeecode.advent_of_code.y2024;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2024, day = 22, title = "Monkey Market")
@Component
public class Year2024Day22 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return Arrays.stream(il.linesAsInts(pc))
                 .map(this::hash)
                 .mapToLong(n -> n)
                 .sum();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int[] bananas = new int[DELTA_BITMASK];
    Arrays.stream(il.linesAsInts(pc))
          .forEach(n -> addToBananas(n, bananas));
    return Arrays.stream(bananas)
                 .max()
                 .getAsInt();
  }

  /**
   * Given a monkey with the hash seed, calculate each round of the hash function. For each state where there are four
   * differences, store the first matching value into the map by adding its banana count to the key that matches that
   * state.
   */
  private void addToBananas(final int seed, final int[] bananas) {
    final boolean[] seen = new boolean[DELTA_BITMASK];
    final int[] values = new int[2];
    final int[] scores = new int[2];
    int delta = 0;

    // Seed the initial values.
    values[1] = seed;
    scores[1] = values[1] % 10;

    // Check every "iteration" except the first.
    for (int i = 1; i < HASH_ITERATIONS; ++i) {
      // Move the old values to the previous slot, then get the new current values.
      values[0] = values[1];
      scores[0] = scores[1];
      values[1] = oneRound(values[0]);
      scores[1] = values[1] % 10;
      // Delta difference moves the old value left, adds the new value right, and masks off differences past four.
      delta = ((delta << 5) + scores[1] - scores[0] + 9) & DELTA_BITMASK;
      // If there are enough deltas and this state has not been seen before, add it into the map.
      if ((i > 3) && !seen[delta]) {
        bananas[delta] += scores[1];
        seen[delta] = true;
      }
    }
  }

  /** Calculate the final hash value for the seed across all rounds. */
  private int hash(final int seed) {
    int value = seed;
    for (int i = 0; i < HASH_ITERATIONS; ++i) {
      value = oneRound(value);
    }
    return value;
  }

  /** Performs one round of the hash function. */
  private int oneRound(final int number) {
    // Use bitwise operators instead of multiplication, division, and modulo. All operands are powers of two which makes
    // that possible, and doing it this way is significantly faster.
    int value = number;
    value = (value ^ (value << 6)) & HASH_BITMASK;
    value = (value ^ (value >> 5));
    value = (value ^ (value << 11)) & HASH_BITMASK;
    return value;
  }

  /** Total number of iterations to execute in the hash function. */
  private static final int HASH_ITERATIONS = 2_000;

  /** Bit mask that ensures the hash value is limited to 24 bits. */
  private static final int HASH_BITMASK = 0xFFFFFF;

  /** Bit mask used to limit the delta key to 20 bits. */
  private static final int DELTA_BITMASK = 0xFFFFF;
}
