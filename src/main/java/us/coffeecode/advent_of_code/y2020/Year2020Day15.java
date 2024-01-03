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

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2020, day = 15, title = "Rambunctious Recitation")
@Component
public final class Year2020Day15 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc, 2_020);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc, 30_000_000);
  }

  private long calculate(final PuzzleContext pc, final int iterations) {
    final int[] input = il.fileAsIntsFromSplit(pc, SEPARATOR);
    // Indices are numbers that are seen, values are the turn they were seen.
    final int[] valuesToLastSeen = new int[iterations];
    for (int i = 0; i < input.length; ++i) {
      valuesToLastSeen[input[i]] = i + 1;
    }

    // Iterate over each turn.
    int nextValue = 0;
    for (int i = input.length + 1; i < iterations; ++i) {
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

  private static final Pattern SEPARATOR = Pattern.compile(",");
}
