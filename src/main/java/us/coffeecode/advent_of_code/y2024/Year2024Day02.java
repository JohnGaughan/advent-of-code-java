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
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;
import us.coffeecode.advent_of_code.util.MyArrays;
import us.coffeecode.advent_of_code.util.MyIntMath;

@AdventOfCodeSolution(year = 2024, day = 2)
@Component
public class Year2024Day02 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public long calculatePart1(final PuzzleContext pc) {
    return Arrays.stream(il.linesAs2dIntArrayFromSplit(pc, SPLIT))
                 .filter(this::isSafe)
                 .count();
  }

  @Solver(part = 2)
  public long calculatePart2(final PuzzleContext pc) {
    long safe = 0;
    for (int[] report : il.linesAs2dIntArrayFromSplit(pc, SPLIT)) {
      if (isSafe(report)) {
        ++safe;
      }
      else {
        for (int i = 0; i < report.length; ++i) {
          if (isSafe(MyArrays.copyOmitting(report, i))) {
            ++safe;
            break;
          }
        }
      }
    }
    return safe;
  }

  private boolean isSafe(final int[] report) {
    // Fail fast once a report fails the "safe" condition.
    // Base case: first two elements.
    int diff = report[1] - report[0];
    if ((Math.abs(diff) > 3) || (MyIntMath.signum(diff) == 0)) {
      return false;
    }
    // Iterative case: current and previous element.
    for (int i = 2; i < report.length; ++i) {
      final int nextDiff = report[i] - report[i - 1];
      if ((MyIntMath.signum(diff) != MyIntMath.signum(nextDiff)) || (Math.abs(nextDiff) > 3)
        || (MyIntMath.signum(nextDiff) == 0)) {
        return false;
      }
      diff = nextDiff;
    }
    return true;
  }

  private static final Pattern SPLIT = Pattern.compile("\\s");
}
