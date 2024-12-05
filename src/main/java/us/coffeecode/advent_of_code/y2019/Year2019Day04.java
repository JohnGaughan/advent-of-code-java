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

import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2019, day = 4, title = "Secure Container")
@Component
public final class Year2019Day04 {

  private static final Pattern SPLIT = Pattern.compile("-");

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

  private long calculate(final PuzzleContext pc) {
    long result = 0;
    final int[] bounds = il.fileAsIntsFromSplit(pc, SPLIT);
    final boolean consecutiveNotInGroupOfThree = pc.getBoolean("ConsecutiveNotInGroupOfThree");
    for (int i = bounds[0]; i < bounds[1]; ++i) {
      final char[] digits = Integer.toString(i)
                                   .toCharArray();
      boolean nonDecreasing = true;
      for (int j = 1; nonDecreasing && j < digits.length; ++j) {
        nonDecreasing = digits[j] >= digits[j - 1];
      }
      if (nonDecreasing) {
        if (consecutiveNotInGroupOfThree) {
          // Must have two consecutive identical digits that are not also part of a group of three.
          boolean groupOfTwo = false;
          for (int j = 0; !groupOfTwo && j < digits.length - 1; ++j) {
            groupOfTwo = (digits[j] == digits[j + 1]) && ((j == digits.length - 2) || (digits[j] != digits[j + 2]))
              && ((j == 0) || (digits[j] != digits[j - 1]));
          }
          if (groupOfTwo) {
            ++result;
          }
        }
        else {
          // Must have two consecutive identical digits.
          boolean groupOfTwo = false;
          for (int j = 1; !groupOfTwo && (j < digits.length); ++j) {
            groupOfTwo = (digits[j] == digits[j - 1]);
          }
          if (groupOfTwo) {
            ++result;
          }
        }
      }
    }
    return result;
  }

}
