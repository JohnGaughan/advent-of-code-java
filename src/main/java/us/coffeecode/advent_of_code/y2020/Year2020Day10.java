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
package us.coffeecode.advent_of_code.y2020;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2020, day = 10, title = "Adapter Array")
@Component
public final class Year2020Day10 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    long previous = 0;
    long diff1 = 0;
    long diff3 = 0;
    for (final long i : il.linesAsLongsSorted(pc)) {
      final long diff = i - previous;
      if (diff == 1) {
        ++diff1;
      }
      else if (diff == 3) {
        ++diff3;
      }
      previous = i;
    }
    // add 3 for the device.
    ++diff3;
    return diff1 * diff3;
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    final long[] numbers = prepareForPart2(il.linesAsLongsSorted(pc));
    final Map<Long, Long> numCombinations = new HashMap<>();

    // Base case: last number has one path.
    numCombinations.put(Long.valueOf(numbers[0]), Long.valueOf(1));

    // Iterate the numbers, skipping the base case.
    for (int i = 1; i < numbers.length; ++i) {
      long newPaths = 0;
      for (long j = numbers[i] + 1; j <= numbers[i] + 3; ++j) {
        final Long key = Long.valueOf(j);
        if (numCombinations.containsKey(key)) {
          newPaths += numCombinations.get(key)
                                     .longValue();
        }
      }
      numCombinations.put(Long.valueOf(numbers[i]), Long.valueOf(newPaths));
    }

    return numCombinations.get(Long.valueOf(0))
                          .longValue();
  }

  /** Prepare the input for part 2. */
  private long[] prepareForPart2(final long[] input) {
    // Array is sorted low to high. We need it high to low, with a zero on the end.
    final long[] output = new long[input.length + 1];
    for (int i = 0; i < input.length; ++i) {
      output[output.length - i - 2] = input[i];
    }
    output[output.length - 1] = 0;
    return output;
  }

}
