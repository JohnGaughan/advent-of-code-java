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

@AdventOfCodeSolution(year = 2025, day = 3)
@Component
public class Year2025Day03 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return calculate(pc);
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    return calculate(pc);
  }

  /** Perform the calculation which is identical between parts aside from the configurable number of digits. */
  private long calculate(final PuzzleContext pc) {
    long result = 0;
    for (final int[] line : il.linesAs2dIntArrayFromDigits(pc)) {
      result += maxScore(line, new int[0], pc.getInt("digitsNeeded"));
    }
    return result;
  }

  /** Find the maximum score for the given battery bank. */
  private long maxScore(final int[] batteryBank, final int[] used, final int digitsNeeded) {
    // Cache the start location in the array so the code is easier to read.
    final int start = ((used.length == 0) ? 0 : (used[used.length - 1] + 1));
    // This nested loop finds the first occurrence of the largest digit that leaves enough remaining digits to form a
    // solution.
    for (int n = 9; n > 0; --n) {
      for (int i = start; i <= batteryBank.length - digitsNeeded; ++i) {
        // Found the first occurrence of the largest digit: either recurse or return the solution. Either way, this
        // nested loop is done.
        if (batteryBank[i] == n) {
          // Only one more number needed, which we just found.
          if (digitsNeeded == 1) {
            long result = 0;
            for (final int digitIndex : used) {
              result += batteryBank[digitIndex];
              result *= 10;
            }
            result += batteryBank[i];
            return result;
          }
          // Need more results: recurse.
          final int[] newUsed = new int[used.length + 1];
          System.arraycopy(used, 0, newUsed, 0, used.length);
          newUsed[used.length] = i;
          return maxScore(batteryBank, newUsed, digitsNeeded - 1);
        }
      }
    }
    return 0;
  }

}
