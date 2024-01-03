/* Advent of Code answers written by John Gaughan
 * Copyright (C) 2021  John Gaughan
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
package us.coffeecode.advent_of_code.y2019;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2019, day = 16, title = "Flawed Frequency Transmission")
@Component
public final class Year2019Day16 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    int[] numbers = il.fileAsIntsFromDigits(pc);
    for (int i = 0; i < 100; ++i) {
      numbers = iterationPart1(numbers);
    }
    return getInteger(numbers, 8);
  }

  private int[] iterationPart1(final int[] input) {
    final int[] output = new int[input.length];
    for (int i = 0; i < output.length; ++i) {
      // Once we get to the second half of the input, we ignore the entire first half because the multipliers are all
      // zeroes. The second half's multipliers are all one. Furthermore, there are a lot of repeated calculations that
      // we can optimize out by working in reverse. This is a version of the optimization used for part two, except it
      // does not mess up digits in the first half which will then cause a wrong answer for part one.
      if (i > output.length >> 1) {
        int sum = 0;
        for (int j = input.length - 1; j >= i; --j) {
          sum = (input[j] + sum) % 10;
          output[j] = sum;
        }
      }
      else {
        long sum = 0;
        final int cycleLength = i + 1 << 2;
        for (int j = i; j < input.length; ++j) {
          final int cycleOffset = (j + 1) % cycleLength;
          final int modIdx = cycleOffset / (cycleLength >> 2);
          if (modIdx % 2 == 0) {
            // Skip parts of the cycle that will be zero.
            j += i;
            continue;
          }
          sum += input[j] * MULTIPLIERS[modIdx];
        }
        // ABS of a negative value will not provide expected results.
        output[i] = (int) (Math.abs(sum) % 10);
      }
    }
    return output;
  }

  /** Multipliers for each part of a phase cycle. */
  private static final int[] MULTIPLIERS = new int[] { 0, 1, 0, -1 };

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final int repetitions = 10_000;
    int[] input = il.fileAsIntsFromDigits(pc);
    final int offset = getInteger(input, 7);
    int[] numbers = new int[input.length * repetitions - offset];
    int remainder = offset % input.length;
    System.arraycopy(input, remainder, numbers, 0, input.length - remainder);
    for (int i = input.length - remainder; i < numbers.length; i += input.length) {
      System.arraycopy(input, 0, numbers, i, input.length);
    }
    for (int i = 0; i < 100; ++i) {
      numbers = iterationPart2(numbers);
    }
    return getInteger(numbers, 8);
  }

  private int[] iterationPart2(final int[] input) {
    final int[] output = new int[input.length];
    int sum = 0;
    for (int i = input.length - 1; i >= 0; --i) {
      sum += input[i];
      output[i] = sum % 10;
    }
    return output;
  }

  private int getInteger(final int[] numbers, final int length) {
    int value = 0;
    for (int i = 0; i < length; ++i) {
      value *= 10;
      value += numbers[i];
    }
    return value;
  }

}
