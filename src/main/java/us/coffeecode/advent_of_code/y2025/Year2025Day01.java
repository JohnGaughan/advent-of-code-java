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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.function.BiIntToLongFunction;

@AdventOfCodeSolution(year = 2025, day = 1)
@Component
public class Year2025Day01 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, this::clicksPart1);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, this::clicksPart2);
  }

  /** Common calculation logic. */
  private long calculate(final PuzzleContext pc, final BiIntToLongFunction clicker) {
    long result = 0;
    int prev = DIAL_START;
    for (final int distance : getInput(pc)) {
      final int next = prev + distance;
      result += clicker.apply(prev, next);
      // Modulus operator can return negative values: this keeps the value positive because the second argument is
      // always positive.
      prev = Math.floorMod(next, DIAL_SIZE);
    }
    return result;
  }

  /** Calculate the number of clicks for part 1, where the dial must land on zero. */
  private long clicksPart1(final int prev, final int next) {
    return (next % DIAL_SIZE == 0) ? 1 : 0;
  }

  /**
   * Calculate the number of clicks for part 1, where the dial lands on or passes over zero. This must account for
   * various edge cases and the fact that the dial may make multiple full rotations during one round of the algorithm.
   * Specifically, zero is a special number that can only be directly reached by turning left. Multiple rotations can
   * land on another multiple of the dial size as the next number, but the previous number can only be the zeroth
   * multiple of the dial size (i.e., zero). This is because after this step, the next number is normalized to be within
   * range of zero to the dial size minus one when it then becomes the previous number.
   */
  private long clicksPart2(final int prev, final int next) {
    // Turned left. Note that the next value may be either positive or negative.
    if (next < prev) {
      // If the dial started at zero, use integer division which rounds toward zero. This ignores the first zero which
      // was counted during the previous round. Next value must be negative here, so flip it to get the positive number
      // of clicks.
      if (prev == 0) {
        return -next / DIAL_SIZE;
      }
      // Otherwise, combine and simplify two other cases. If we ended up on a positive number then it must be less than
      // the dial size: this division truncates to zero. If we ended up on zero or a negative number, then adding in the
      // dial size causes division to count that first spin past (or onto) zero.
      return (DIAL_SIZE - next) / DIAL_SIZE;
    }

    // Turned right. There are no edge cases involving zero: simply count how many times it passes zero.
    else {
      return next / DIAL_SIZE;
    }
  }

  /** Value on which the dial starts before turning it. */
  private static final int DIAL_START = 50;

  /** Count of numbers on the dial, ranging from zero to one minus this number. */
  private static final int DIAL_SIZE = 100;

  /** Get the puzzle input as a sequence of integers, where left turns are negative. */
  private int[] getInput(final PuzzleContext pc) {
    return il.linesAsInts(pc, s -> Integer.parseInt(s.replace('L', '-')
                                                     .replace("R", "")));
  }
}
