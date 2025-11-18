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
package us.coffeecode.advent_of_code.y2016;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import us.coffeecode.advent_of_code.annotation.AdventOfCodeSolution;
import us.coffeecode.advent_of_code.annotation.Solver;
import us.coffeecode.advent_of_code.component.InputLoader;
import us.coffeecode.advent_of_code.component.PuzzleContext;

@AdventOfCodeSolution(year = 2016, day = 16)
@Component
public final class Year2016Day16 {

  @Autowired
  private InputLoader il;

  @Solver(part = 1)
  public String calculatePart1(final PuzzleContext pc) {
    return calculate(pc);
  }

  @Solver(part = 2)
  public String calculatePart2(final PuzzleContext pc) {
    return calculate(pc);
  }

  private String calculate(final PuzzleContext pc) {
    return checksum(dragon(il.fileAsString(pc), pc.getInt("length")));
  }

  private boolean[] dragon(final String input, final int length) {
    final boolean[] str = new boolean[length];
    // First "a" string.
    for (int i = 0; i < input.length(); ++i) {
      str[i] = input.charAt(i) == '1';
    }
    int nextZero = input.length();
    // Tack on zeros and "b" strings iteratively.
    for (int i = nextZero, j = nextZero - 1; i < length; ++i) {
      if (i == nextZero) {
        str[i] = false;
        j = nextZero - 1;
        nextZero = (nextZero << 1) + 1;
      }
      else {
        str[i] = !str[j];
        --j;
      }
    }
    return str;
  }

  private String checksum(final boolean[] input) {
    int partitionSize = 1;
    while (input.length % partitionSize == 0) {
      partitionSize <<= 1;
    }
    partitionSize >>= 1;
    final StringBuilder checksum = new StringBuilder(input.length / partitionSize);
    for (int i = 0; i < input.length; i += partitionSize) {
      int ones = 0;
      for (int j = i; j < i + partitionSize; ++j) {
        if (input[j]) {
          ++ones;
        }
      }
      checksum.append((ones & 1) == 0 ? '1' : '0');
    }
    // Note: the checksum may have a leading zero and yes, this matters.
    return checksum.toString();
  }

}
